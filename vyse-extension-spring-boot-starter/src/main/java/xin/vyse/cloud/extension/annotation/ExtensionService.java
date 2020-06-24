package xin.vyse.cloud.extension.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author vyse.guaika
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtensionService {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    /**
     * 业务key
     *
     * @return
     */
    String bizCode();

    String desc() default "";
}
