package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.ImportDTOConfig;

/**
 * 导入DTO配置构建器
 * <p>
 * 用于配置导入DTO类的生成选项
 * 继承自AbstractModelBuilder，提供通用配置方法
 *
 * @author luminion
 * @since 1.0.0
 */
public class ImportDTOBuilder extends AbstractModelBuilder<ImportDTOConfig, ImportDTOBuilder> {

    public ImportDTOBuilder(ImportDTOConfig render) {
        super(render);
    }

}
