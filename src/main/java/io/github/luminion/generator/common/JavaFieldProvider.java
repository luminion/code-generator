package io.github.luminion.generator.common;

import io.github.luminion.generator.po.TableField;

/**
 * 表列类型到java类型转换器
 *
 * @author luminion
 * @since 1.0.0
 */
@FunctionalInterface
public interface JavaFieldProvider {

    /**
     * 转为java字段信息
     *
     * @param metaInfo 数据库表字段信息
     * @return 承载java字段信息的实例
     * @since 1.0.0
     */
    JavaFieldInfo convert(TableField.MetaInfo metaInfo);
    
}
