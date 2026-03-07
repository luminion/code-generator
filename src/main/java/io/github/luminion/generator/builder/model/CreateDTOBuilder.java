package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.CreateDTOConfig;

/**
 * 创建DTO配置构建器
 * <p>
 * 用于配置创建DTO类的生成选项
 * 继承自AbstractModelBuilder，提供通用配置方法
 *
 * @author luminion
 * @since 1.0.0
 */
public class CreateDTOBuilder extends AbstractModelBuilder<CreateDTOConfig, CreateDTOBuilder> {

    public CreateDTOBuilder(CreateDTOConfig render) {
        super(render);
    }

}
