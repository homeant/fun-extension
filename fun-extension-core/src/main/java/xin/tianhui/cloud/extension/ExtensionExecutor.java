package xin.tianhui.cloud.extension;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xin.tianhui.cloud.extension.domain.Context;
import xin.tianhui.cloud.extension.domain.Point;
import xin.tianhui.cloud.extension.domain.Extension;
import xin.tianhui.cloud.extension.exception.ExtensionException;
import xin.tianhui.cloud.extension.repository.ExtensionRepository;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 扩展点执行器
 *
 * @author vyse.guaika
 */
public class ExtensionExecutor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final ExtensionRepository repository;

    public ExtensionExecutor(ExtensionRepository repository) {
        this.repository = repository;
    }


    public <R, C> R submit(Class<C> target, Context context, Function<C, R> function) {
        if (context == null) {
            throw new ExtensionException("context is null");
        }
        return function.apply(loadExtensionService(target, context));
    }

    public <T> void execute(Class<T> target, Context context, Consumer<T> consumer) {
        if (context == null) {
            throw new ExtensionException("context is null");
        }
        consumer.accept(loadExtensionService(target, context));
    }

    private <C> C loadExtensionService(Class<C> target, Context context) {
        Point point = repository.getPointList().get(target.getName());
        if (point != null) {
            List<Extension> extensionList = point.getExtensionList();
            for (Extension extension : extensionList) {
                String[] bizCodes = extension.getBizCode();
                for (int i = 0; i < bizCodes.length; i++) {
                    if (Objects.equals(bizCodes[i], context.getBizCode())) {
                        return (C) applicationContext.getBean(extension.getClassName());
                    }
                }
            }
        }
        throw new ExtensionException(context.getBizCode(), "not find extension");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
