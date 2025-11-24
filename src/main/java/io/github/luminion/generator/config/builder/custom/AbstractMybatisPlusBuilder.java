package io.github.luminion.generator.config.builder.custom;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.custom.MybatisPlusConfig;
import lombok.NonNull;

/**
 * @author luminion
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class AbstractMybatisPlusBuilder<B extends AbstractMybatisPlusBuilder<B>> {
    protected final Configurer<MybatisPlusConfig> configurer;

    public AbstractMybatisPlusBuilder(Configurer<MybatisPlusConfig> configurer) {
        this.configurer = configurer;
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
    public B idType(@NonNull IdType idType) {
        this.configurer.getCustomConfig().setIdType(idType);
        return self();
    }

    /**
     * 乐观锁字段名
     *
     * @param versionColumnName 字段名
     * @return this
     */
    public B versionColumnName(@NonNull String versionColumnName) {
        this.configurer.getCustomConfig().setVersionColumnName(versionColumnName);
        return self();
    }

    /**
     * 逻辑删除字段名
     *
     * @param logicDeleteColumnName 字段名
     * @return this
     */
    public B logicDeleteColumnName(@NonNull String logicDeleteColumnName) {
        this.configurer.getCustomConfig().setLogicDeleteColumnName(logicDeleteColumnName);
        return self();
    }

    /**
     * 开启 ActiveRecord 模式
     *
     * @param enable 是否启用
     * @return this
     */
    public B activeRecord(boolean enable) {
        this.configurer.getCustomConfig().setActiveRecord(enable);
        return self();
    }


    /**
     * 是否生成实体时，生成字段注解
     *
     * @param enable 是否启用
     * @return this
     */
    public B tableFieldAnnotation(boolean enable) {
        this.configurer.getCustomConfig().setTableFieldAnnotation(enable);
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
        this.configurer.getCustomConfig().getTableFillMap().put(columnName, fieldFill);
        return self();
    }


}
