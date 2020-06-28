package xin.vyse.cloud.extension.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xin.vyse.cloud.extension.ExtensionExecutor;
import xin.vyse.cloud.extension.registrar.ExtensionScannerRegistrar;

/**
 * 扩展类配置
 * @author vyse.guaika
 */
@Configuration
public class ExtensionAutoConfiguration {

    @Bean
    public ExtensionExecutor extensionExecutor() {
        return new ExtensionExecutor();
    }

    @Bean
    public ExtensionScannerRegistrar extensionScannerRegistrar(){
        return new ExtensionScannerRegistrar();
    }
}
