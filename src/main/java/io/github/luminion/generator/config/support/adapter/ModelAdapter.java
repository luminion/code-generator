package io.github.luminion.generator.config.support.adapter;

import io.github.luminion.generator.config.support.ModelConfig;

import java.util.Arrays;

/**
 * @author luminion
 * @since 1.0.0
 */
public class ModelAdapter {
    private final ModelConfig modelConfig;

    public ModelAdapter(ModelConfig config) {
        this.modelConfig = config;
    }

    /**
     * 查询dto继承实体类
     *
     * @return this
     */
    public ModelAdapter queryDTOExtendsEntity() {
        this.modelConfig.setQueryDTOExtendsEntity(true);
        return this;
    }

    /**
     * 查询vo继承实体类
     *
     * @return this
     */
    public ModelAdapter queryVOExtendsEntity() {
        this.modelConfig.setQueryVOExtendsEntity(true);
        return this;
    }

    /**
     * 添加编辑排除字段
     *
     * @param fields 字段
     */
    public ModelAdapter addEditExcludeFields(String... fields) {
        this.modelConfig.getEditExcludeFields().addAll(Arrays.asList(fields));
        return this;
    }

    /**
     * 添加编辑排除列
     *
     * @param columns 列
     */
    public ModelAdapter addEditExcludeColumns(String... columns) {
        this.modelConfig.getEditExcludeColumns().addAll(Arrays.asList(columns));
        return this;
    }
}
