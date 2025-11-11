package io.github.luminion.generator.config.support.adapter;

import io.github.luminion.generator.config.support.ServiceConfig;

/**
 * @author luminion
 * @since 1.0.0
 */
public class ServiceAdapter {
    private final ServiceConfig config;

    public ServiceAdapter() {
        this.config = new ServiceConfig();
    }

    /**
     * Service接口父类
     *
     * @param clazz 类
     * @return this
     */
    public ServiceAdapter superServiceClass(Class<?> clazz) {
        return superServiceClass(clazz.getName());
    }

    /**
     * Service接口父类
     *
     * @param superServiceClass 类名
     * @return this
     */
    public ServiceAdapter superServiceClass(String superServiceClass) {
        this.config.setSuperServiceClass(superServiceClass);
        return this;
    }

    /**
     * Service实现类父类
     *
     * @param clazz 类
     * @return this
     */
    public ServiceAdapter superServiceImplClass(Class<?> clazz) {
        return superServiceImplClass(clazz.getName());
    }

    /**
     * Service实现类父类
     *
     * @param superServiceImplClass 类名
     * @return this
     */
    public ServiceAdapter superServiceImplClass(String superServiceImplClass) {
        this.config.setSuperServiceImplClass(superServiceImplClass);
        return this;
    }
}
