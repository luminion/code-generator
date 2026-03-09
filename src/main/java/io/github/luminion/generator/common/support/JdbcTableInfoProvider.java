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
package io.github.luminion.generator.common.support;

import io.github.luminion.generator.util.FieldTypeConvertUtils;
import io.github.luminion.generator.common.DatabaseKeywordsHandler;
import io.github.luminion.generator.common.FieldTypeConverter;
import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.TableInfoProvider;
import io.github.luminion.generator.config.v2.DataSourceConfig;
import io.github.luminion.generator.enums.NameConvertType;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 元数据查询数据库信息.
 * <p>
 * 测试通过的数据库：H2、Mysql-5.7.37、Mysql-8.0.25、PostgreSQL-11.15、PostgreSQL-14.1、Oracle-11.2.0.1.0、DM8
 * </p>
 * <p>
 * FAQ:
 * 1.Mysql无法读取表注释: 链接增加属性 remarks=true和useInformationSchema=true
 * 2.Oracle无法读取注释: 增加属性remarks=true，也有些驱动版本说是增加remarksReporting=true
 * </p>
 *
 * @author nieqiurong 2022/5/11.
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
public class JdbcTableInfoProvider implements TableInfoProvider {
    protected final DataSourceConfig dataSourceConfig;
    protected final JdbcDatabaseMetaDataWrapper databaseMetaDataWrapper;

    public JdbcTableInfoProvider(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
        this.databaseMetaDataWrapper = new JdbcDatabaseMetaDataWrapper(dataSourceConfig.createConnection(), dataSourceConfig.getSchema());
    }

    @Override
    public List<TableInfo> queryTables() {
        try {
            boolean isInclude = !dataSourceConfig.getInclude().isEmpty();
            boolean isExclude = !dataSourceConfig.getExclude().isEmpty();
            List<TableInfo> tableList = new ArrayList<>();
            List<JdbcDatabaseMetaDataWrapper.Table> tables = this.getTables();
            //需要反向生成或排除的表信息
            List<TableInfo> includeTableList = new ArrayList<>();
            List<TableInfo> excludeTableList = new ArrayList<>();
            tables.forEach(table -> {
                String tableName = table.getName();
                if (StringUtils.isNotBlank(tableName)) {
                    TableInfo tableInfo = new TableInfo();
                    tableInfo.setTableName(tableName);
                    String remarks = table.getRemarks();
                    if (remarks != null) {
                        String replaced = remarks.replaceAll("[\r\n]", "");
                        tableInfo.setComment(replaced);
                    }
                    Set<String> tablePrefix = dataSourceConfig.getTablePrefix();
                    Set<String> tableSuffix = dataSourceConfig.getTableSuffix();
                    String removePrefixAndSuffix = NameConvertType.removePrefixAndSuffix(tableName, tablePrefix, tableSuffix);
                    String entityName = dataSourceConfig.getNamingConverter().convertEntityName(removePrefixAndSuffix);
                    tableInfo.setEntityName(entityName);
                    if (isInclude && dataSourceConfig.matchIncludeTable(tableName)) {
                        includeTableList.add(tableInfo);
                    } else if (isExclude && dataSourceConfig.matchExcludeTable(tableName)) {
                        excludeTableList.add(tableInfo);
                    }
                    tableList.add(tableInfo);
                }
            });
            // 过滤表
            filter(tableList, includeTableList, excludeTableList);
            // 转换表信息
            tableList.forEach(this::convertTableFields);
            return tableList;
        } finally {
            // 数据库操作完成,释放连接对象
            databaseMetaDataWrapper.closeConnection();
        }
    }

    protected List<JdbcDatabaseMetaDataWrapper.Table> getTables() {
        // 是否跳过视图
        boolean skipView = dataSourceConfig.isSkipView();
        // 获取表过滤
        String tableNamePattern = dataSourceConfig.getTableNamePattern();
        return databaseMetaDataWrapper.getTables(tableNamePattern, skipView ? new String[]{"TABLE"} : new String[]{"TABLE", "VIEW"});
    }

