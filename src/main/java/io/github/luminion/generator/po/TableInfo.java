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

import io.github.luminion.generator.common.support.DefaultDatabaseQueryMetaDataWrapper;
import io.github.luminion.generator.config.ConfigResolver;
import io.github.luminion.generator.config.base.StrategyConfig;
import io.github.luminion.generator.enums.NameConvertType;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 表信息，关联到当前字段信息
 *
 * @author YangHu, lanjerry
 * @author luminion
 * @since 1.0.0
 */
@Data
public class TableInfo {

    private final ConfigResolver configResolver;

    /**
     * 表名是否转化
     */
    private boolean convert;

    /**
     * 表名称
     */
    private String name;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 公共字段
     */
    private final List<TableField> commonFields = new ArrayList<>();

    /**
     * 表字段
     */
    private final List<TableField> fields = new ArrayList<>();

    /**
     * 额外字段
     */
    private final List<TableSuffixField> extraFields = new ArrayList<>();

    /**
     * 是否有主键
     */
    private boolean havePrimaryKey;

    /**
     * 主键字段
     */
    private TableField primaryKeyField;

//    /**
//     * 索引信息
//     *
//     */
//    @Setter
//    @Getter
//    private List<DefaultDatabaseQueryMetaDataWrapper.Index> indexList;

    private final Map<String, TableField> tableFieldMap = new HashMap<>();

    private String schemaName;

    public TableInfo(ConfigResolver configResolver, DefaultDatabaseQueryMetaDataWrapper.Table table) {
        this.configResolver = configResolver;
        StrategyConfig strategyConfig = configResolver.getConfigCollector().getStrategyConfig();
        String tableName = table.getName();
        this.name = tableName;
        String remarks = table.getRemarks();
        if (remarks != null) {
            this.comment = remarks.replaceAll("[\r\n]", "");
        }
        Set<String> tablePrefix = strategyConfig.getTablePrefix();
        Set<String> tableSuffix = strategyConfig.getTableSuffix();
        String removePrefixAndSuffix = NameConvertType.removePrefixAndSuffix(tableName, tablePrefix, tableSuffix);
        String entityName = strategyConfig.getNameConverter().convertEntityName(removePrefixAndSuffix);
        this.entityName = entityName;
//        if (strategyConfig.startsWithTablePrefix(name) || strategyConfig.isTableFieldAnnotationEnable()) {
//            this.convert = true;
//        } else {
//            this.convert = !entityName.equalsIgnoreCase(name);
//        }
        this.convert = !entityName.equalsIgnoreCase(name);
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
        if (getConfigResolver().getConfigCollector().getStrategyConfig().matchIgnoreColumns(field.getColumnName())) {
            // 忽略字段不在处理
            return;
        }
        tableFieldMap.put(field.getName(), field);
        if (getConfigResolver().getConfigCollector().getStrategyConfig().matchSuperEntityColumns(field.getColumnName())) {
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
    public void addExtraField(TableSuffixField field) {
        this.extraFields.add(field);
    }

    /**
     * 转换filed实体为 xml mapper 中的 base column 字符串信息
     */
    public String getBaseResultColumns() {
        // 用于base column 
        return this.fields.stream()
                .map(TableField::getColumnName)
                .collect(Collectors.joining(", "));
    }

    /**
     * 处理额外字段
     */
    public void processExtraField() {
        Set<String> existPropertyNames = this.getFields().stream()
                .map(e -> e.getPropertyName())
                .collect(Collectors.toSet());
        for (TableField field : this.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            for (Map.Entry<String, String> entry : getConfigResolver().getConfigCollector().getStrategyConfig().getExtraFieldSuffixMap().entrySet()) {
                String suffix = entry.getKey();
                String sqlOperator = entry.getValue();
                if (getConfigResolver().getConfigCollector().getStrategyConfig().getExtraFieldProvider().whetherGenerate(sqlOperator, field)) {
                    String suffixPropertyName = field.getPropertyName() + suffix;
                    if (existPropertyNames.contains(suffixPropertyName)) {
                        continue;
                    }
                    existPropertyNames.add(suffixPropertyName);
                    TableSuffixField extraField = new TableSuffixField();
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

}
