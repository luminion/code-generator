package io.github.luminion.generator.util;

import io.github.luminion.generator.datasource.FieldTypeProvider;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.enums.FieldTypeEnum;
import io.github.luminion.generator.metadata.TableField;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 列类型到java字段类型转换器帮助程序
 *
 * @author luminion
 * @since 1.0.0
 */
public abstract class FieldTypeConvertUtils {
    private static final Map<Integer, FieldTypeProvider> typeMap = new HashMap<>();
    static {
        // byte[]
        typeMap.put(Types.BINARY, FieldTypeEnum.BYTE_ARRAY);
        typeMap.put(Types.BLOB, FieldTypeEnum.BYTE_ARRAY);
        typeMap.put(Types.LONGVARBINARY, FieldTypeEnum.BYTE_ARRAY);
        typeMap.put(Types.VARBINARY, FieldTypeEnum.BYTE_ARRAY);
        //byte
        typeMap.put(Types.TINYINT, FieldTypeEnum.BYTE);
        //long
        typeMap.put(Types.BIGINT, FieldTypeEnum.LONG);
        //boolean
        typeMap.put(Types.BIT, FieldTypeEnum.BOOLEAN);
        typeMap.put(Types.BOOLEAN, FieldTypeEnum.BOOLEAN);
        //short
        typeMap.put(Types.SMALLINT, FieldTypeEnum.SHORT);
        //string
        typeMap.put(Types.CHAR, FieldTypeEnum.STRING);
        typeMap.put(Types.CLOB, FieldTypeEnum.STRING);
        typeMap.put(Types.VARCHAR, FieldTypeEnum.STRING);
        typeMap.put(Types.LONGVARCHAR, FieldTypeEnum.STRING);
        typeMap.put(Types.LONGNVARCHAR, FieldTypeEnum.STRING);
        typeMap.put(Types.NCHAR, FieldTypeEnum.STRING);
        typeMap.put(Types.NCLOB, FieldTypeEnum.STRING);
        typeMap.put(Types.NVARCHAR, FieldTypeEnum.STRING);
        //date
        typeMap.put(Types.DATE, FieldTypeEnum.DATE);
        //timestamp
        typeMap.put(Types.TIMESTAMP, FieldTypeEnum.TIMESTAMP);
        typeMap.put(Types.TIMESTAMP_WITH_TIMEZONE, FieldTypeEnum.TIMESTAMP);
        //double
        typeMap.put(Types.FLOAT, FieldTypeEnum.DOUBLE);
        typeMap.put(Types.REAL, FieldTypeEnum.DOUBLE);
        typeMap.put(Types.DOUBLE, FieldTypeEnum.DOUBLE);
        //int
        typeMap.put(Types.INTEGER, FieldTypeEnum.INTEGER);
        //bigDecimal
        typeMap.put(Types.NUMERIC, FieldTypeEnum.BIG_DECIMAL);
        typeMap.put(Types.DECIMAL, FieldTypeEnum.BIG_DECIMAL);
        // 类型需要补充完整
    }

    
    public static FieldTypeProvider getJavaFieldType(TableField.MetaInfo metaInfo, DateType dateType) {
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
                return typeMap.getOrDefault(typeCode, FieldTypeEnum.OBJECT);
        }
    }

    private static FieldTypeProvider getBitType(TableField.MetaInfo metaInfo) {
        if (metaInfo.getLength() > 1) {
            return FieldTypeEnum.BYTE_ARRAY;
        }
        return FieldTypeEnum.BOOLEAN;
    }

    private static FieldTypeProvider getNumber(TableField.MetaInfo metaInfo) {
        if (metaInfo.getScale() > 0 || metaInfo.getLength() > 18) {
            return typeMap.get(metaInfo.getJdbcType().TYPE_CODE);
        } else if (metaInfo.getLength() > 9) {
            return FieldTypeEnum.LONG;
        } else if (metaInfo.getLength() > 4) {
            return FieldTypeEnum.INTEGER;
        } else {
            return FieldTypeEnum.SHORT;
        }
    }

    private static FieldTypeProvider getDateType(TableField.MetaInfo metaInfo, DateType dateType) {
        FieldTypeEnum javaJavaFieldTypeEnum;
        switch (dateType) {
            case SQL_PACK:
                javaJavaFieldTypeEnum = FieldTypeEnum.DATE_SQL;
                break;
            case TIME_PACK:
                javaJavaFieldTypeEnum = FieldTypeEnum.LOCAL_DATE;
                break;
            default:
                javaJavaFieldTypeEnum = FieldTypeEnum.DATE;
        }
        return javaJavaFieldTypeEnum;
    }

    private static FieldTypeProvider getTimeType(TableField.MetaInfo metaInfo, DateType dateType) {
        FieldTypeEnum fieldTypeEnum;
        if (dateType == DateType.TIME_PACK) {
            fieldTypeEnum = FieldTypeEnum.LOCAL_TIME;
        } else {
            fieldTypeEnum = FieldTypeEnum.TIME;
        }
        return fieldTypeEnum;
    }

    private static FieldTypeProvider getTimestampType(TableField.MetaInfo metaInfo, DateType dateType) {
        FieldTypeEnum javaJavaFieldTypeEnum;
        if (dateType == DateType.TIME_PACK) {
            javaJavaFieldTypeEnum = FieldTypeEnum.LOCAL_DATE_TIME;
        } else if (dateType == DateType.ONLY_DATE) {
            javaJavaFieldTypeEnum = FieldTypeEnum.DATE;
        } else {
            javaJavaFieldTypeEnum = FieldTypeEnum.TIMESTAMP;
        }
        return javaJavaFieldTypeEnum;
    }
    
}
