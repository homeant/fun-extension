package xin.vyse.cloud.extension.factory;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xin.vyse.cloud.extension.IExtensionPoint;

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
        return (IExtensionPoint) type.getDeclaredConstructors()[0].newInstance();
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
