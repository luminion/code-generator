package io.github.luminion.generator.common.support;


import io.github.luminion.generator.common.ExtraFieldProvider;
import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.enums.SqlKeyword;
import io.github.luminion.generator.po.TableField;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

/**
 * 额外字段配置
 *
 * @author luminion
 * @since 1.0.0
 */
public class DefaultExtraFieldProvider implements ExtraFieldProvider {
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
    public Boolean whetherGenerate(String sqlOperator, TableField tableField) {
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
        boolean isIdColumn = tableField.getColumnName().endsWith("id");


        JavaFieldInfo javaType = tableField.getJavaType();
        boolean isComparable;
        if (javaType.getPkg()==null){
            isComparable = true;
        }else{
            Class<?> clazz = Class.forName(javaType.getPkg());
            isComparable =  Comparable.class.isAssignableFrom(clazz);
        }

        // 大小比较
        if (sqlKeyword.isCompare()) {
            return ALLOW_COMPARE.contains(propertyType) 
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
