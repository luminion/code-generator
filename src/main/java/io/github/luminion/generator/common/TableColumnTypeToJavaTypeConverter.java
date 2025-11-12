package io.github.luminion.generator.common;

import io.github.luminion.generator.po.TableField;

/**
 * @author luminion
 * @since 1.0.0
 */
@FunctionalInterface
public interface TableColumnTypeToJavaTypeConverter {

    JavaFieldType convert(TableField.MetaInfo metaInfo);
    
}
