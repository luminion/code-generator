package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.MapperXmlConfig;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MapperXmlBuilder extends AbstractModelBuilder<MapperXmlConfig, MapperXmlBuilder> {


    public MapperXmlBuilder(MapperXmlConfig render) {
        super(render);
    }

    /**
     * 是否开启BaseResultMap
     *
     * @param enable 是否启用
     * @return this
     */
    public MapperXmlBuilder baseResultMap(boolean enable) {
        this.config.setBaseResultMap(enable);
        return this;
    }

    /**
     * 是否开启baseColumnList
     *
     * @param enable 是否启用
     * @return this
     */
    public MapperXmlBuilder baseColumnList(boolean enable) {
        this.config.setBaseColumnList(enable);
        return this;
    }

    /**
     * 设置缓存实现类
     *
     * @param cacheClass 缓存类, 该类需要是org.apache.ibatis.cache.Cache的子类
     * @return this
     */
    public MapperXmlBuilder cacheClass(String cacheClass) {
        this.config.setCacheClass(cacheClass);
        return this;
    }

    /**
     * 添加排序列
     *
     * @param column 排序列名
     * @param desc   是否倒序
     * @return this
     */
    public MapperXmlBuilder sortColumn(String column, boolean desc) {
        this.config.getSortColumnMap().put(column, desc);
        return this;
    }

    /**
     * 清空排序列
     *
     * @return this
     */
    public MapperXmlBuilder sortColumnClear() {
        this.config.getSortColumnMap().clear();
        return this;
    }
}
