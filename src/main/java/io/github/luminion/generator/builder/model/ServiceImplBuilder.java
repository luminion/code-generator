package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.ServiceImplConfig;

/**
 * @author luminion
 * @since 1.0.0
 */
public class ServiceImplBuilder extends AbstractModelBuilder<ServiceImplConfig, ServiceImplBuilder> {

    public ServiceImplBuilder(ServiceImplConfig render) {
        super(render);
    }

    /**
     * 自定义继承的ServiceImpl类全称，带包名
     *
     * @param superClass 父类
     * @return this
     */
    public ServiceImplBuilder superClass(String superClass) {
        this.config.setSuperClass(superClass);
        return this;
    }
}
