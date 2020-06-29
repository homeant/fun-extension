package xin.vyse.cloud.extension;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;
import xin.vyse.cloud.extension.annotation.ExtensionService;
import xin.vyse.cloud.extension.domain.Context;
import xin.vyse.cloud.extension.exception.ExtensionException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * 扩展点执行器
 *
 * @author vyse.guaika
 */
public class ExtensionExecutor implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    public <R, C> R execute(Class<C> target, Context context, Function<C, R> exeFunction) {
        if (context == null) {
            throw new ExtensionException("context is null");
        }
        return exeFunction.apply(getExtensionService(target, context));
    }

    private <C> C getExtensionService(Class<C> target, Context context) {
        try {
            Map<String, C> beansOfType = applicationContext.getBeansOfType(target);
            for (C type : beansOfType.values()) {
                Class<?> typeClass = ClassUtils.forName(type.getClass().getName(),type.getClass().getClassLoader());
                applicationContext.getType(type.getClass().getName());
                ExtensionService annotation = typeClass.getAnnotation(ExtensionService.class);
                if (annotation != null && Objects.equals(annotation.bizCode(), context.getBizCode())) {
                    return (C) applicationContext.getBean(typeClass);
                }
            }
        } catch (BeansException | ClassNotFoundException ex) {
            throw new ExtensionException(context.getBizCode(), ex);
        }
        throw new ExtensionException(context.getBizCode(), "extensionService of "+target.getCanonicalName()+" could not be found");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
