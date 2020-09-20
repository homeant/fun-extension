package xin.tianhui.cloud.extension.repository;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import xin.tianhui.cloud.extension.domain.Point;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扩展点仓库
 *
 * @author vyse.guaika
 */
@Slf4j
@ToString
public class ExtensionRepository implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;

    private Map<String, Point> POINT_LIST = new ConcurrentHashMap<>();

    private static ExtensionRepository INSTANCE = new ExtensionRepository();

    public synchronized static ExtensionRepository INSTANCE() {
        return INSTANCE;
    }

    public Map<String, Point> getPointList() {
        return POINT_LIST;
    }

    @PostConstruct
    public void init() {
        log.info("ExtensionRepository init...");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        log.info("event:{}", contextRefreshedEvent);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
