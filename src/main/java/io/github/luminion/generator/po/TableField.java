/*
 * Copyright (c) 2011-2025, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.luminion.generator.po;

import io.github.luminion.generator.common.JavaFieldProvider;
import io.github.luminion.generator.common.support.DefaultJavaFieldProvider;
import io.github.luminion.generator.common.support.DefaultDatabaseQueryMetaDataWrapper;
import io.github.luminion.generator.config.base.StrategyConfig;
import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.DatabaseKeywordsHandler;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.enums.JdbcType;
import io.github.luminion.generator.enums.NameConvertType;
import io.github.luminion.generator.fill.Column;
import io.github.luminion.generator.fill.Property;
import io.github.luminion.generator.util.StringUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 表字段信息
 *
 * @author YangHu
 * @author luminion
 * @since 1.0.0
 */
@ToString
@Slf4j
public class TableField {

    @Getter
    private final StrategyConfig strategyConfig;

  

    /**
     * 数据库字段名称
     */
    @Getter
    private String name;
    /**
     * 是否关键字
     *
     * @since 3.3.2
     */
    @Getter
    private boolean keyWords;
    
    /**
     * 数据库字段（关键字含转义符号）
     */
    @Getter
    private String columnName;
    /**
     * 字段元数据信息
     *
     * @since 3.5.0
     */
    @Getter
    private MetaInfo metaInfo;

    /**
     * java属性名称
     */
    @Getter
    private String propertyName;

    /**
     * java数据库字段类型
     */
    @Getter
    private JavaFieldInfo JavaType;

    /**
     * 是否做注解转换
     */
    @Getter
    private boolean convert;

    /**
     * 是否主键
     */
    @Getter
    private boolean keyFlag;


    /**
     * 主键是否为自增类型
     */
    @Getter
    private boolean keyIdentityFlag;

    /**
     * 字段注释
     */
    @Getter
    private String comment;
    /**
     * 填充
     */
    private String fill;
    /**
     * 自定义查询字段列表
     */
    @Getter
    private Map<String, Object> customMap;



    public TableField(TableInfo tableInfo,
                      DefaultDatabaseQueryMetaDataWrapper.Column columnInfo) {
        this.strategyConfig = tableInfo.getConfigurer().getStrategyConfig();
        if (columnInfo.isPrimaryKey()) {
            this.keyFlag = true;
            this.keyIdentityFlag = columnInfo.isAutoIncrement();
            tableInfo.setHavePrimaryKey(true);
            tableInfo.setPrimaryTableField(this);
            if (this.keyIdentityFlag && strategyConfig.getIdType() != null) {
                log.warn("当前表[{}]的主键为自增主键，会导致全局主键的ID类型设置失效!", tableInfo.getName());
            }
        }
        this.name = columnInfo.getName();
        this.columnName = name;
        DatabaseKeywordsHandler keyWordsHandler = strategyConfig.getKeyWordsHandler();
        if (keyWordsHandler != null && keyWordsHandler.isKeyWords(columnName)) {
            this.keyWords = true;
            this.columnName = keyWordsHandler.formatColumn(columnName);
        }
        // 注释双引号替换为单引号
        if (columnInfo.getRemarks() != null) {
            this.comment = columnInfo.getRemarks().replace("\"", "'");
        }
        Function<String, String> converter = strategyConfig.getTableColumnNameToEntityFieldName();
        Set<String> fieldPrefix = strategyConfig.getFieldPrefix();
        Set<String> fieldSuffix = strategyConfig.getFieldSuffix();
        String propertyName = NameConvertType.doConvertName(columnName, fieldPrefix, fieldSuffix, converter);
        // 设置字段的元数据信息
        TableField.MetaInfo metaInfo = new TableField.MetaInfo(columnInfo, tableInfo);
        JavaFieldInfo columnType;
        JavaFieldProvider javaFieldTypeConverter = strategyConfig.getJavaFieldTypeConverter();
        if (javaFieldTypeConverter != null) {
            columnType = javaFieldTypeConverter.convert(metaInfo);
        } else {
            DateType dateType = strategyConfig.getDateType();
            
            columnType = DefaultJavaFieldProvider.getJavaFieldType(metaInfo,dateType);
        }
        this.JavaType = columnType;
        if (strategyConfig.isBooleanColumnRemoveIsPrefix()
                && "boolean".equalsIgnoreCase(this.getPropertyType())
                && propertyName.startsWith("is")
        ) {
            this.convert = true;
            this.propertyName = StringUtils.removePrefixAfterPrefixToLower(propertyName, 2);
        }
        
        if (NameConvertType.UNDERLINE_TO_CAMEL_CASE.getFunction().equals(strategyConfig.getTableColumnNameToEntityFieldName())) {
            // 下划线转驼峰策略
            this.convert = !propertyName.equalsIgnoreCase(NameConvertType.underlineToCamel(this.columnName));
        } else {
            this.convert = !propertyName.equalsIgnoreCase(this.columnName);
        }
        
        if (strategyConfig.isTableFieldAnnotationEnable()) {
            this.convert = true;
        } else {
            if (this.keyFlag) {
                this.convert = !"id".equals(propertyName);
            }
        }
        this.propertyName = propertyName;
        this.metaInfo = metaInfo;
    }


