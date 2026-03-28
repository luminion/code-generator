package io.github.luminion.generator.builder;

import io.github.luminion.generator.config.Configurer;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mapper配置构建器
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class MapperBuilder {
    private final Configurer configurer;

    /**
     * 自定义继承的Mapper类全称，带包名
     *
     * @param superClass 父类
     * @return this
     */
    public MapperBuilder superClass(String superClass) {
        configurer.getMapperConfig().setMapperSuperClass(superClass);
        return this;
    }

    /**
     * Mapper标记注解
     *
     * @param mapperAnnotationClass 注解类
     * @return this
     */
    public MapperBuilder mapperAnnotationClass(String mapperAnnotationClass) {
        configurer.getMapperConfig().setMapperAnnotationClass(mapperAnnotationClass);
        return this;
    }

    public MapperBuilder enableBaseColumnList() {
        configurer.getMapperConfig().setMapperBaseColumnList(true);
        return this;
    }

    public MapperBuilder enableBaseResultMap() {
        configurer.getMapperConfig().setMapperBaseResultMap(true);
        return this;
    }

    public MapperBuilder mapperCacheClass(String mapperCacheClass) {
        configurer.getMapperConfig().setMapperCacheClass(mapperCacheClass);
        return this;
    }

    public MapperBuilder mapperCacheClass(Class<?> clazz) {
        configurer.getMapperConfig().setMapperCacheClass(clazz.getCanonicalName());
        return this;
    }

    /**
     * @deprecated use {@link #mapperCacheClass(Class)} instead.
     */
    @Deprecated
    public MapperBuilder mapperXmlSubPackage(Class<?> clazz) {
        return mapperCacheClass(clazz);
    }

    public MapperBuilder appendOrderColumn(String column, boolean isDesc) {
        configurer.getMapperConfig().getXmlOrderColumnMap().put(column, isDesc);
        return this;
    }

    public MapperBuilder orderColumnMap(Map<String, Boolean> orderColumMap) {
        configurer.getMapperConfig().setXmlOrderColumnMap(new LinkedHashMap<>(orderColumMap));
        return this;
    }
}
