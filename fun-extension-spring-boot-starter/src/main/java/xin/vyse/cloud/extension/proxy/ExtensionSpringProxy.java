package xin.vyse.cloud.extension.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author vyse.guaika
 */
@Slf4j
public class ExtensionSpringProxy {

    public Object getInstance(Class<?> targetClass){
        ProxyFactory proxyFactory = new ProxyFactory();
        try {
            proxyFactory.setTarget(targetClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(),e);
        }
        return proxyFactory.getProxy();
    }
}
