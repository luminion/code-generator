package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.QueryDTOConfig;

/**
 * @author luminion
 * @since 1.0.0
 */
public class QueryDTOBuilder extends AbstractModelBuilder<QueryDTOConfig, QueryDTOBuilder> {

    public QueryDTOBuilder(QueryDTOConfig render) {
        super(render);
    }

    /**
     * 是否继承实体类
     *
     * @param enable 是否启用
     * @return this
     */
    public QueryDTOBuilder extendsEntity(boolean enable) {
        this.config.setExtendsEntity(enable);
        return this;
    }

}
