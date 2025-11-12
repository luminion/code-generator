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

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.base.StrategyConfig;
import io.github.luminion.generator.enums.NamingStrategy;
import io.github.luminion.generator.jdbc.DatabaseMetaDataWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 表信息，关联到当前字段信息
 *
 * @author YangHu, lanjerry
 * @author luminion
 * @since 1.0.0
 */
public class TableInfo {


    @Getter
    private final Configurer configurer;
    /**
     * 配置适配器
     */
    @Getter
    private final StrategyConfig strategyConfig;

    /**
     * 是否转换
     */
    @Getter
    @Setter
    private boolean convert;

    /**
     * 表名称
     */
    @Getter
    private String name;

    /**
     * 表注释
     */
    @Getter
    private String comment;

    /**
     * 实体名称
     */
    @Getter
    private String entityName;

    /**
     * 公共字段
     */
    @Getter
    private final List<TableField> commonFields = new ArrayList<>();

    /**
     * 表字段
     */
    @Getter
    private final List<TableField> fields = new ArrayList<>();

    /**
     * 额外字段
     */
    @Getter
    private final List<TableExtraField> extraFields = new ArrayList<>();

    /**
     * 是否有主键
     */
    @Getter
    @Setter
    private boolean havePrimaryKey;
    
    /**
     * 主键字段
     */
    @Getter
    @Setter
    private TableField primaryTableField;

    /**
     * 索引信息
     *
     * @since 3.5.10
     */
    @Setter
    @Getter
    private List<DatabaseMetaDataWrapper.Index> indexList;

    @Getter
    private final Map<String, TableField> tableFieldMap = new HashMap<>();

    @Getter
    private String schemaName;

    public TableInfo(Configurer configurer, DatabaseMetaDataWrapper.Table table) {
        this.configurer = configurer;
        this.strategyConfig = configurer.getStrategyConfig();
        this.name = table.getName();
        String remarks = table.getRemarks();
        if (remarks != null) {
            this.comment = remarks.replaceAll("[\r\n]", "");
        }
        Set<String> tablePrefix = strategyConfig.getTablePrefix();
        Set<String> tableSuffix = strategyConfig.getTableSuffix();
        Function<String, String> tableNameToEntityName = strategyConfig.getTableNameToEntityName();
        String entityName = NamingStrategy.doConvertName(name, tablePrefix, tableSuffix, tableNameToEntityName);
        this.entityName = entityName;
        if (strategyConfig.startsWithTablePrefix(name) || strategyConfig.isTableFieldAnnotationEnable()) {
            this.convert = true;
        } else {
            this.convert = !entityName.equalsIgnoreCase(name);
        }
        this.processExtraField();
    }

    public String getEntityPath() {
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    }

    /**
     * 添加字段
     *
     * @param field 字段
     * @since 3.5.0
     */
    public void addField(TableField field) {
        if (strategyConfig.matchIgnoreColumns(field.getColumnName())) {
            // 忽略字段不在处理
            return;
        }
        tableFieldMap.put(field.getName(), field);
        if (strategyConfig.matchSuperEntityColumns(field.getColumnName())) {
            this.commonFields.add(field);
        } else {
            this.fields.add(field);
        }
    }

    /**
     * 添加额外字段
     *
     * @param field 字段
     */
    public void addExtraField(TableExtraField field) {
        this.extraFields.add(field);
    }

    /**
     * 转换filed实体为 xml mapper 中的 base column 字符串信息
     */
    public String getFieldNames() {
        String fieldNames = this.fields.stream()
                .map(TableField::getColumnName)
                .collect(Collectors.joining(", "));
        // 用于base column 
        return fieldNames;
    }

    /**
     * 处理额外字段
     */
    private void processExtraField() {
        Set<String> existPropertyNames = this.getFields().stream()
                .map(e -> e.getPropertyName())
                .collect(Collectors.toSet());
        for (TableField field : this.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            for (Map.Entry<String, String> entry : strategyConfig.getExtraFieldSuffixMap().entrySet()) {
                String suffix = entry.getKey();
                String sqlOperator = entry.getValue();
                if (strategyConfig.getExtraFieldStrategy().apply(sqlOperator, field)) {
                    String suffixPropertyName = field.getPropertyName() + suffix;
                    if (existPropertyNames.contains(suffixPropertyName)) {
                        continue;
                    }
                    existPropertyNames.add(suffixPropertyName);
                    TableExtraField extraField = new TableExtraField();
                    extraField.setSqlOperator(sqlOperator);
                    extraField.setPropertyType(field.getPropertyType());
                    extraField.setPropertyName(field.getPropertyName() + suffix);
                    extraField.setCapitalName(field.getCapitalName() + suffix);
                    extraField.setColumnName(field.getColumnName());
                    extraField.setComment(field.getComment());
                    extraFields.add(extraField.refactor());
                }
            }
        }
    }

    public TableField getField(String name) {
        return tableFieldMap.get(name);
    }

}
