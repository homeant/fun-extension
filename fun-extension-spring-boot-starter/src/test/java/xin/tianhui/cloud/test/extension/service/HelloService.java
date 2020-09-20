package xin.tianhui.cloud.test.extension.service;

import xin.tianhui.cloud.extension.IExtensionPoint;
import xin.tianhui.cloud.extension.annotation.ExtensionPoint;

@ExtensionPoint(value = "helloService",desc = "扩展点示例")
public interface HelloService extends IExtensionPoint {
    String hello(String name);
}
