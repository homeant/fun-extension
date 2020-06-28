package xin.vyse.cloud.test.extension.service;

import xin.vyse.cloud.extension.IExtensionPoint;
import xin.vyse.cloud.extension.annotation.ExtensionPoint;

@ExtensionPoint(name = "hello service")
public interface HelloService extends IExtensionPoint {
    String hello(String name);
}
