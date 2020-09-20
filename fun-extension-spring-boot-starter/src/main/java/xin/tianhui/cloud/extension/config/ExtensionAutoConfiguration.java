package xin.tianhui.cloud.extension.config;

import org.springframework.context.annotation.*;
import xin.tianhui.cloud.extension.ExtensionExecutor;
import xin.tianhui.cloud.extension.repository.ExtensionRepository;

/**
 * 扩展类配置
 *
 * @author vyse.guaika
 */
@Configuration
public class ExtensionAutoConfiguration {

    @Bean
    public ExtensionRepository extensionRepository() {
        return ExtensionRepository.INSTANCE();
    }

    @Bean
    public ExtensionExecutor extensionExecutor(ExtensionRepository repository) {
        return new ExtensionExecutor(repository);
    }
}
