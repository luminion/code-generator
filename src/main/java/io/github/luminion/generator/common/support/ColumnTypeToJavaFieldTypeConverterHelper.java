package io.github.luminion.generator.common.support;

import io.github.luminion.generator.common.JavaFieldProvider;
import io.github.luminion.generator.enums.FieldType;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.po.TableField;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 列类型到java字段类型转换器帮助程序
 *
 * @author luminion
 * @since 1.0.0
 */
public abstract class ColumnTypeToJavaFieldTypeConverterHelper {
    private static final Map<Integer, JavaFieldProvider> typeMap = new HashMap<>();

    static {
        // byte[]
        typeMap.put(Types.BINARY, FieldType.BYTE_ARRAY);
        typeMap.put(Types.BLOB, FieldType.BYTE_ARRAY);
        typeMap.put(Types.LONGVARBINARY, FieldType.BYTE_ARRAY);
        typeMap.put(Types.VARBINARY, FieldType.BYTE_ARRAY);
        //byte
        typeMap.put(Types.TINYINT, FieldType.BYTE);
        //long
        typeMap.put(Types.BIGINT, FieldType.LONG);
        //boolean
        typeMap.put(Types.BIT, FieldType.BOOLEAN);
        typeMap.put(Types.BOOLEAN, FieldType.BOOLEAN);
        //short
        typeMap.put(Types.SMALLINT, FieldType.SHORT);
        //string
        typeMap.put(Types.CHAR, FieldType.STRING);
        typeMap.put(Types.CLOB, FieldType.STRING);
        typeMap.put(Types.VARCHAR, FieldType.STRING);
        typeMap.put(Types.LONGVARCHAR, FieldType.STRING);
        typeMap.put(Types.LONGNVARCHAR, FieldType.STRING);
        typeMap.put(Types.NCHAR, FieldType.STRING);
        typeMap.put(Types.NCLOB, FieldType.STRING);
        typeMap.put(Types.NVARCHAR, FieldType.STRING);
        //date
        typeMap.put(Types.DATE, FieldType.DATE);
        //timestamp
        typeMap.put(Types.TIMESTAMP, FieldType.TIMESTAMP);
        typeMap.put(Types.TIMESTAMP_WITH_TIMEZONE, FieldType.TIMESTAMP);
        //double
        typeMap.put(Types.FLOAT, FieldType.DOUBLE);
        typeMap.put(Types.REAL, FieldType.DOUBLE);
        typeMap.put(Types.DOUBLE, FieldType.DOUBLE);
        //int
        typeMap.put(Types.INTEGER, FieldType.INTEGER);
        //bigDecimal
        typeMap.put(Types.NUMERIC, FieldType.BIG_DECIMAL);
        typeMap.put(Types.DECIMAL, FieldType.BIG_DECIMAL);
        // 类型需要补充完整
    }

    
    public static JavaFieldProvider getJavaFieldType(TableField.MetaInfo metaInfo, DateType dateType) {
        // 是否用包装类??? 可以尝试判断字段是否允许为null来判断是否用包装类
        int typeCode = metaInfo.getJdbcType().TYPE_CODE;
        switch (typeCode) {
            //  需要增加类型处理，尚未补充完整
            case Types.BIT:
                return getBitType(metaInfo);
            case Types.DECIMAL:
            case Types.NUMERIC:
                return getNumber(metaInfo);
            case Types.DATE:
                return getDateType(metaInfo, dateType);
            case Types.TIME:
                return getTimeType(metaInfo, dateType);
            case Types.TIMESTAMP:
                return getTimestampType(metaInfo, dateType);
            default:
                return typeMap.getOrDefault(typeCode, FieldType.OBJECT);
        }
    }

    private static JavaFieldProvider getBitType(TableField.MetaInfo metaInfo) {
        if (metaInfo.getLength() > 1) {
            return FieldType.BYTE_ARRAY;
        }
        return FieldType.BOOLEAN;
    }

    private static JavaFieldProvider getNumber(TableField.MetaInfo metaInfo) {
        if (metaInfo.getScale() > 0 || metaInfo.getLength() > 18) {
            return typeMap.get(metaInfo.getJdbcType().TYPE_CODE);
        } else if (metaInfo.getLength() > 9) {
            return FieldType.LONG;
        } else if (metaInfo.getLength() > 4) {
            return FieldType.INTEGER;
        } else {
            return FieldType.SHORT;
        }
    }

    private static JavaFieldProvider getDateType(TableField.MetaInfo metaInfo, DateType dateType) {
        FieldType javaFieldTypeEnum;
        switch (dateType) {
            case SQL_PACK:
                javaFieldTypeEnum = FieldType.DATE_SQL;
                break;
            case TIME_PACK:
                javaFieldTypeEnum = FieldType.LOCAL_DATE;
                break;
            default:
                javaFieldTypeEnum = FieldType.DATE;
        }
        return javaFieldTypeEnum;
    }

    private static JavaFieldProvider getTimeType(TableField.MetaInfo metaInfo, DateType dateType) {
        FieldType javaFieldTypeEnum;
        if (dateType == DateType.TIME_PACK) {
            javaFieldTypeEnum = FieldType.LOCAL_TIME;
        } else {
            javaFieldTypeEnum = FieldType.TIME;
        }
        return javaFieldTypeEnum;
    }

    private static JavaFieldProvider getTimestampType(TableField.MetaInfo metaInfo, DateType dateType) {
        FieldType javaFieldTypeEnum;
        if (dateType == DateType.TIME_PACK) {
            javaFieldTypeEnum = FieldType.LOCAL_DATE_TIME;
        } else if (dateType == DateType.ONLY_DATE) {
            javaFieldTypeEnum = FieldType.DATE;
        } else {
            javaFieldTypeEnum = FieldType.TIMESTAMP;
        }
        return javaFieldTypeEnum;
    }
    
}
