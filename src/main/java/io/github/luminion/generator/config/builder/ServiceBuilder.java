package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.config.Configurer;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class ServiceBuilder {
    private final Configurer configurer;

    /**
     * Service接口父类
     *
     * @param clazz 类
     * @return this
     */
    public ServiceBuilder superServiceClass(Class<?> clazz) {
        return superServiceClass(clazz.getName());
    }

    /**
     * Service接口父类
     *
     * @param superServiceClass 类名
     * @return this
     */
    public ServiceBuilder superServiceClass(String superServiceClass) {
        this.configurer.getServiceConfig().setSuperServiceClass(superServiceClass);
        return this;
    }

    /**
     * Service实现类父类
     *
     * @param clazz 类
     * @return this
     */
    public ServiceBuilder superServiceImplClass(Class<?> clazz) {
        return superServiceImplClass(clazz.getName());
    }

    /**
     * Service实现类父类
     *
     * @param superServiceImplClass 类名
     * @return this
     */
    public ServiceBuilder superServiceImplClass(String superServiceImplClass) {
        this.configurer.getServiceConfig().setSuperServiceImplClass(superServiceImplClass);
        return this;
    }
}
