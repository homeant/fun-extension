package xin.vyse.cloud.extension.annotation;

import org.springframework.core.annotation.AliasFor;

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
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    String desc() default "";
}
