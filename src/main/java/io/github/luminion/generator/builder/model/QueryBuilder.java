package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.QueryConfig;

/**
 * 查询功能配置构建器
 * <p>
 * 整合查询参数(QueryDTO)和查询结果(QueryVO)的配置
 *
 * @author luminion
 * @since 1.0.0
 */
public class QueryBuilder {

    private final QueryConfig config;

    /**
     * 查询参数构建器 (对应 QueryDTO)
     */
    private final QueryParamBuilder queryParamBuilder;

    /**
     * 查询结果构建器 (对应 QueryVO)
     */
    private final QueryResultBuilder queryResultBuilder;

    public QueryBuilder(QueryConfig config) {
        this.config = config;
        this.queryParamBuilder = new QueryParamBuilder(config);
        this.queryResultBuilder = new QueryResultBuilder(config);
    }

    /**
     * 配置查询参数 (QueryDTO)
     *
     * @param consumer 查询参数配置
     * @return this
     */
    public QueryBuilder queryParam(java.util.function.Consumer<QueryParamBuilder> consumer) {
        consumer.accept(queryParamBuilder);
        return this;
    }

    /**
     * 配置查询结果 (QueryVO)
     *
     * @param consumer 查询结果配置
     * @return this
     */
    public QueryBuilder queryResult(java.util.function.Consumer<QueryResultBuilder> consumer) {
        consumer.accept(queryResultBuilder);
        return this;
    }

    /**
     * 查询参数构建器
     * 对应 QueryDTO 配置
     */
    public static class QueryParamBuilder {
        private final QueryConfig config;

        public QueryParamBuilder(QueryConfig config) {
            this.config = config;
        }

        /**
         * 继承实体类
         *
         * @return this
         */
        public QueryParamBuilder extendsEntityEnable() {
            config.setQueryParamExtendsEntity(true);
            return this;
        }
    }

    /**
     * 查询结果构建器
     * 对应 QueryVO 配置
     */
    public static class QueryResultBuilder {
        private final QueryConfig config;

        public QueryResultBuilder(QueryConfig config) {
            this.config = config;
        }

        /**
         * 继承实体类
         *
         * @return this
         */
        public QueryResultBuilder extendsEntityEnable() {
            config.setQueryResultExtendsEntity(true);
            return this;
        }
    }
}
