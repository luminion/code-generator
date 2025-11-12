package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.config.base.ModelConfig;

import java.util.Arrays;

/**
 * @author luminion
 * @since 1.0.0
 */
public class ModelBuilder {
    private final ModelConfig modelConfig;

    public ModelBuilder(ModelConfig config) {
        this.modelConfig = config;
    }

    /**
     * 查询dto继承实体类
     *
     * @return this
     */
    public ModelBuilder queryDTOExtendsEntity() {
        this.modelConfig.setQueryDTOExtendsEntity(true);
        return this;
    }

    /**
     * 查询vo继承实体类
     *
     * @return this
     */
    public ModelBuilder queryVOExtendsEntity() {
        this.modelConfig.setQueryVOExtendsEntity(true);
        return this;
    }

    /**
     * 添加编辑排除字段
     *
     * @param fields 字段
     */
    public ModelBuilder addEditExcludeFields(String... fields) {
        this.modelConfig.getEditExcludeFields().addAll(Arrays.asList(fields));
        return this;
    }

    /**
     * 添加编辑排除列
     *
     * @param columns 列
     */
    public ModelBuilder addEditExcludeColumns(String... columns) {
        this.modelConfig.getEditExcludeColumns().addAll(Arrays.asList(columns));
        return this;
    }
}
