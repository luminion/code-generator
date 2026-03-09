package io.github.luminion.generator.common;

import io.github.luminion.generator.po.TableField;


/**
 * @author luminion
 * @since 1.0.0
 */
@FunctionalInterface
public interface ExtraFieldStrategy {

    /**
     * 控制是否生成额外字段
     *
     * @param sqlOperator sql运算符
     * @param tableField 表字段信息
     * @return 是否生成额外字段
     * @since 1.0.0
     */
    Boolean generateExtraField(String sqlOperator, TableField tableField);
}
