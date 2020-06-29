package xin.vyse.cloud.extension.registrar;


import lombok.Data;
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
import xin.vyse.cloud.extension.annotation.EnableExtension;
import xin.vyse.cloud.extension.annotation.ExtensionPoint;
import xin.vyse.cloud.extension.annotation.ExtensionService;
import xin.vyse.cloud.extension.domain.ExtensionObject;
import xin.vyse.cloud.extension.domain.ExtensionPointObject;
import xin.vyse.cloud.extension.factory.ExtensionServiceFactoryBean;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扩展点注册扫描
 *
 * @author vyse.guaika
 */
@Slf4j
@Data
public class ExtensionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private Environment environment;

    private ResourceLoader resourceLoader;

    private Map<ExtensionPointObject, List<ExtensionObject>> repository = new ConcurrentHashMap<>();


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
                            this.repositoryExtension(registry, serviceAnnotationMetadata, serviceAttributes);
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
    private void repositoryExtension(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(ExtensionServiceFactoryBean.class);
        String name = this.getName(attributes);
        String alias = this.getAlias(attributes);
        definition.addPropertyValue("name", name);
        definition.addPropertyValue("type", className);
        definition.setAutowireMode(2);
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        beanDefinition.setAttribute("factoryBeanObjectType", className);
//        boolean primary = (Boolean) attributes.get("primary");
//        beanDefinition.setPrimary(primary);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, new String[]{alias});
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private String getName(Map<String, Object> attributes) {
        return (String) attributes.get("name");
    }

    private String getAlias(Map<String, Object> attributes) {
        return (String) attributes.get("name");
    }

    /**
     * 注册扩张点接口
     *
     * @param registry
     * @param annotationMetadata
     * @param attributes
     */
    private void repositoryExtensionPoint(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();

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
}
