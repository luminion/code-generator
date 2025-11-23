package io.github.luminion.generator.po;

import lombok.Data;

/**
 * 表后缀字段
 * @author luminion
 * @since 1.0.0
 */
@Data
public class TableSuffixField {
    /**
     * 属性类型
     */
    private String propertyType;
    /**
     * 属性名称
     */
    private String propertyName;
    /**
     * GET和SET方法的前缀
     */
    private String capitalName;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 注释
     */
    private String comment;
    /**
     * 后缀
     */
    private String suffix;
    /**
     * sql运算符
     */
    private String sqlOperator;
    /**
     * xml转义或特殊处理后的sql运算符
     */
    private String sqlOperatorXml;

    /**
     * 重构
     *
     * @return {@link TableSuffixField }
     */
    public TableSuffixField refactor() {
        switch (sqlOperator.toUpperCase()) {
            case "LIKE":
                 this.comment += "(包含字符)";
                 this.sqlOperator = "LIKE";
                 this.sqlOperatorXml = "LIKE";
                 break;
            case "NOT LIKE":
                this.comment += "(不包含字符)";
                this.sqlOperator = "NOT LIKE";
                this.sqlOperatorXml = "NOT LIKE";
                break;
            case "IN":
                this.comment += "(包含值)";
                this.sqlOperator = "IN";
                this.sqlOperatorXml = "IN";
                this.propertyType = "List<" + propertyType + ">";
                break;
            case "NOT IN":
                this.comment += "(不包含值)";
                this.sqlOperator = "NOT IN";
                this.sqlOperatorXml = "NOT IN";
                this.propertyType = "List<" + propertyType + ">";
                break;
            case "IS NULL":
                this.comment += "(为空)";
                this.sqlOperator = "IS NULL";
                this.sqlOperatorXml = "IS NULL";
                break;
            case "IS NOT NULL":
                this.comment += "(非空)";
                this.sqlOperator = "IS NOT NULL";
                this.sqlOperatorXml = "IS NOT NULL";
                break;
            case ">":
                this.comment += "(大于)";
                this.sqlOperator = ">";
                this.sqlOperatorXml = "&gt;";
                break;
            case "<":
                this.comment += "(小于)";
                this.sqlOperator = "<";
                this.sqlOperatorXml = "&lt;";
                break;
            case ">=":
                this.comment += "(大于等于)";
                this.sqlOperator = ">=";
                this.sqlOperatorXml = "&gt;=";
                break;
            case "<=":
                this.comment += "(小于等于)";
                this.sqlOperator = "<=";
                this.sqlOperatorXml = "&lt;=";
                break;
            case "!=":
            case "<>":
                this.comment += "(不等于)";
                this.sqlOperator = "!=";
                this.sqlOperatorXml = "&lt;&gt;";
                break;
            case "&":
                this.comment += "(包含指定bit位)";
                this.sqlOperator = "&";
                this.sqlOperatorXml = "&";
                break;
            case "!&":
                this.comment += "(不包含指定bit位)";
                this.sqlOperator = "!&";
                this.sqlOperatorXml = "!&";
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported suffix field operator:[%s], allow: LIKE,NOT LIKE,IN,NOT IN,IS NULL,IS NOT NULL,>,<,>=,<=,!=,<>,&,!&", sqlOperator));
        }
        return this;
    }

}
