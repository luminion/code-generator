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
package io.github.luminion.generator.datasource.support;

import io.github.luminion.generator.config.DataSourceConfig;
import io.github.luminion.generator.datasource.TableInfoProvider;
import io.github.luminion.generator.enums.NameConvertType;
import io.github.luminion.generator.internal.schema.JdbcTableFieldMapper;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    protected final JdbcTableFieldMapper tableFieldMapper;

    public JdbcTableInfoProvider(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
        this.databaseMetaDataWrapper = new JdbcDatabaseMetaDataWrapper(dataSourceConfig.createConnection(), dataSourceConfig.getSchema());
        this.tableFieldMapper = new JdbcTableFieldMapper(dataSourceConfig);
    }

    @Override
    public List<TableInfo> queryTables() {
        try {
            boolean hasIncludeTables = !dataSourceConfig.getIncludeTables().isEmpty();
            boolean hasExcludeTables = !dataSourceConfig.getExcludeTables().isEmpty();
            List<TableInfo> tableList = new ArrayList<>();
            List<JdbcDatabaseMetaDataWrapper.Table> tables = this.getTables();
            List<TableInfo> includeTableList = new ArrayList<>();
            List<TableInfo> excludeTableList = new ArrayList<>();
            tables.forEach(table -> {
                String tableName = table.getName();
                if (StringUtils.isBlank(tableName)) {
                    return;
                }
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                String remarks = table.getRemarks();
                if (remarks != null) {
                    tableInfo.setComment(remarks.replaceAll("[\r\n]", ""));
                }
                String normalizedTableName = NameConvertType.removePrefixAndSuffix(tableName,
                        dataSourceConfig.getTablePrefixes(),
                        dataSourceConfig.getTableSuffixes());
                tableInfo.setEntityName(dataSourceConfig.getNamingConverter().convertEntityName(normalizedTableName));
                if (hasIncludeTables && dataSourceConfig.matchIncludeTable(tableName)) {
                    includeTableList.add(tableInfo);
                } else if (hasExcludeTables && dataSourceConfig.matchExcludeTable(tableName)) {
                    excludeTableList.add(tableInfo);
                }
                tableList.add(tableInfo);
            });
            filter(tableList, includeTableList, excludeTableList);
            tableList.forEach(this::convertTableFields);
            return tableList;
        } finally {
            databaseMetaDataWrapper.closeConnection();
        }
    }

    protected List<JdbcDatabaseMetaDataWrapper.Table> getTables() {
        boolean skipView = dataSourceConfig.isSkipView();
        String tableNamePattern = dataSourceConfig.getTableNamePattern();
        return databaseMetaDataWrapper.getTables(tableNamePattern, skipView ? new String[]{"TABLE"} : new String[]{"TABLE", "VIEW"});
    }

    protected void convertTableFields(TableInfo tableInfo) {
        tableFieldMapper.mapTableFields(tableInfo, getColumnsInfo(tableInfo.getTableName()));
    }

    protected Map<String, JdbcDatabaseMetaDataWrapper.Column> getColumnsInfo(String tableName) {
        return databaseMetaDataWrapper.getColumnsInfo(tableName, true);
    }

    protected void filter(List<TableInfo> tableList, List<TableInfo> includeTableList, List<TableInfo> excludeTableList) {
        boolean hasIncludeTables = !dataSourceConfig.getIncludeTables().isEmpty();
        boolean hasExcludeTables = !dataSourceConfig.getExcludeTables().isEmpty();
        if (hasExcludeTables || hasIncludeTables) {
            Pattern pattern = Pattern.compile("[~!/@#$%^&*()+\\\\\\[\\]|{};:'\",<.>?]+");
            Set<String> configuredTables = hasExcludeTables ? dataSourceConfig.getExcludeTables() : dataSourceConfig.getIncludeTables();
            Map<String, String> notExistTables = new HashSet<>(configuredTables).stream()
                    .filter(tableName -> !pattern.matcher(tableName).find())
                    .collect(Collectors.toMap(String::toLowerCase, tableName -> tableName, (oldValue, newValue) -> newValue));
            for (TableInfo tableInfo : tableList) {
                if (notExistTables.isEmpty()) {
                    break;
                }
                notExistTables.remove(tableInfo.getTableName().toLowerCase());
            }
            if (!notExistTables.isEmpty()) {
                log.warn("表[{}]在数据库中不存在！！！", String.join(",", notExistTables.values()));
            }
            if (hasExcludeTables) {
                tableList.removeAll(excludeTableList);
            } else {
                tableList.clear();
                tableList.addAll(includeTableList);
            }
        }
    }
}