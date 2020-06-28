package xin.vyse.cloud.extension.factory;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xin.vyse.cloud.extension.repository.ExtensionRepository;

@Data
public class ExtensionServiceFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private Class<T> type;

    private String name;

    private ApplicationContext applicationContext;

    @Override
    public T getObject() throws Exception {
        ExtensionRepository repository = this.applicationContext.getBean(ExtensionRepository.class);
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }
}
