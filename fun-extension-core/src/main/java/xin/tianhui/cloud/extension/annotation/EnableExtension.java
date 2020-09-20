package xin.tianhui.cloud.extension.annotation;


import org.springframework.context.annotation.Import;
import xin.tianhui.cloud.extension.registrar.ExtensionRegistrar;

import java.lang.annotation.*;

/**
 * 扩展点开关
 * @author vyse.guaika
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ExtensionRegistrar.class})
public @interface EnableExtension {
    String[] basePackages() default  {};

    Class<?> [] basePackageClasses() default  {};
}
