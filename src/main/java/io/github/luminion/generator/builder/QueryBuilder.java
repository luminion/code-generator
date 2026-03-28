package io.github.luminion.generator.builder;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.metadata.MethodReference;
import io.github.luminion.generator.naming.ExtraFieldStrategy;
import io.github.luminion.generator.util.LambdaUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Query配置构建器
 *
 * @author luminion
 * @since 1.0.0
 */
public class QueryBuilder {
    private final Configurer configurer;

    public QueryBuilder(Configurer configurer) {
        this.configurer = configurer;
    }

    public QueryBuilder queryByIdMethodName(String methodName) {
        configurer.getQueryConfig().setQueryByIdMethodName(methodName);
        return this;
    }

    public QueryBuilder queryListMethodName(String methodName) {
        configurer.getQueryConfig().setQueryListMethodName(methodName);
        return this;
    }

    public QueryBuilder queryPageMethodName(String methodName) {
        configurer.getQueryConfig().setQueryPageMethodName(methodName);
        return this;
    }

    public QueryBuilder appendExtraFieldSuffix(String suffix, String sqlOperator) {
        configurer.getQueryConfig().getExtraFieldSuffixMap().put(suffix, sqlOperator);
        return this;
    }

    public QueryBuilder extraFieldSuffixMap(Map<String, String> extraFieldSuffixMap) {
        configurer.getQueryConfig().setExtraFieldSuffixMap(new LinkedHashMap<>(extraFieldSuffixMap));
        return this;
    }

    public QueryBuilder extraFieldStrategy(ExtraFieldStrategy extraFieldStrategy) {
        configurer.getQueryConfig().setExtraFieldStrategy(extraFieldStrategy);
        return this;
    }

    public QueryBuilder pageParamName(String pageParamName) {
        configurer.getQueryConfig().setPageParamName(pageParamName);
        return this;
    }

    public QueryBuilder sizeParamName(String sizeParamName) {
        configurer.getQueryConfig().setSizeParamName(sizeParamName);
        return this;
    }

    public <T> QueryBuilder pageSizeParam(MethodReference<T, Long> pageParamGetter, MethodReference<T, Long> sizeParamGetter) {
        String pageParamName = LambdaUtils.resolveGetterPropertyName(pageParamGetter);
        String sizeParamName = LambdaUtils.resolveGetterPropertyName(sizeParamGetter);
        Class<T> queryBaseClass = LambdaUtils.resolveGetterClass(pageParamGetter);
        configurer.getQueryConfig().setPageParamName(pageParamName);
        configurer.getQueryConfig().setSizeParamName(sizeParamName);
        configurer.getQueryConfig().setQueryParamPageFields(false);
        configurer.getQueryConfig().setQueryParamSuperClass(queryBaseClass.getCanonicalName());
        return this;
    }

    public QueryBuilder disableQueryParamPageFields() {
        configurer.getQueryConfig().setQueryParamPageFields(false);
        return this;
    }

    public QueryBuilder queryParamSuperClass(String fullyQualifiedClassName) {
        configurer.getQueryConfig().setQueryParamSuperClass(fullyQualifiedClassName);
        return this;
    }

    public QueryBuilder queryParamSuperClass(Class<?> superClassType) {
        configurer.getQueryConfig().setQueryParamSuperClass(superClassType.getCanonicalName());
        return this;
    }

    public QueryBuilder queryResultSuperClass(String fullyQualifiedClassName) {
        configurer.getQueryConfig().setQueryResultSuperClass(fullyQualifiedClassName);
        return this;
    }

    public QueryBuilder queryResultSuperClass(Class<?> superClassType) {
        configurer.getQueryConfig().setQueryResultSuperClass(superClassType.getCanonicalName());
        return this;
    }
}