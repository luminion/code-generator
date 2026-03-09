package io.github.luminion.generator.common.support;

import io.github.luminion.generator.common.NamingConverter;
import io.github.luminion.generator.enums.NameConvertType;

/**
 * @author luminion
 * @since 1.0.0
 */
public class DefaultNamingConverter implements NamingConverter {
    @Override
    public String convertEntityName(String tableName) {
        return NameConvertType.UNDERLINE_TO_PASCAL_CASE.getFunction().apply(tableName);
    }

    @Override
    public String convertFieldName(String columnName) {
        return NameConvertType.UNDERLINE_TO_CAMEL_CASE.getFunction().apply(columnName);
    }
}
