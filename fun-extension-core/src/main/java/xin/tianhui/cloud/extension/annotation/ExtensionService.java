package xin.tianhui.cloud.extension.annotation;


import java.lang.annotation.*;

/**
 * @author vyse.guaika
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtensionService {
    String value() default "";

    String name();

    /**
     * 业务key
     *
     * @return
     */
    String[] bizCode();

    String desc() default "";
}
