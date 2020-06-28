package xin.vyse.cloud.test.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import xin.vyse.cloud.extension.annotation.EnableExtension;

@EnableExtension(basePackages = {"xin.vyse.cloud.test.*"})
@EnableAutoConfiguration
public class ApplicationConfiguration {
}
