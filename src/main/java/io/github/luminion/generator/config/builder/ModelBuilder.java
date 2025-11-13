package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.config.Configurer;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class ModelBuilder {
    private final Configurer configurer;

    /**
     * 查询dto继承实体类
     *
     * @return this
     */
    public ModelBuilder queryDTOExtendsEntity() {
        this.configurer.getModelConfig().setQueryDTOExtendsEntity(true);
        return this;
    }

    /**
     * 查询vo继承实体类
     *
     * @return this
     */
    public ModelBuilder queryVOExtendsEntity() {
        this.configurer.getModelConfig().setQueryVOExtendsEntity(true);
        return this;
    }

    /**
     * 添加编辑排除字段
     *
     * @param fields 字段
     */
    public ModelBuilder addEditExcludeFields(String... fields) {
        this.configurer.getModelConfig().getEditExcludeFields().addAll(Arrays.asList(fields));
        return this;
    }

    /**
     * 添加编辑排除列
     *
     * @param columns 列
     */
    public ModelBuilder addEditExcludeColumns(String... columns) {
        this.configurer.getModelConfig().getEditExcludeColumns().addAll(Arrays.asList(columns));
        return this;
    }
}
