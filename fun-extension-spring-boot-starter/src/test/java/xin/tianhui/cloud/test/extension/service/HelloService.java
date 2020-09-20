package xin.tianhui.cloud.test.extension.service;

import xin.tianhui.cloud.extension.IExtensionPoint;
import xin.tianhui.cloud.extension.annotation.ExtensionPoint;

@ExtensionPoint(name = "hello service")
public interface HelloService extends IExtensionPoint {
    String hello(String name);
}
