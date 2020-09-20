package xin.tianhui.cloud.extension;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xin.tianhui.cloud.extension.annotation.ExtensionService;
import xin.tianhui.cloud.extension.domain.Context;
import xin.tianhui.cloud.extension.exception.ExtensionException;

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
            Map<String, C> serviceBeans = applicationContext.getBeansOfType(target);
            for (C service : serviceBeans.values()) {
                ExtensionService annotation = service.getClass().getAnnotation(ExtensionService.class);
                if (annotation != null) {
                    String[] bizCodes = annotation.bizCode();
                    for (int i = 0; i < bizCodes.length; i++) {
                        if (Objects.equals(bizCodes[i], context.getBizCode())) {
                            return service;
                        }
                    }
                }
            }
        } catch (BeansException ex) {
            throw new ExtensionException(context.getBizCode(), ex);
        }
        throw new ExtensionException(context.getBizCode(), "extensionService of " + target.getCanonicalName() + " could not be found");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
