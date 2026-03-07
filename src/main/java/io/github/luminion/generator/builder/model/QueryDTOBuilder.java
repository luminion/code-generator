package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.QueryDTOConfig;

/**
 * 查询DTO配置构建器
 * <p>
 * 用于配置查询DTO类的生成选项：
 * <ul>
 *   <li>继承实体类</li>
 * </ul>
 * 继承自AbstractModelBuilder，提供通用配置方法
 *
 * @author luminion
 * @since 1.0.0
 */
public class QueryDTOBuilder extends AbstractModelBuilder<QueryDTOConfig, QueryDTOBuilder> {

    public QueryDTOBuilder(QueryDTOConfig render) {
        super(render);
    }

    /**
     * 启用继承实体类（默认关闭）
     *
     * @return this
     */
    public QueryDTOBuilder extendsEntityEnable() {
        this.config.setExtendsEntity(true);
        return this;
    }

}
