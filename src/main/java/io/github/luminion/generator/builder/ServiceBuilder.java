package io.github.luminion.generator.builder;

import io.github.luminion.generator.config.Configurer;
import lombok.RequiredArgsConstructor;

/**
 * Service配置构建器
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class ServiceBuilder {
    private final Configurer configurer;

    /**
     * 自定义继承的Service类全称，带包名
     *
     * @return this
     */
    public ServiceBuilder serviceSuperClass(String classCanonicalName) {
        configurer.getServiceConfig().setServiceSuperClass(classCanonicalName);
        return this;
    }

    /**
     * 自定义继承的Service类全称，带包名
     *
     * @return this
     */
    public ServiceBuilder serviceSuperClass(Class<?> clazz) {
        configurer.getServiceConfig().setServiceSuperClass(clazz.getCanonicalName());
        return this;
    }

    /**
     * 自定义继承的Service类全称，带包名
     *
     * @return this
     */
    public ServiceBuilder serviceImplSuperClass(String classCanonicalName) {
        configurer.getServiceConfig().setServiceImplSuperClass(classCanonicalName);
        return this;
    }

    /**
     * 自定义继承的Service类全称，带包名
     *
     * @return this
     */
    public ServiceBuilder serviceImplSuperClass(Class<?> clazz) {
        configurer.getServiceConfig().setServiceImplSuperClass(clazz.getCanonicalName());
        return this;
    }

    /**
     * service分页返回类型，例如IPage
     *
     * @return this
     */
    public ServiceBuilder pageType(String classCanonicalName) {
        configurer.getServiceConfig().setPageType(classCanonicalName);
        return this;
    }

    /**
     * service分页返回类型，例如IPage
     *
     * @return this
     */
    public ServiceBuilder pageType(Class<?> clazz) {
        configurer.getServiceConfig().setPageType(clazz.getCanonicalName());
        return this;
    }

    /**
     * service分页方法页码参数名
     *
     * @return this
     */
    public ServiceBuilder pageParamName(String pageParamName) {
        configurer.getServiceConfig().setPageParamName(pageParamName);
        return this;
    }

    /**
     * service分页方法每页条数参数名
     *
     * @return this
     */
    public ServiceBuilder sizeParamName(String sizeParamName) {
        configurer.getServiceConfig().setSizeParamName(sizeParamName);
        return this;
    }

}
