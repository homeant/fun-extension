package xin.tianhui.cloud.extension.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Point implements Serializable {

    private String className;

    private String name;

    private String desc;

    private List<Extension> extensionList;
}
