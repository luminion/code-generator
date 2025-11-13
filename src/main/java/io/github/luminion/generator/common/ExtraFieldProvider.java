package io.github.luminion.generator.common;

import io.github.luminion.generator.po.TableField;

import java.util.function.BiFunction;

/**
 * @author luminion
 * @since 1.0.0
 */
@FunctionalInterface
public interface ExtraFieldProvider extends BiFunction<String, TableField, Boolean> {

    /**
     * 控制是否生成额外字段
     *
     * @param sqlOperator sql运算符
     * @param tableField 表字段信息
     * @return 是否生成额外字段
     * @since 1.0.0
     */
    @Override
    Boolean apply(String sqlOperator, TableField tableField);
}
