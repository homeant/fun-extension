package xin.vyse.cloud.extension.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xin.vyse.cloud.extension.ExtensionExecutor;
import xin.vyse.cloud.extension.repository.ExtensionRepository;

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
    public ExtensionRepository extensionRepository(){
        return new ExtensionRepository();
    }
}
