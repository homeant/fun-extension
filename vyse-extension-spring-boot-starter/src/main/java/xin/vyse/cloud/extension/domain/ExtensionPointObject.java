package xin.vyse.cloud.extension.domain;

import lombok.Data;
import xin.vyse.cloud.extension.IExtensionPoint;

import java.io.Serializable;
import java.util.List;

@Data
public class ExtensionPointObject implements Serializable {
    private String name;

    private String desc;

    private Class<? extends IExtensionPoint> target;

    private List<ExtensionObject> extensionObjectList;
}
