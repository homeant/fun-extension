package xin.tianhui.cloud.test.extension.service.impl;

import xin.tianhui.cloud.extension.annotation.ExtensionService;
import xin.tianhui.cloud.test.extension.service.HelloService;

@ExtensionService(name = "helloService", bizCode = "xin.tianhui2.cloud.hello")
public class HelloService2Impl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello 2 " + name;
    }
}
