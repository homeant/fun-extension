package xin.tianhui.cloud.extension.repository;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import xin.tianhui.cloud.extension.domain.Point;
import xin.tianhui.cloud.extension.domain.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private static final Map<String, Point> pointList = new ConcurrentHashMap<>();


    public synchronized static void put(String name, Point point) {
        pointList.put(name, point);
    }

    public synchronized static void putService(String name, Service service) {
        if (pointList.containsKey(name)) {
            Point point = pointList.get(name);
            List<Service> serviceList = point.getServiceList();
            if (serviceList == null) {
                point.setServiceList(new ArrayList<>());
                serviceList = point.getServiceList();
            }
            serviceList.add(service);
        }
    }

    public static Map<String, Point> getPointList() {
        return pointList;
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
}