    public String getPropertyType() {
        if (null != JavaType) {
            return JavaType.getType();
        }
        return null;
    }

    /**
     * 按 JavaBean 规则来生成 get 和 set 方法后面的属性名称
     * 需要处理一下特殊情况：
     * <p>
     * 1、如果只有一位，转换为大写形式
     * 2、如果多于 1 位，只有在第二位是小写的情况下，才会把第一位转为小写
     * <p>
     * 我们并不建议在数据库对应的对象中使用基本类型，因此这里不会考虑基本类型的情况
     */
    public String getCapitalName() {
        if (propertyName.length() == 1) {
            return propertyName.toUpperCase();
        }
        if (Character.isLowerCase(propertyName.charAt(1))) {
            return Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        }
        return propertyName;
    }

    /**
     * 获取注解字段名称
     *
     * @return 字段
     */
    public String getAnnotationColumnName() {
        if (keyWords) {
            if (columnName.startsWith("\"")) {
                return String.format("\\\"%s\\\"", name);
            }
        }
        return columnName;
    }

    /**
     * 是否为乐观锁字段
     *
     * @return 是否为乐观锁字段
     */
    public boolean isVersionField() {
        String propertyName = strategyConfig.getVersionPropertyName();
        String columnName = strategyConfig.getVersionColumnName();
        return StringUtils.isNotBlank(propertyName) && this.propertyName.equals(propertyName)
                || StringUtils.isNotBlank(columnName) && this.name.equalsIgnoreCase(columnName);
    }

    /**
     * 是否为逻辑删除字段
     *
     * @return 是否为逻辑删除字段
     */
    public boolean isLogicDeleteField() {
        String propertyName = strategyConfig.getLogicDeletePropertyName();
        String columnName = strategyConfig.getLogicDeleteColumnName();
        return StringUtils.isNotBlank(propertyName) && this.propertyName.equals(propertyName)
                || StringUtils.isNotBlank(columnName) && this.name.equalsIgnoreCase(columnName);
    }


    public TableField setCustomMap(Map<String, Object> customMap) {
        this.customMap = customMap;
        return this;
    }

    public String getFill() {
        if (StringUtils.isBlank(fill)) {
            strategyConfig.getTableFillList().stream()
                    //忽略大写字段问题
                    .filter(tf -> tf instanceof Column && tf.getName().equalsIgnoreCase(name)
                            || tf instanceof Property && tf.getName().equals(propertyName))
                    .findFirst().ifPresent(tf -> this.fill = tf.getFieldFill().name());
        }
        return fill;
    }

    /**
     * 元数据信息
     *
     * @author nieqiurong 2021/2/8
     * @since 3.5.0
     */
    public static class MetaInfo {

        /**
         * 表名称
         */
        @Getter
        private String tableName;

        /**
         * 字段名称
         */
        @Getter
        private String columnName;

        /**
         * 字段长度
         */
        @Getter
        private int length;

        /**
         * 是否非空
         */
        @Getter
        private boolean nullable;

        /**
         * 字段注释
         */
        @Getter
        private String remarks;

        /**
         * 字段默认值
         */
        @Getter
        private String defaultValue;

        /**
         * 字段精度
         */
        @Getter
        private int scale;

        /**
         * JDBC类型
         */
        @Getter
        private JdbcType jdbcType;

        /**
         * 类型名称(可用做额外判断处理,例如在pg下,json,uuid,jsonb,tsquery这种都认为是OHTER 1111)
         *
         * @since 3.5.3
         */
        @Getter
        private String typeName;

        /**
         * 是否为生成列
         *
         * @since 3.5.8
         */
        private boolean generatedColumn;

        public MetaInfo(DefaultDatabaseQueryMetaDataWrapper.Column column, TableInfo tableInfo) {
            if (column != null) {
                this.tableName = tableInfo.getName();
                this.columnName = column.getName();
                this.length = column.getLength();
                this.nullable = column.isNullable();
                this.remarks = column.getRemarks();
                this.defaultValue = column.getDefaultValue();
                this.scale = column.getScale();
                this.jdbcType = column.getJdbcType();
                this.typeName = column.getTypeName();
                this.generatedColumn = column.isGeneratedColumn();
            }
        }
    }
}
