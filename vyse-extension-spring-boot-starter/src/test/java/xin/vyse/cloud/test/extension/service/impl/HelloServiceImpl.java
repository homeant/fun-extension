package xin.vyse.cloud.test.extension.service.impl;

import xin.vyse.cloud.extension.annotation.ExtensionService;
import xin.vyse.cloud.test.extension.service.HelloService;

@ExtensionService(name = "helloService", bizCode = "xin.vyse.cloud.hello")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
