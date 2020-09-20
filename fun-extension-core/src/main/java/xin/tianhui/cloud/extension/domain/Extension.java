package xin.tianhui.cloud.extension.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Extension implements Serializable {
    private String className;

    private String name;

    private String[] bizCode;

    private String desc;
}
