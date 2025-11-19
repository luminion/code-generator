package io.github.luminion.generator.config.builder.special;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.IdType;
import io.github.luminion.generator.fill.IFill;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public abstract class AbstractSpecialBuilder<C extends AbstractSpecialBuilder<C>> {
    protected final Configurer configurer;
    @SuppressWarnings("unchecked")
    private C returnThis() {
        return (C) this;
    }
    /**
     * 是否生成实体时，生成字段注解
     *
     * @param enable 是否启用
     * @return this
     */
    protected C entityTableFieldAnnotation(boolean enable) {
        this.configurer.getEntityConfig().setTableFieldAnnotation(enable);
        return returnThis();
    }

    /**
     * 开启 ActiveRecord 模式
     *
     * @param enable 是否启用
     * @return this
     */
    protected C entityActiveRecord(boolean enable) {
        this.configurer.getEntityConfig().setActiveRecord(enable);
        return returnThis();
    }
    
    /**
     * 全局主键类型
     *
     * @param idType 主键类型
     * @return this
     */
    protected C strategyIdType(@NonNull IdType idType) {
        this.configurer.getStrategyConfig().setIdType(idType);
        return returnThis();
    }

    /**
     * 乐观锁字段名
     *
     * @param versionColumnName 字段名
     * @return this
     */
    protected C strategyVersionColumnName(@NonNull String versionColumnName) {
        this.configurer.getStrategyConfig().setVersionColumnName(versionColumnName);
        return returnThis();
    }

    /**
     * 逻辑删除字段名
     *
     * @param logicDeleteColumnName 字段名
     * @return this
     */
    protected C strategyLogicDeleteColumnName(@NonNull String logicDeleteColumnName) {
        this.configurer.getStrategyConfig().setLogicDeleteColumnName(logicDeleteColumnName);
        return returnThis();
    }

    /**
     * 添加表填充字段
     *
     * @param tableFills 表填充字段
     * @return this
     */
    protected C strategyTableFills(IFill... tableFills) {
        this.configurer.getStrategyConfig().getTableFillList().addAll(Arrays.asList(tableFills));
        return returnThis();
    }
}
