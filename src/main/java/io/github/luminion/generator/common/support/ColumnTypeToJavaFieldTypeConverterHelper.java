package io.github.luminion.generator.common.support;

import io.github.luminion.generator.common.JavaFieldType;
import io.github.luminion.generator.enums.JavaFieldTypeEnum;
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
    private static final Map<Integer, JavaFieldType> typeMap = new HashMap<>();

    static {
        // byte[]
        typeMap.put(Types.BINARY, JavaFieldTypeEnum.BYTE_ARRAY);
        typeMap.put(Types.BLOB, JavaFieldTypeEnum.BYTE_ARRAY);
        typeMap.put(Types.LONGVARBINARY, JavaFieldTypeEnum.BYTE_ARRAY);
        typeMap.put(Types.VARBINARY, JavaFieldTypeEnum.BYTE_ARRAY);
        //byte
        typeMap.put(Types.TINYINT, JavaFieldTypeEnum.BYTE);
        //long
        typeMap.put(Types.BIGINT, JavaFieldTypeEnum.LONG);
        //boolean
        typeMap.put(Types.BIT, JavaFieldTypeEnum.BOOLEAN);
        typeMap.put(Types.BOOLEAN, JavaFieldTypeEnum.BOOLEAN);
        //short
        typeMap.put(Types.SMALLINT, JavaFieldTypeEnum.SHORT);
        //string
        typeMap.put(Types.CHAR, JavaFieldTypeEnum.STRING);
        typeMap.put(Types.CLOB, JavaFieldTypeEnum.STRING);
        typeMap.put(Types.VARCHAR, JavaFieldTypeEnum.STRING);
        typeMap.put(Types.LONGVARCHAR, JavaFieldTypeEnum.STRING);
        typeMap.put(Types.LONGNVARCHAR, JavaFieldTypeEnum.STRING);
        typeMap.put(Types.NCHAR, JavaFieldTypeEnum.STRING);
        typeMap.put(Types.NCLOB, JavaFieldTypeEnum.STRING);
        typeMap.put(Types.NVARCHAR, JavaFieldTypeEnum.STRING);
        //date
        typeMap.put(Types.DATE, JavaFieldTypeEnum.DATE);
        //timestamp
        typeMap.put(Types.TIMESTAMP, JavaFieldTypeEnum.TIMESTAMP);
        typeMap.put(Types.TIMESTAMP_WITH_TIMEZONE, JavaFieldTypeEnum.TIMESTAMP);
        //double
        typeMap.put(Types.FLOAT, JavaFieldTypeEnum.DOUBLE);
        typeMap.put(Types.REAL, JavaFieldTypeEnum.DOUBLE);
        typeMap.put(Types.DOUBLE, JavaFieldTypeEnum.DOUBLE);
        //int
        typeMap.put(Types.INTEGER, JavaFieldTypeEnum.INTEGER);
        //bigDecimal
        typeMap.put(Types.NUMERIC, JavaFieldTypeEnum.BIG_DECIMAL);
        typeMap.put(Types.DECIMAL, JavaFieldTypeEnum.BIG_DECIMAL);
        // 类型需要补充完整
    }

    
    public static JavaFieldType getJavaFieldType(TableField.MetaInfo metaInfo, DateType dateType) {
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
                return typeMap.getOrDefault(typeCode, JavaFieldTypeEnum.OBJECT);
        }
    }

    private static JavaFieldType getBitType(TableField.MetaInfo metaInfo) {
        if (metaInfo.getLength() > 1) {
            return JavaFieldTypeEnum.BYTE_ARRAY;
        }
        return JavaFieldTypeEnum.BOOLEAN;
    }

    private static JavaFieldType getNumber(TableField.MetaInfo metaInfo) {
        if (metaInfo.getScale() > 0 || metaInfo.getLength() > 18) {
            return typeMap.get(metaInfo.getJdbcType().TYPE_CODE);
        } else if (metaInfo.getLength() > 9) {
            return JavaFieldTypeEnum.LONG;
        } else if (metaInfo.getLength() > 4) {
            return JavaFieldTypeEnum.INTEGER;
        } else {
            return JavaFieldTypeEnum.SHORT;
        }
    }

    private static JavaFieldType getDateType(TableField.MetaInfo metaInfo, DateType dateType) {
        JavaFieldTypeEnum javaFieldTypeEnum;
        switch (dateType) {
            case SQL_PACK:
                javaFieldTypeEnum = JavaFieldTypeEnum.DATE_SQL;
                break;
            case TIME_PACK:
                javaFieldTypeEnum = JavaFieldTypeEnum.LOCAL_DATE;
                break;
            default:
                javaFieldTypeEnum = JavaFieldTypeEnum.DATE;
        }
        return javaFieldTypeEnum;
    }

    private static JavaFieldType getTimeType(TableField.MetaInfo metaInfo, DateType dateType) {
        JavaFieldTypeEnum javaFieldTypeEnum;
        if (dateType == DateType.TIME_PACK) {
            javaFieldTypeEnum = JavaFieldTypeEnum.LOCAL_TIME;
        } else {
            javaFieldTypeEnum = JavaFieldTypeEnum.TIME;
        }
        return javaFieldTypeEnum;
    }

    private static JavaFieldType getTimestampType(TableField.MetaInfo metaInfo, DateType dateType) {
        JavaFieldTypeEnum javaFieldTypeEnum;
        if (dateType == DateType.TIME_PACK) {
            javaFieldTypeEnum = JavaFieldTypeEnum.LOCAL_DATE_TIME;
        } else if (dateType == DateType.ONLY_DATE) {
            javaFieldTypeEnum = JavaFieldTypeEnum.DATE;
        } else {
            javaFieldTypeEnum = JavaFieldTypeEnum.TIMESTAMP;
        }
        return javaFieldTypeEnum;
    }
    
}
