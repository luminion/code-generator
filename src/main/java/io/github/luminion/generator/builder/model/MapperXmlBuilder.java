package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.MapperXmlConfig;

/**
 * MapperXml配置构建器
 * <p>
 * 用于配置Mapper XML文件的生成选项：
 * <ul>
 *   <li>BaseResultMap</li>
 *   <li>baseColumnList</li>
 *   <li>缓存实现类</li>
 *   <li>排序字段</li>
 * </ul>
 * 继承自AbstractModelBuilder，提供通用配置方法
 *
 * @author luminion
 * @since 1.0.0
 */
public class MapperXmlBuilder extends AbstractModelBuilder<MapperXmlConfig, MapperXmlBuilder> {


    public MapperXmlBuilder(MapperXmlConfig render) {
        super(render);
    }

    /**
     * 启用BaseResultMap（默认关闭）
     *
     * @return this
     */
    public MapperXmlBuilder baseResultMapEnable() {
        this.config.setBaseResultMap(true);
        return this;
    }

    /**
     * 启用baseColumnList（默认关闭）
     *
     * @return this
     */
    public MapperXmlBuilder baseColumnListEnable() {
        this.config.setBaseColumnList(true);
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
