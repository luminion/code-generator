package io.github.luminion.generator.common;

import io.github.luminion.generator.po.TableField;

/**
 * 表列类型到java类型转换器
 *
 * @author luminion
 * @since 1.0.0
 */
@FunctionalInterface
public interface TableColumnTypeToJavaTypeConverter {

    JavaFieldType convert(TableField.MetaInfo metaInfo);
    
}