    protected void convertTableFields(TableInfo tableInfo) {
        String tableName = tableInfo.getTableName();
        Map<String, JdbcDatabaseMetaDataWrapper.Column> columnsInfoMap = getColumnsInfo(tableName);
        columnsInfoMap.forEach((k, columnInfo) -> {
            TableField tableField = new TableField();
            if (columnInfo.isPrimaryKey()) {
                tableField.setKeyFlag(true);
                tableField.setKeyIdentityFlag(columnInfo.isAutoIncrement());
                tableInfo.setHavePrimaryKey(true);
                tableInfo.setPrimaryKeyField(tableField);
            }
            String columnName = columnInfo.getName();
            tableField.setName(columnName);
            tableField.setColumnName(columnName);
            // 数据库列关键字转化
            DatabaseKeywordsHandler keyWordsHandler = dataSourceConfig.getKeyWordsHandler();
            if (keyWordsHandler != null && keyWordsHandler.isKeyWords(columnName)) {
                tableField.setKeyWords(true);
                columnName = keyWordsHandler.formatColumn(columnName);
                tableField.setColumnName(columnName);
            }
            // 设置字段的元数据信息
            TableField.MetaInfo metaInfo = new TableField.MetaInfo();
            metaInfo.setTableName(tableInfo.getTableName());
            metaInfo.setColumnName(columnInfo.getName());
            metaInfo.setLength(columnInfo.getLength());
            metaInfo.setNullable(columnInfo.isNullable());
            metaInfo.setRemarks(columnInfo.getRemarks());
            metaInfo.setDefaultValue(columnInfo.getDefaultValue());
            metaInfo.setScale(columnInfo.getScale());
            metaInfo.setJdbcType(columnInfo.getJdbcType());
            metaInfo.setTypeName(columnInfo.getTypeName());
            tableField.setMetaInfo(metaInfo);

            JavaFieldInfo javaFieldInfo = FieldTypeConvertUtils.getJavaFieldType(metaInfo, dataSourceConfig.getDateType());
            FieldTypeConverter fieldTypeConverter = dataSourceConfig.getFieldTypeConverter();
            if (fieldTypeConverter != null) {
                javaFieldInfo = fieldTypeConverter.convert(metaInfo);
            }
            tableField.setPropertyClassSimpleName(javaFieldInfo.getClassSimpleName());
            tableField.setPropertyClassCanonicalName(javaFieldInfo.getClassCanonicalName());

            // 注释双引号替换为单引号
            if (columnInfo.getRemarks() != null) {
                String comment = columnInfo.getRemarks().replace("\"", "'");
                tableField.setComment(comment);
            }
            Set<String> fieldPrefix = dataSourceConfig.getFieldPrefix();
            Set<String> fieldSuffix = dataSourceConfig.getFieldSuffix();
            String removePrefixAndSuffix = NameConvertType.removePrefixAndSuffix(columnName, fieldPrefix, fieldSuffix);
            String propertyName = dataSourceConfig.getNamingConverter().convertFieldName(removePrefixAndSuffix);
            tableField.setPropertyName(propertyName);
           
            boolean removeIsPrefix = dataSourceConfig.isBooleanColumnRemoveIsPrefix();
            boolean isBoolean = "boolean".equalsIgnoreCase(tableField.getPropertyType());
            boolean startsWithIs = propertyName.startsWith("is");
            if (removeIsPrefix && isBoolean && startsWithIs) {
                tableField.setConvert(true);
                propertyName = StringUtils.removePrefixAfterPrefixToLower(propertyName, 2);
                tableField.setPropertyName(propertyName);
            }
            if (DefaultNamingConverter.class.equals(dataSourceConfig.getNamingConverter().getClass())) {
                // 下划线转驼峰策略
                boolean b = !propertyName.equalsIgnoreCase(NameConvertType.underlineToCamel(tableField.getColumnName()));
                tableField.setConvert(b);
            } else {
                boolean b = !propertyName.equalsIgnoreCase(tableField.getColumnName());
                tableField.setConvert(b);
            }
            if (tableField.isKeyFlag()) {
                boolean b = !"id".equals(propertyName);
                tableField.setConvert(b);
            }
            if (dataSourceConfig.matchIgnoreColumns(tableField.getColumnName())) {
                return;
            }
            if (dataSourceConfig.matchSuperEntityColumns(tableField.getColumnName())) {
                tableInfo.getCommonFields().add(tableField);
            } else {
                tableInfo.getFields().add(tableField);
            }
        });
    }

    protected Map<String, JdbcDatabaseMetaDataWrapper.Column> getColumnsInfo(String tableName) {
        return databaseMetaDataWrapper.getColumnsInfo(tableName, true);
    }
    

    protected void filter(List<TableInfo> tableList, List<TableInfo> includeTableList, List<TableInfo> excludeTableList) {
        boolean isInclude = !dataSourceConfig.getInclude().isEmpty();
        boolean isExclude = !dataSourceConfig.getExclude().isEmpty();
        if (isExclude || isInclude) {
            Pattern pattern = Pattern.compile("[~!/@#$%^&*()+\\\\\\[\\]|{};:'\",<.>?]+");
            Map<String, String> notExistTables = new HashSet<>(isExclude ? dataSourceConfig.getExclude() : dataSourceConfig.getInclude())
                    .stream()
                    .filter(s -> !pattern.matcher(s).find())
                    .collect(Collectors.toMap(String::toLowerCase, s -> s, (o, n) -> n));
            // 将已经存在的表移除，获取配置中数据库不存在的表
            for (TableInfo tabInfo : tableList) {
                if (notExistTables.isEmpty()) {
                    break;
                }
                //解决可能大小写不敏感的情况导致无法移除掉
                notExistTables.remove(tabInfo.getTableName().toLowerCase());
            }
            if (!notExistTables.isEmpty()) {
                log.warn("表[{}]在数据库中不存在！！！", String.join(",", notExistTables.values()));
            }
            // 需要反向生成的表信息
            if (isExclude) {
                tableList.removeAll(excludeTableList);
            } else {
                tableList.clear();
                tableList.addAll(includeTableList);
            }
        }
    }

}
