package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.ServiceConfig;

/**
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
