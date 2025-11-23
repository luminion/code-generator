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

import io.github.luminion.generator.common.TableInfoProvider;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.core.DataSourceConfig;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.config.core.StrategyConfig;
import io.github.luminion.generator.util.StringUtils;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
public class DefaultDatabaseQuery implements TableInfoProvider {
    protected final DefaultDatabaseQueryMetaDataWrapper databaseMetaDataWrapper;
    protected final StrategyConfig strategyConfig;
    protected final Configurer<?> configurer;

    public DefaultDatabaseQuery(Configurer<?> configurer) {
        this.configurer = configurer;
        this.strategyConfig = configurer.getStrategyConfig();
        DataSourceConfig dataSourceConfig = configurer.getDataSourceConfig();
        this.databaseMetaDataWrapper = new DefaultDatabaseQueryMetaDataWrapper(dataSourceConfig.createConnection(), dataSourceConfig.getSchemaName());
    }

    @Override
    public List<TableInfo> queryTables() {
        try {
            boolean isInclude = !strategyConfig.getInclude().isEmpty();
            boolean isExclude = !strategyConfig.getExclude().isEmpty();
            //所有的表信息
            List<TableInfo> tableList = new ArrayList<>();
            List<DefaultDatabaseQueryMetaDataWrapper.Table> tables = this.getTables();
            //需要反向生成或排除的表信息
            List<TableInfo> includeTableList = new ArrayList<>();
            List<TableInfo> excludeTableList = new ArrayList<>();
            tables.forEach(table -> {
                String tableName = table.getName();
                if (StringUtils.isNotBlank(tableName)) {
                    TableInfo tableInfo = new TableInfo(configurer, table);
                    if (isInclude && strategyConfig.matchIncludeTable(tableName)) {
                        includeTableList.add(tableInfo);
                    } else if (isExclude && strategyConfig.matchExcludeTable(tableName)) {
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

    protected List<DefaultDatabaseQueryMetaDataWrapper.Table> getTables() {
        // 是否跳过视图
        boolean skipView = strategyConfig.isSkipView();
        // 获取表过滤
        String tableNamePattern = strategyConfig.getTableNamePattern();
        return databaseMetaDataWrapper.getTables(tableNamePattern, skipView ? new String[]{"TABLE"} : new String[]{"TABLE", "VIEW"});
    }

    protected void convertTableFields(TableInfo tableInfo) {
        String tableName = tableInfo.getName();
//        tableInfo.setIndexList(getIndex(tableName));
        Map<String, DefaultDatabaseQueryMetaDataWrapper.Column> columnsInfoMap = getColumnsInfo(tableName);
        columnsInfoMap.forEach((k, columnInfo) -> {
            TableField field = new TableField( tableInfo, columnInfo);
            tableInfo.addField(field);
        });
    }

    protected Map<String, DefaultDatabaseQueryMetaDataWrapper.Column> getColumnsInfo(String tableName) {
        return databaseMetaDataWrapper.getColumnsInfo(tableName, true);
    }

//    protected List<DefaultDatabaseQueryMetaDataWrapper.Index> getIndex(String tableName) {
//        return databaseMetaDataWrapper.getIndex(tableName);
//    }

    protected void filter(List<TableInfo> tableList, List<TableInfo> includeTableList, List<TableInfo> excludeTableList) {
        boolean isInclude = !strategyConfig.getInclude().isEmpty();
        boolean isExclude = !strategyConfig.getExclude().isEmpty();
        if (isExclude || isInclude) {
            Pattern pattern = Pattern.compile("[~!/@#$%^&*()+\\\\\\[\\]|{};:'\",<.>?]+");
            Map<String, String> notExistTables = new HashSet<>(isExclude ? strategyConfig.getExclude() : strategyConfig.getInclude())
                    .stream()
                    .filter(s -> !pattern.matcher(s).find())
                    .collect(Collectors.toMap(String::toLowerCase, s -> s, (o, n) -> n));
            // 将已经存在的表移除，获取配置中数据库不存在的表
            for (TableInfo tabInfo : tableList) {
                if (notExistTables.isEmpty()) {
                    break;
                }
                //解决可能大小写不敏感的情况导致无法移除掉
                notExistTables.remove(tabInfo.getName().toLowerCase());
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
