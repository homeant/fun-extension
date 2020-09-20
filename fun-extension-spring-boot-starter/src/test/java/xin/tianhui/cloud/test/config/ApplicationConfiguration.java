package xin.tianhui.cloud.test.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import xin.tianhui.cloud.extension.annotation.EnableExtension;

@EnableExtension(basePackages = {"xin.tianhui.cloud.test.*"})
@SpringBootApplication(scanBasePackages = "xin.tianhui.cloud.test")
public class ApplicationConfiguration {
}
