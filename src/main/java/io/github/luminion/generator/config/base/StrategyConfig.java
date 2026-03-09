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
package io.github.luminion.generator.config.base;

import io.github.luminion.generator.common.*;
import io.github.luminion.generator.common.support.DefaultNamingConverter;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.util.*;

/**
 * 策略配置项
 *
 * @author YangHu, tangguo, hubin
 * @author luminion
 * @since 1.0.0
 */
@Data
public class StrategyConfig implements TemplateRender {
    // ===================字段名转换===================
    /**
     * 数据库表明/字段名转化到实体类名/属性名的转化器
     */
    protected NamingConverter namingConverter = new DefaultNamingConverter();
    /**
     * 数据库字段类型转化为java字段类型的方式
     */
    protected FieldTypeConverter FieldTypeConverter;
    /**
     * 数据库关键字处理器
     */
    protected DatabaseKeywordsHandler keyWordsHandler;

    // ===================字段类型或特殊字段===================



    /**
     * 验证配置项
     */
    public void init() {
        boolean isInclude = !this.include.isEmpty();
        boolean isExclude = !this.exclude.isEmpty();
        if (isInclude && isExclude) {
            throw new IllegalArgumentException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        }
    }

    /**
     * 表名称匹配过滤表前缀
     *
     * @param tableName 表名称
     */
    public boolean startsWithTablePrefix(String tableName) {
        return this.tablePrefix.stream().anyMatch(tableName::startsWith);
    }

    /**
     * 包含表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     */
    public boolean matchIncludeTable(String tableName) {
        return matchTable(tableName, this.include);
    }

    /**
     * 排除表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     */
    public boolean matchExcludeTable(String tableName) {
        return matchTable(tableName, this.exclude);
    }

    /**
     * 表名匹配
     *
     * @param tableName   表名
     * @param matchTables 匹配集合
     * @return 是否匹配
     */
    protected boolean matchTable(String tableName, Set<String> matchTables) {
        return matchTables.stream().anyMatch(t -> tableNameMatches(t, tableName));
    }

    /**
     * 表名匹配
     *
     * @param matchTableName 匹配表名
     * @param dbTableName    数据库表名
     * @return 是否匹配
     */
    protected boolean tableNameMatches(String matchTableName, String dbTableName) {
        return matchTableName.equalsIgnoreCase(dbTableName) || StringUtils.matches(matchTableName, dbTableName);
    }

    /**
     * 匹配父类字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     */
    public boolean matchSuperEntityColumns(String fieldName) {
        // 公共字段判断忽略大小写【 部分数据库大小写不敏感 】
        return superEntityColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    /**
     * 匹配忽略字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     */
    public boolean matchIgnoreColumns(String fieldName) {
        return ignoreColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

  
}