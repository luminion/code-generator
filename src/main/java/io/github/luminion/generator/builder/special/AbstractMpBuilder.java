package io.github.luminion.generator.builder.special;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.special.MybatisPlusConfig;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class AbstractMpBuilder<B extends AbstractMpBuilder<B>> {
    protected final ConfigCollector<MybatisPlusConfig> configCollector;

    public AbstractMpBuilder(ConfigCollector<MybatisPlusConfig> configCollector) {
        this.configCollector = configCollector;
    }

    @SuppressWarnings("unchecked")
    protected B self() {
        return (B) this;
    }


    /**
     * 全局主键类型
     *
     * @param idType 主键类型
     * @return this
     */
    public B idType(IdType idType) {
        this.configCollector.getCustomConfig().setIdType(idType);
        return self();
    }

    /**
     * 乐观锁字段名
     *
     * @param versionColumnName 字段名
     * @return this
     */
    public B versionColumnName(String versionColumnName) {
        this.configCollector.getCustomConfig().setVersionColumnName(versionColumnName);
        return self();
    }

    /**
     * 逻辑删除字段名
     *
     * @param logicDeleteColumnName 字段名
     * @return this
     */
    public B logicDeleteColumnName(String logicDeleteColumnName) {
        this.configCollector.getCustomConfig().setLogicDeleteColumnName(logicDeleteColumnName);
        return self();
    }

    /**
     * 开启 ActiveRecord 模式
     *
     * @param enable 是否启用
     * @return this
     */
    public B activeRecord(boolean enable) {
        this.configCollector.getCustomConfig().setActiveRecord(enable);
        return self();
    }


    /**
     * 是否生成实体时，生成字段注解
     *
     * @param enable 是否启用
     * @return this
     */
    public B tableFieldAnnotation(boolean enable) {
        this.configCollector.getCustomConfig().setTableFieldAnnotation(enable);
        return self();
    }

    /**
     * 添加表填充字段
     *
     * @param columnName 表字段名
     * @param fieldFill  表填充字段
     * @return this
     */
    public B tableFill(String columnName, FieldFill fieldFill) {
        this.configCollector.getCustomConfig().getTableFillMap().put(columnName, fieldFill);
        return self();
    }


}
