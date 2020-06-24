package xin.vyse.cloud.extension;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xin.vyse.cloud.extension.annotation.ExtensionPoint;
import xin.vyse.cloud.extension.domain.Context;
import xin.vyse.cloud.extension.domain.ExtensionObject;
import xin.vyse.cloud.extension.domain.ExtensionPointObject;
import xin.vyse.cloud.extension.exception.ExtensionException;
import xin.vyse.cloud.extension.repository.ExtensionRepository;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 扩展点执行器
 *
 * @author vyse.guaika
 */
public class ExtensionExecutor implements ApplicationContextAware {

    @Autowired
    private ExtensionRepository extensionRepository;

    private ApplicationContext applicationContext;


    public <R, C> R execute(Class<C> target, Context context, Function<C, R> exeFunction) {
        if (context == null) {
            throw new ExtensionException("context is null");
        }
        return exeFunction.apply(getExtensionPoint(target, context));
    }

    private <C> C getExtensionPoint(Class<C> target, Context context) {
        ExtensionPoint annotation = target.getAnnotation(ExtensionPoint.class);
        if (annotation != null) {
            ExtensionPointObject extensionPointObject = new ExtensionPointObject();
            extensionPointObject.setName(annotation.name());
            extensionPointObject.setDesc(annotation.desc());
            extensionPointObject.setTarget((Class<IExtensionPoint>) target);
            List<ExtensionObject> extensionObjects = extensionRepository.extensions(extensionPointObject);
            return (C) extensionObjects.stream().filter(extensionObject -> Objects.equals(extensionObject.getBizCode(), context.getBizCode()))
                    .findFirst().map(bean -> applicationContext.getBean(bean.getTarget()))
                    .orElseThrow(() -> new ExtensionException(context.getBizCode(), "not find"));
        }
        throw new ExtensionException("not find class of " + target.getCanonicalName());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
