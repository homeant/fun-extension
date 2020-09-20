package xin.tianhui.cloud.test.extension;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import xin.tianhui.cloud.extension.ExtensionExecutor;
import xin.tianhui.cloud.extension.domain.Context;
import xin.tianhui.cloud.extension.repository.ExtensionRepository;
import xin.tianhui.cloud.test.ApplicationTest;
import xin.tianhui.cloud.test.config.ApplicationConfiguration;
import xin.tianhui.cloud.test.extension.service.HelloService;
import xin.tianhui.cloud.test.extension.service.TestBean;

@Slf4j
public class ExtensionTest extends ApplicationTest {


    @Autowired
    private ExtensionExecutor extensionExecutor;


    @Autowired
    private ExtensionRepository repository;

    @Test
    public void test() {
        Context context = Context.create("xin.tianhui.cloud.hello");
        String result = extensionExecutor.submit(HelloService.class, context, helloService -> helloService.hello("tom"));
        log.info("result:{}",result);
        log.info("point:{}",repository.getPointList());
    }
}
