package xin.vyse.cloud.extension.repository;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import xin.vyse.cloud.extension.domain.ExtensionObject;
import xin.vyse.cloud.extension.domain.ExtensionPointObject;

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
public class ExtensionRepository {

    private Map<ExtensionPointObject, List<ExtensionObject>> repository = new ConcurrentHashMap<>();

    public void setRepository(Map<ExtensionPointObject, List<ExtensionObject>> repository) {
        this.repository = repository;
    }


    public List<ExtensionObject> extensions(ExtensionPointObject extensionPointObject) {
        List<ExtensionObject> extensionObjects = repository.get(extensionPointObject);
        if (extensionObjects != null && !extensionObjects.isEmpty()) {
            return extensionObjects;
        }
        return Collections.emptyList();
    }
}
