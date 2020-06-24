package xin.vyse.cloud.extension.annotation;


import org.springframework.context.annotation.Import;
import xin.vyse.cloud.extension.registrar.ExtensionScannerRegistrar;

import java.lang.annotation.*;

/**
 * 扩展点开关
 * @author vyse.guaika
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ExtensionScannerRegistrar.class})
public @interface EnableExtension {
    String[] basePackages() default  {};

    Class<?> [] basePackageClasses() default  {};
}
