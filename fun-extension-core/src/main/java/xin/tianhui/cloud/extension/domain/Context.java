package xin.tianhui.cloud.extension.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author vyse.guaika
 */
@Data
public class Context implements Serializable {
    private String bizCode;

    private String tenant;

    public static Context create(String bizCode) {
        Context context = new Context();
        context.setBizCode(bizCode);
        return context;
    }
}
