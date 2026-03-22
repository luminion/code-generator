package io.github.luminion.generator.builder.v2;

import io.github.luminion.generator.common.ExtraFieldStrategy;
import io.github.luminion.generator.config.Configurer;

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
    
    public QueryBuilder queryByIdMethodName(String methodName){
        configurer.getQueryConfig().setQueryByIdMethodName(methodName);
        return this;
    }
    
    public QueryBuilder queryListMethodName(String methodName){
        configurer.getQueryConfig().setQueryListMethodName(methodName);
        return this;
    }
    
    public QueryBuilder queryPageMethodName(String methodName){
        configurer.getQueryConfig().setQueryPageMethodName(methodName);
        return this;
    }
    
    public QueryBuilder appendExtraFieldSuffix(String suffix, String operator){
        configurer.getQueryConfig().getExtraFieldSuffixMap().put(suffix, operator);
        return this;
    }
    
    public QueryBuilder extraFieldSuffixMap(Map<String, String> extraFieldSuffixMap){
        configurer.getQueryConfig().setExtraFieldSuffixMap(new LinkedHashMap<>(extraFieldSuffixMap));
        return this;
    }
    
    public QueryBuilder extraFieldStrategy(ExtraFieldStrategy extraFieldStrategy){
        configurer.getQueryConfig().setExtraFieldStrategy(extraFieldStrategy);
        return this;
    }
    
    public QueryBuilder pageParamName(String pageParamName){
        configurer.getQueryConfig().setPageParamName(pageParamName);
        return this;
    }
    
    public QueryBuilder sizeParamName(String sizeParamName){
        configurer.getQueryConfig().setSizeParamName(sizeParamName);
        return this;
    }
    
    public QueryBuilder queryParamSuperClass(String classCanonicalName){
        configurer.getQueryConfig().setQueryParamSuperClass(classCanonicalName);
        return this;
    }
    
    public QueryBuilder queryParamSuperClass(Class<?> clazz){
        configurer.getQueryConfig().setQueryParamSuperClass(clazz.getCanonicalName());
        return this;
    }

    public QueryBuilder queryResultSuperClass(String classCanonicalName){
        configurer.getQueryConfig().setQueryResultSuperClass(classCanonicalName);
        return this;
    }

    public QueryBuilder queryResultSuperClass(Class<?> clazz){
        configurer.getQueryConfig().setQueryResultSuperClass(clazz.getCanonicalName());
        return this;
    }
    
    

   
}
