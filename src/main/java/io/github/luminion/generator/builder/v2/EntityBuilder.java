package io.github.luminion.generator.builder.v2;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.ColumnFillStrategy;
import io.github.luminion.generator.enums.IdStrategy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 实体类配置构建器
 * <p>
 * 用于配置实体类的生成选项：
 *
 * @author luminion
 * @since 1.0.0
 */
public class EntityBuilder {
    private final Configurer configurer;

    public EntityBuilder(Configurer configurer) {
        this.configurer = configurer;
    }

    /**
     * 自定义继承的Entity类全称，带包名
     *
     */
    public EntityBuilder superClass(String classCanonicalName) {
        configurer.getEntityConfig().setEntitySuperClass(classCanonicalName);
        return this;
    }

    /**
     * 主键生成策略
     */
    public EntityBuilder idStrategy(IdStrategy idStrategy) {
        configurer.getEntityConfig().setIdType(idStrategy);
        return this;
    }

    /**
     * 开启 ActiveRecord 模式( entity extends Model)
     */
    public EntityBuilder enableActiveRecord() {
        configurer.getEntityConfig().setActiveRecord(true);
        return this;
    }

    /**
     * 启用实体类生成时生成字段注解
     */
    public EntityBuilder enableTableFieldAnnotation() {
        configurer.getEntityConfig().setTableFieldAnnotation(true);
        return this;
    }

    /**
     * 乐观锁字段名称
     */
    public EntityBuilder versionColumnName(String versionColumnName) {
        configurer.getDataSourceConfig().setVersionColumnName(versionColumnName);
        return this;
    }

    /**
     * 逻辑删除字段名称
     */
    public EntityBuilder logicDeleteColumnName(String logicDeleteColumnName) {
        configurer.getDataSourceConfig().setLogicDeleteColumnName(logicDeleteColumnName);
        return this;
    }

    /**
     * 自动填充字段(对应mybatis-plus的FiledFill)
     */
    public EntityBuilder columnFill(String columnName, ColumnFillStrategy fillStrategy) {
        configurer.getDataSourceConfig().getColumnFillMap().put(columnName, fillStrategy);
        return this;
    }

    /**
     * 自动填充字段(对应mybatis-plus的FiledFill)
     */
    public EntityBuilder columnFill(Map<String, ColumnFillStrategy> columnFillMap) {
        configurer.getDataSourceConfig().setColumnFillMap(new LinkedHashMap<>(columnFillMap));
        return this;
    }


}
