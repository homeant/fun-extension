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
@Component
public @interface Extension {
    @AliasFor(annotation = Component.class)
    String value() default "";

    String name();

    /**
     * 业务key
     *
     * @return
     */
    String bizCode();

    String desc();
}
