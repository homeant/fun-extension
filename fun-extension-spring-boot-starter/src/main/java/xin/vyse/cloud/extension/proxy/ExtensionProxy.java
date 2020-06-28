package xin.vyse.cloud.extension.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ExtensionProxy implements MethodInterceptor {

    public  Object getInstance(Class<?> targetClazz){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return methodProxy.invokeSuper(obj,args);
    }
}
