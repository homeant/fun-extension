package xin.vyse.cloud.extension.repository;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.config.ContextNamespaceHandler;
import xin.vyse.cloud.extension.domain.ExtensionObject;
import xin.vyse.cloud.extension.domain.ExtensionPointObject;

import javax.annotation.PostConstruct;
import java.util.Collections;
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
public class ExtensionRepository implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Map<ExtensionPointObject, List<ExtensionObject>> repository = new ConcurrentHashMap<>();

    public void setRepository(Map<ExtensionPointObject, List<ExtensionObject>> repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init(){
        log.info("ExtensionRepository init...");
    }


    public List<ExtensionObject> extensions(ExtensionPointObject extensionPointObject) {
        List<ExtensionObject> extensionObjects = repository.get(extensionPointObject);
        if (extensionObjects != null && !extensionObjects.isEmpty()) {
            return extensionObjects;
        }
        return Collections.emptyList();
    }
}
