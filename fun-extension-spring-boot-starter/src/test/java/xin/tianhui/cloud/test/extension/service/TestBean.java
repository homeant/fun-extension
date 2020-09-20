package xin.tianhui.cloud.test.extension.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestBean {
    public void test(){
        log.info("Autowired test");
    }
}
