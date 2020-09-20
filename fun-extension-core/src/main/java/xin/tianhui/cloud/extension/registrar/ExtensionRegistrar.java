package xin.tianhui.cloud.extension.registrar;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import xin.tianhui.cloud.extension.annotation.EnableExtension;
import xin.tianhui.cloud.extension.annotation.ExtensionPoint;
import xin.tianhui.cloud.extension.annotation.ExtensionService;
import xin.tianhui.cloud.extension.domain.Point;
import xin.tianhui.cloud.extension.domain.Extension;
import xin.tianhui.cloud.extension.repository.ExtensionRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 扩展点注册扫描
 *
 * @author vyse.guaika
 */
@Slf4j
public class ExtensionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private Environment environment;

    private ResourceLoader resourceLoader;

    private ExtensionRepository repository = ExtensionRepository.INSTANCE();


    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(ExtensionPoint.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        Set<String> basePackages = getBasePackages(metadata);
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    Assert.isTrue(annotationMetadata.isInterface(), "@ExtensionPoint can only be specified on an interface");
                    Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(ExtensionPoint.class.getCanonicalName());
                    this.repositoryExtensionPoint(registry, annotationMetadata, attributes);
                    ClassPathScanningCandidateComponentProvider serviceScanner = getScanner();
                    serviceScanner.setResourceLoader(this.resourceLoader);
                    serviceScanner.addIncludeFilter(new AnnotationTypeFilter(ExtensionService.class));
                    for (BeanDefinition serviceCandidateComponent : serviceScanner.findCandidateComponents(basePackage)) {
                        if (serviceCandidateComponent instanceof AnnotatedBeanDefinition) {
                            AnnotationMetadata serviceAnnotationMetadata = ((AnnotatedBeanDefinition) serviceCandidateComponent).getMetadata();
                            Map<String, Object> serviceAttributes = serviceAnnotationMetadata.getAnnotationAttributes(ExtensionService.class.getCanonicalName());
                            this.repositoryExtensionService(annotationMetadata, registry, serviceAnnotationMetadata, serviceAttributes);
                        }
                    }
                }
            }
        }
    }

    /**
     * 注册扩展点
     *
     * @param registry
     * @param annotationMetadata
     * @param attributes
     */
    private void repositoryExtensionService(AnnotationMetadata pointAnnotationMetadata, BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(className);
        String name = this.getName(attributes);
        String alias = this.getAlias(attributes, className);
        Extension extension = new Extension();
        extension.setBizCode(this.getBizCode(attributes));
        extension.setClassName(className);
        extension.setName(name);
        extension.setDesc(getDesc(attributes));
        repository.getPointList().get(pointAnnotationMetadata.getClassName()).getExtensionList().add(extension);
        definition.setAutowireMode(2);
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, new String[]{alias});
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private String getName(Map<String, Object> attributes) {
        return (String) attributes.get("name");
    }

    private String getValue(Map<String, Object> attributes) {
        return (String) attributes.get("value");
    }

    private String getDesc(Map<String, Object> attributes) {
        return (String) attributes.get("desc");
    }

    private String[] getBizCode(Map<String, Object> attributes) {
        return (String[]) attributes.get("bizCode");
    }

    private String getTenant(Map<String, Object> attributes) {
        return (String) attributes.get("tenant");
    }

    private String getAlias(Map<String, Object> attributes, String className) {
        String value = (String) attributes.get("value");
        if (!StringUtils.hasText(value)) {
            value = className.substring(className.lastIndexOf(".") + 1);
            return value.substring(0, 1).toLowerCase() + value.substring(1);
        }
        return value;
    }

    /**
     * 注册扩展点接口
     *
     * @param registry
     * @param annotationMetadata
     * @param attributes
     */
    private void repositoryExtensionPoint(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        Point point = new Point();
        point.setClassName(className);
        point.setName(getValue(attributes));
        point.setDesc(getDesc(attributes));
        point.setExtensionList(new ArrayList<>());
        repository.getPointList().put(className, point);
    }


    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableExtension.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (pkg != null && !"".equals(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }


        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    /**
     * 扫描器
     *
     * @return
     */
    protected ClassPathScanningCandidateComponentProvider getScanner() {

        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {

            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation()) {
                    isCandidate = true;
                }
                return isCandidate;
            }
        };
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
