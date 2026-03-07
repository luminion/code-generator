package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.ServiceConfig;

/**
 * Service接口配置构建器
 * <p>
 * 用于配置Service接口的生成选项：
 * <ul>
 *   <li>继承父类</li>
 * </ul>
 * 继承自AbstractModelBuilder，提供通用配置方法
 *
 * @author luminion
 * @since 1.0.0
 */
public class ServiceBuilder extends AbstractModelBuilder<ServiceConfig, ServiceBuilder> {

    public ServiceBuilder(ServiceConfig render) {
        super(render);
    }


    /**
     * 自定义继承的Service类全称，带包名
     *
     * @param superClass 父类
     * @return this
     */
    public ServiceBuilder superClass(String superClass) {
        this.config.setSuperClass(superClass);
        return this;
    }
}
