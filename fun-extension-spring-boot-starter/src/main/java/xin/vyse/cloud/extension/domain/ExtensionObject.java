package xin.vyse.cloud.extension.domain;

import lombok.Data;
import xin.vyse.cloud.extension.IExtensionPoint;

import java.io.Serializable;

@Data

public class ExtensionObject implements Serializable {
    private String name;

    private String desc;

    private String bizCode;

    private Class<? extends IExtensionPoint> target;
}
