package xin.vyse.cloud.extension.registrar;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import xin.vyse.cloud.extension.IExtensionPoint;
import xin.vyse.cloud.extension.annotation.EnableExtension;
import xin.vyse.cloud.extension.annotation.ExtensionService;
import xin.vyse.cloud.extension.annotation.ExtensionPoint;
import xin.vyse.cloud.extension.domain.ExtensionObject;
import xin.vyse.cloud.extension.domain.ExtensionPointObject;
import xin.vyse.cloud.extension.exception.ExtensionException;
import xin.vyse.cloud.extension.factory.ExtensionFactoryBean;
import xin.vyse.cloud.extension.repository.ExtensionRepository;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扩展点注册扫描
 *
 * @author vyse.guaika
 */
@Slf4j
public class ExtensionScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {

    private ClassLoader classLoader;

    private Environment environment;

    private ResourceLoader resourceLoader;

    private Map<ExtensionPointObject, List<ExtensionObject>> repository = new ConcurrentHashMap<>();

    private Set<String> basePackages;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(ExtensionPoint.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        this.basePackages = getBasePackages(metadata);
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    this.repository(annotationMetadata, registry);
                }
            }
        }
        log.info("repository:{}", this.repository);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ExtensionRepository.class);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        definition.getPropertyValues().add("repository", this.repository);
        definition.setBeanClass(ExtensionRepository.class);
        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        String beanId = StringUtils.uncapitalize(ExtensionRepository.class.getSimpleName());
        registry.registerBeanDefinition(beanId, definition);
    }

    private void repository(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        try {
            String className = annotationMetadata.getClassName();
            Class<?> beanClazz = Class.forName(className);
            ExtensionPoint extensionPoint = beanClazz.getAnnotation(ExtensionPoint.class);
            if (extensionPoint != null) {
                ExtensionPointObject pointObject = new ExtensionPointObject();
                pointObject.setTarget((Class<IExtensionPoint>) beanClazz);
                pointObject.setName(extensionPoint.name());
                pointObject.setDesc(extensionPoint.desc());
                this.repositoryExtension(pointObject, registry);
            }
        } catch (ClassNotFoundException e) {
            throw new ExtensionException(e);
        }
    }

    private void repositoryExtension(ExtensionPointObject extensionPoint, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getImplScanner();
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(ExtensionService.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    try {
                        String className = annotationMetadata.getClassName();
                        Class<?> beanClazz = Class.forName(className);
                        ExtensionService extensionService = beanClazz.getAnnotation(ExtensionService.class);
                        if (extensionService != null) {
                            ExtensionObject extensionObject = new ExtensionObject();
                            extensionObject.setTarget((Class<IExtensionPoint>) beanClazz);
                            extensionObject.setBizCode(extensionService.bizCode());
                            extensionObject.setName(extensionService.name());
                            extensionObject.setDesc(extensionService.desc());

                            // 注入bean
                            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ExtensionRepository.class);
                            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                            definition.getPropertyValues().add("targetClass", beanClazz);
                            definition.setBeanClass(ExtensionFactoryBean.class);
                            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                            String beanId = StringUtils.uncapitalize(beanClazz.getSimpleName());
                            registry.registerBeanDefinition(beanId, definition);
                            this.put(extensionPoint, extensionObject);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new ExtensionException(e);
                    }
                }
            }
        }
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

    protected ClassPathScanningCandidateComponentProvider getScanner() {

        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {

            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (beanDefinition.getMetadata().isInterface()
                            && beanDefinition.getMetadata().getInterfaceNames().length == 1
                            && Annotation.class.getName().equals(beanDefinition.getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(),
                                    ExtensionScannerRegistrar.this.classLoader);
                            return !target.isAnnotation();
                        } catch (Exception ex) {
                            this.logger.error(
                                    "Could not load target class: " + beanDefinition.getMetadata().getClassName(), ex);

                        }
                    }
                    return true;
                }
                return false;

            }
        };
    }

    protected ClassPathScanningCandidateComponentProvider getImplScanner() {

        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {

            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (beanDefinition.getMetadata().getInterfaceNames().length == 1
                            && Annotation.class.getName().equals(beanDefinition.getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(),
                                    ExtensionScannerRegistrar.this.classLoader);
                            return !target.isAnnotation();
                        } catch (Exception ex) {
                            this.logger.error(
                                    "Could not load target class: " + beanDefinition.getMetadata().getClassName(), ex);

                        }
                    }
                    return true;
                }
                return false;

            }
        };
    }

    public void put(ExtensionPointObject extensionPointObject, ExtensionObject extensionObject) {
        if (this.repository.containsKey(extensionPointObject)) {
            List<ExtensionObject> extensionObjects = repository.get(extensionPointObject);
            if (extensionObjects != null && !extensionObjects.isEmpty()) {
                extensionObjects.add(extensionObject);
            } else {
                ArrayList<ExtensionObject> extensionObjectList = new ArrayList<>();
                extensionObjectList.add(extensionObject);
                repository.put(extensionPointObject, extensionObjectList);
            }
        } else {
            ArrayList<ExtensionObject> extensionObjectList = new ArrayList<>();
            extensionObjectList.add(extensionObject);
            repository.put(extensionPointObject, extensionObjectList);
        }
    }

    public void put(ExtensionPointObject extensionPointObject, List<ExtensionObject> extensionObjectList) {
        if (this.repository.containsKey(extensionPointObject)) {
            List<ExtensionObject> extensionObjects = repository.get(extensionPointObject);
            if (extensionObjects != null && !extensionObjects.isEmpty()) {
                extensionObjects.addAll(extensionObjectList);
            } else {
                repository.put(extensionPointObject, extensionObjectList);
            }
        } else {
            repository.put(extensionPointObject, extensionObjectList);
        }
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
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
