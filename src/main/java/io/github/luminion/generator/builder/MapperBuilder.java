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
     * @return this
     */
    public MapperBuilder superClass(String fullyQualifiedClassName) {
        configurer.getMapperConfig().setMapperSuperClass(fullyQualifiedClassName);
        return this;
    }

    /**
     * 自定义继承的Mapper类
     */
    public MapperBuilder superClass(Class<?> superClassType) {
        return superClass(superClassType.getCanonicalName());
    }

    /**
     * Mapper标记注解
     *
     * @return this
     */
    public MapperBuilder mapperAnnotationClass(String fullyQualifiedAnnotationClassName) {
        configurer.getMapperConfig().setMapperAnnotationClass(fullyQualifiedAnnotationClassName);
        return this;
    }

    /**
     * Mapper标记注解
     */
    public MapperBuilder mapperAnnotationClass(Class<?> annotationClass) {
        return mapperAnnotationClass(annotationClass.getCanonicalName());
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

    public MapperBuilder mapperCacheClass(Class<?> mapperCacheType) {
        configurer.getMapperConfig().setMapperCacheClass(mapperCacheType.getCanonicalName());
        return this;
    }

    /**
     * @deprecated use {@link #mapperCacheClass(Class)} instead.
     */
    @Deprecated
    public MapperBuilder mapperXmlSubPackage(Class<?> clazz) {
        return mapperCacheClass(clazz);
    }

    public MapperBuilder appendOrderColumn(String columnName, boolean descending) {
        configurer.getMapperConfig().getXmlOrderColumnMap().put(columnName, descending);
        return this;
    }

    public MapperBuilder orderColumnMap(Map<String, Boolean> orderColumnMap) {
        configurer.getMapperConfig().setXmlOrderColumnMap(new LinkedHashMap<>(orderColumnMap));
        return this;
    }
}