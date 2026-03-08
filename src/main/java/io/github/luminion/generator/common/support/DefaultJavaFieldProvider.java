package io.github.luminion.generator.common.support;

import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.enums.JavaFieldType;
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
public abstract class DefaultJavaFieldProvider {
    private static final Map<Integer, JavaFieldInfo> typeMap = new HashMap<>();
    static {
        // byte[]
        typeMap.put(Types.BINARY, JavaFieldType.BYTE_ARRAY);
        typeMap.put(Types.BLOB, JavaFieldType.BYTE_ARRAY);
        typeMap.put(Types.LONGVARBINARY, JavaFieldType.BYTE_ARRAY);
        typeMap.put(Types.VARBINARY, JavaFieldType.BYTE_ARRAY);
        //byte
        typeMap.put(Types.TINYINT, JavaFieldType.BYTE);
        //long
        typeMap.put(Types.BIGINT, JavaFieldType.LONG);
        //boolean
        typeMap.put(Types.BIT, JavaFieldType.BOOLEAN);
        typeMap.put(Types.BOOLEAN, JavaFieldType.BOOLEAN);
        //short
        typeMap.put(Types.SMALLINT, JavaFieldType.SHORT);
        //string
        typeMap.put(Types.CHAR, JavaFieldType.STRING);
        typeMap.put(Types.CLOB, JavaFieldType.STRING);
        typeMap.put(Types.VARCHAR, JavaFieldType.STRING);
        typeMap.put(Types.LONGVARCHAR, JavaFieldType.STRING);
        typeMap.put(Types.LONGNVARCHAR, JavaFieldType.STRING);
        typeMap.put(Types.NCHAR, JavaFieldType.STRING);
        typeMap.put(Types.NCLOB, JavaFieldType.STRING);
        typeMap.put(Types.NVARCHAR, JavaFieldType.STRING);
        //date
        typeMap.put(Types.DATE, JavaFieldType.DATE);
        //timestamp
        typeMap.put(Types.TIMESTAMP, JavaFieldType.TIMESTAMP);
        typeMap.put(Types.TIMESTAMP_WITH_TIMEZONE, JavaFieldType.TIMESTAMP);
        //double
        typeMap.put(Types.FLOAT, JavaFieldType.DOUBLE);
        typeMap.put(Types.REAL, JavaFieldType.DOUBLE);
        typeMap.put(Types.DOUBLE, JavaFieldType.DOUBLE);
        //int
        typeMap.put(Types.INTEGER, JavaFieldType.INTEGER);
        //bigDecimal
        typeMap.put(Types.NUMERIC, JavaFieldType.BIG_DECIMAL);
        typeMap.put(Types.DECIMAL, JavaFieldType.BIG_DECIMAL);
        // 类型需要补充完整
    }

    
    public static JavaFieldInfo getJavaFieldType(TableField.MetaInfo metaInfo, DateType dateType) {
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
                return typeMap.getOrDefault(typeCode, JavaFieldType.OBJECT);
        }
    }

    private static JavaFieldInfo getBitType(TableField.MetaInfo metaInfo) {
        if (metaInfo.getLength() > 1) {
            return JavaFieldType.BYTE_ARRAY;
        }
        return JavaFieldType.BOOLEAN;
    }

    private static JavaFieldInfo getNumber(TableField.MetaInfo metaInfo) {
        if (metaInfo.getScale() > 0 || metaInfo.getLength() > 18) {
            return typeMap.get(metaInfo.getJdbcType().TYPE_CODE);
        } else if (metaInfo.getLength() > 9) {
            return JavaFieldType.LONG;
        } else if (metaInfo.getLength() > 4) {
            return JavaFieldType.INTEGER;
        } else {
            return JavaFieldType.SHORT;
        }
    }

    private static JavaFieldInfo getDateType(TableField.MetaInfo metaInfo, DateType dateType) {
        JavaFieldType javaJavaFieldTypeEnum;
        switch (dateType) {
            case SQL_PACK:
                javaJavaFieldTypeEnum = JavaFieldType.DATE_SQL;
                break;
            case TIME_PACK:
                javaJavaFieldTypeEnum = JavaFieldType.LOCAL_DATE;
                break;
            default:
                javaJavaFieldTypeEnum = JavaFieldType.DATE;
        }
        return javaJavaFieldTypeEnum;
    }

    private static JavaFieldInfo getTimeType(TableField.MetaInfo metaInfo, DateType dateType) {
        JavaFieldType javaJavaFieldTypeEnum;
        if (dateType == DateType.TIME_PACK) {
            javaJavaFieldTypeEnum = JavaFieldType.LOCAL_TIME;
        } else {
            javaJavaFieldTypeEnum = JavaFieldType.TIME;
        }
        return javaJavaFieldTypeEnum;
    }

    private static JavaFieldInfo getTimestampType(TableField.MetaInfo metaInfo, DateType dateType) {
        JavaFieldType javaJavaFieldTypeEnum;
        if (dateType == DateType.TIME_PACK) {
            javaJavaFieldTypeEnum = JavaFieldType.LOCAL_DATE_TIME;
        } else if (dateType == DateType.ONLY_DATE) {
            javaJavaFieldTypeEnum = JavaFieldType.DATE;
        } else {
            javaJavaFieldTypeEnum = JavaFieldType.TIMESTAMP;
        }
        return javaJavaFieldTypeEnum;
    }
    
}
