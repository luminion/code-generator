package io.github.luminion.generator.naming;


import io.github.luminion.generator.enums.SqlKeyword;
import io.github.luminion.generator.metadata.TableField;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

/**
 * 额外字段配置
 *
 * @author luminion
 * @since 1.0.0
 */
public class DefaultExtraFieldStrategy implements ExtraFieldStrategy {
    private static final List<String> ALLOW_COMPARE = Arrays.asList(
            "Byte"
            , "Short"
            , "Integer"
            , "Long"
            , "Float"
            , "Double"
            , "BigInteger"
            , "BigDecimal"
            , "Date"
            , "Time"
            , "Timestamp"
            , "LocalDate"
            , "LocalTime"
            , "LocalDateTime"
    );
    private static final List<String> ALLOW_IN = Arrays.asList(
            "Byte"
            , "Short"
            , "Integer"
            , "Long"
//            , "Float"
//            , "Double"
            , "BigInteger"
            , "BigDecimal"
            , "Date"
//            , "Time"
//            , "Timestamp"
            , "LocalDate"
//            , "LocalTime"
//            , "LocalDateTime"
    );

    @Override
    @SneakyThrows
    public Boolean generateExtraField(String sqlOperator, TableField tableField) {
        SqlKeyword sqlKeyword = SqlKeyword.resolve(sqlOperator);
        String replacedOperator = sqlKeyword.getSymbol();
        String propertyType = tableField.getPropertyType();
        // 主键
        boolean isKeyFlag = tableField.isKeyFlag();
        // 可空
        boolean isNullable = tableField.getMetaInfo().isNullable();
        // 字符串
        boolean isString = "String".equals(propertyType);
        int length = tableField.getMetaInfo().getLength();
        boolean isShortString = isString && length > 0 && length <= 32;
        boolean isIdColumn = tableField.getColumnName().endsWith("id") || tableField.getColumnName().endsWith("by");


        String propertyPkg = tableField.getPropertyPkg();
        boolean isComparable;
        if (propertyPkg == null) {
            isComparable = true;
        } else {
            Class<?> clazz = Class.forName(propertyPkg);
            isComparable = Comparable.class.isAssignableFrom(clazz);
        }

        // 大小比较
        if (sqlKeyword.isCompare()) {
            return ALLOW_COMPARE.contains(propertyType) 
                    && isComparable
                    && !isKeyFlag 
                    && !isIdColumn
                    ;
        }

        // 模糊查询
        if (sqlKeyword.isLike()) {
            return isString
                    && !isIdColumn
                    ;
        }

        // in查询
        if (sqlKeyword.isIn()) {
            return ALLOW_IN.contains(propertyType)  
                    || isKeyFlag 
                    || isIdColumn 
                    || isShortString
                    ;
        }

        // 是否为空
        if (sqlKeyword.isNullCheck()) {
            return isNullable 
                    && !isKeyFlag
                    ;
        }
        
        // 比特位
        if (sqlKeyword.isBitOperation()){
            return "Byte".equals(propertyType) 
                    || "Short".equals(propertyType) 
                    || "Integer".equals(propertyType) 
                    || "Long".equals(propertyType)
                    ;
        }
        return true;
    }

}
