package xin.vyse.cloud.extension.factory;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xin.vyse.cloud.extension.IExtensionPoint;
import xin.vyse.cloud.extension.proxy.ExtensionSpringProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 扩展点工厂
 *
 * @author xiaobang_1118
 */
@Data
public class ExtensionServiceFactoryBean implements FactoryBean<IExtensionPoint>, ApplicationContextAware {

    private Class<IExtensionPoint> type;

    private String name;

    private String[] bizCode;

    private String tenant;

    private ApplicationContext applicationContext;

    @Override
    public IExtensionPoint getObject() throws Exception {
        return type.newInstance();
        //return (IExtensionPoint) new ExtensionSpringProxy().getInstance(type);
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
