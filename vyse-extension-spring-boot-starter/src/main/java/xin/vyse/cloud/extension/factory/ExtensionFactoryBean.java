package xin.vyse.cloud.extension.factory;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import xin.vyse.cloud.extension.proxy.ExtensionProxy;

@Data
public class ExtensionFactoryBean<T> implements FactoryBean<T> {

    private Class<T> targetClass;

    @Override
    public T getObject() throws Exception {
        return (T) new ExtensionProxy().getInstance(targetClass);
    }

    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }
}
