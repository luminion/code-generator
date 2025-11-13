package io.github.luminion.generator.common;


/**
 * 名称转换器
 *
 * @author luminion
 * @since 1.0.0
 */
public interface NameConverter {
    /**
     * 转化为实体类名
     *
     * @param tableName 数据库表名
     * @return 实体类名
     * @since 1.0.0
     */
    String convertEntityName(String tableName);
    
    /**
     * 转换字段名
     *
     * @param columnName 数据库列名
     * @return java字段名
     * @since 1.0.0
     */
    String convertFieldName(String columnName);
    
}
