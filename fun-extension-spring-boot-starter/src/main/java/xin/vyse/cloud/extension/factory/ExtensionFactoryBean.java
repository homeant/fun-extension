package xin.vyse.cloud.extension.factory;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import xin.vyse.cloud.extension.IExtensionPoint;
import xin.vyse.cloud.extension.proxy.ExtensionSpringProxy;
import xin.vyse.cloud.extension.repository.ExtensionRepository;

@Data
public class ExtensionFactoryBean<T extends IExtensionPoint> implements FactoryBean<T>, ApplicationListener<ContextRefreshedEvent> {

    private Class<T> targetClass;

    private ExtensionRepository extensionRepository;

    @Override
    public T getObject() throws Exception {
        return (T) new ExtensionSpringProxy().getInstance(targetClass);
    }

    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    }
}
