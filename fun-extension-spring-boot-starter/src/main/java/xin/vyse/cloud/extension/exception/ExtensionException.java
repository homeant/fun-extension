package xin.vyse.cloud.extension.exception;

import lombok.Data;

/**
 * 扩展点异常
 *
 * @author vyse.guaika
 */
@Data
public class ExtensionException extends RuntimeException {
    private String bizCode;

    public ExtensionException(String bizCode, String message) {
        super(message);
        this.bizCode = bizCode;
    }

    public ExtensionException(String bizCode, String message, Throwable cause) {
        super(message, cause);
        this.bizCode = bizCode;
    }

    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(Throwable cause) {
        super(cause);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
