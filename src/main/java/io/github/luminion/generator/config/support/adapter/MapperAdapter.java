package io.github.luminion.generator.config.support.adapter;

import io.github.luminion.generator.config.support.MapperConfig;

import java.lang.annotation.Annotation;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MapperAdapter {

    private final MapperConfig mapperConfig;

    public MapperAdapter(MapperConfig mapperConfig) {
        this.mapperConfig = mapperConfig;
    }

    /**
     * 父类Mapper
     *
     * @param superClass 类名
     * @return this
     */
    public MapperAdapter superClass(String superClass) {
        this.mapperConfig.setSuperClass(superClass);
        return this;
    }

    /**
     * 父类Mapper
     *
     * @param superClass 类
     * @return this
     * @since 3.5.0
     */
    public MapperAdapter superClass(Class<?> superClass) {
        return superClass(superClass.getName());
    }

    /**
     * 标记 MapperConfigurer 注解
     *
     * @param annotationClass 注解Class
     * @return this
     * @since 3.5.3
     */
    public MapperAdapter mapperAnnotation(Class<? extends Annotation> annotationClass) {
        this.mapperConfig.setMapperAnnotationClass(annotationClass);
        return this;
    }

    /**
     * 开启baseResultMap
     *
     * @return this
     * @since 3.5.0
     */
    public MapperAdapter enableBaseResultMap() {
        this.mapperConfig.setBaseResultMap(true);
        return this;
    }

    /**
     * 开启baseColumnList
     *
     * @return this
     * @since 3.5.0
     */
    public MapperAdapter enableBaseColumnList() {
        this.mapperConfig.setBaseColumnList(true);
        return this;
    }

    /**
     * 设置缓存实现类
     *
     * @param cache 缓存实现
     * @see org.apache.ibatis.cache.Cache 传入类必须是该类的子类
     * @return this
     * @since 3.5.0
     */
    public MapperAdapter cache(Class<?> cache) {
        this.mapperConfig.setCache(cache);
        return this;
    }

    /**
     * 清空排序字段
     *
     * @return this
     */
    public MapperAdapter clearSortColumnMap() {
        this.mapperConfig.getSortColumnMap().clear();
        return this;
    }

    /**
     * 添加排序字段
     *
     * @param columnName 字段名
     * @param isDesc     是否倒排
     * @return this
     */
    public MapperAdapter sortColumn(String columnName, boolean isDesc) {
        this.mapperConfig.getSortColumnMap().put(columnName, isDesc);
        return this;
    }
}
