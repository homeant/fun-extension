package xin.tianhui.cloud.extension.annotation;


import java.lang.annotation.*;

/**
 * 扩展点注解
 *
 * @author vyse.guaika
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtensionPoint {

    String value();

    String desc() default "";
}
