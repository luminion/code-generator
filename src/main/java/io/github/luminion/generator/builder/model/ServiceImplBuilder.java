package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.ServiceImplConfig;

/**
 * ServiceImpl实现类配置构建器
 * <p>
 * 用于配置ServiceImpl实现类的生成选项：
 * <ul>
 *   <li>继承父类</li>
 * </ul>
 * 继承自AbstractModelBuilder，提供通用配置方法
 *
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
