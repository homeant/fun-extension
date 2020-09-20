package xin.tianhui.cloud.test.extension.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import xin.tianhui.cloud.extension.annotation.ExtensionService;
import xin.tianhui.cloud.test.extension.service.HelloService;
import xin.tianhui.cloud.test.extension.service.TestBean;

@ExtensionService(name = "helloService", bizCode = "xin.tianhui.cloud.hello")
public class HelloServiceImpl implements HelloService {

    @Autowired
    private TestBean testBean;

    @Override
    public String hello(String name) {
        testBean.test();
        return "hello " + name;
    }
}
