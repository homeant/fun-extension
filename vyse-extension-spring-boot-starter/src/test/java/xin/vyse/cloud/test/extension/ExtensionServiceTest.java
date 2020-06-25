package xin.vyse.cloud.test.extension;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import xin.vyse.cloud.extension.ExtensionExecutor;
import xin.vyse.cloud.extension.domain.Context;
import xin.vyse.cloud.test.ApplicationTest;
import xin.vyse.cloud.test.extension.service.HelloService;

@Slf4j
public class ExtensionServiceTest extends ApplicationTest {

    @Autowired
    private HelloService helloService;

    @Autowired
    private ExtensionExecutor extensionExecutor;

    @Test
    public void test() {
        Context context = new Context();
        context.setBizCode("xin.vyse.cloud.hello");
        String result = extensionExecutor.execute(HelloService.class, context, helloService -> helloService.hello("tom"));
        log.info("result:{}",result);
    }

    @Test
    public void test2(){
        log.info("service:{}", helloService);
    }
}
