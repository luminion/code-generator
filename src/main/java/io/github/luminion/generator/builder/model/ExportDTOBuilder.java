package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.ExportDTOConfig;

/**
 * 导出DTO配置构建器
 * <p>
 * 用于配置导出DTO类的生成选项
 * 继承自AbstractModelBuilder，提供通用配置方法
 *
 * @author luminion
 * @since 1.0.0
 */
public class ExportDTOBuilder extends AbstractModelBuilder<ExportDTOConfig, ExportDTOBuilder> {

    public ExportDTOBuilder(ExportDTOConfig render) {
        super(render);
    }

}
