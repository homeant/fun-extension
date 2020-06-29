package xin.vyse.cloud.extension.repository;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.PostConstruct;

/**
 * 扩展点仓库
 *
 * @author vyse.guaika
 */
@Slf4j
@ToString
@Data
public class ExtensionRepository implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;


    @PostConstruct
    public void init(){
        log.info("ExtensionRepository init...");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        log.info("event:{}",contextRefreshedEvent);
    }
}
