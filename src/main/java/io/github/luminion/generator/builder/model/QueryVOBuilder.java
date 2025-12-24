package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.QueryVOConfig;

/**
 * @author luminion
 * @since 1.0.0
 */
public class QueryVOBuilder extends AbstractModelBuilder<QueryVOConfig, QueryVOBuilder> {

    public QueryVOBuilder(QueryVOConfig render) {
        super(render);
    }

    /**
     * 是否继承实体类
     *
     * @param enable 是否启用
     * @return this
     */
    public QueryVOBuilder extendsEntity(boolean enable) {
        this.config.setExtendsEntity(enable);
        return this;
    }
}
