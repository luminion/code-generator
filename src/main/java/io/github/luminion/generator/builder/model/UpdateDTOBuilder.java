package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.UpdateDTOConfig;

/**
 * 更新DTO配置构建器
 * <p>
 * 用于配置更新DTO类的生成选项
 * 继承自AbstractModelBuilder，提供通用配置方法
 *
 * @author luminion
 * @since 1.0.0
 */
public class UpdateDTOBuilder extends AbstractModelBuilder<UpdateDTOConfig, UpdateDTOBuilder> {

    public UpdateDTOBuilder(UpdateDTOConfig render) {
        super(render);
    }
}
