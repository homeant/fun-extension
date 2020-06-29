package xin.vyse.cloud.extension.factory;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xin.vyse.cloud.extension.proxy.ExtensionSpringProxy;

/**
 * 扩展点工厂
 *
 * @author xiaobang_1118
 */
@Data
public class ExtensionServiceFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private Class<T> type;

    private String name;

    private ApplicationContext applicationContext;

    @Override
    public T getObject() throws Exception {
        return (T) new ExtensionSpringProxy().getInstance(type);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
