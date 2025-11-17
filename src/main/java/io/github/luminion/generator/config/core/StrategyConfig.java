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
package io.github.luminion.generator.config.core;

import io.github.luminion.generator.common.*;
import io.github.luminion.generator.common.support.DefaultExtraFieldProvider;
import io.github.luminion.generator.common.support.DefaultNameConverter;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.enums.IdType;
import io.github.luminion.generator.fill.IFill;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.util.ReflectUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.lang.reflect.Field;
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
    protected NameConverter nameConverter = new DefaultNameConverter();
    /**
     * 数据库字段类型转化为java字段类型的方式
     */
    protected JavaFieldProvider JavaFieldProvider;
    /**
     * 数据库关键字处理器
     */
    protected DatabaseKeywordsHandler keyWordsHandler;
    
    // ===================字段类型或特殊字段===================
    
    /**
     * 指定生成的主键的ID类型
     */
    protected IdType idType = IdType.ASSIGN_ID;
    /**
     * java日期类型
     */
    private DateType dateType = DateType.TIME_PACK;

    /**
     * 乐观锁字段名称(数据库字段)
     */
    protected String versionColumnName;
    /**
     * 逻辑删除字段名称(数据库字段)
     */
    protected String logicDeleteColumnName;
    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    protected boolean booleanColumnRemoveIsPrefix;
    /**
     * 自定义基础的Entity类，公共字段
     */
    protected final Set<String> superEntityColumns = new HashSet<>();
    /**
     * 自定义忽略字段
     */
    protected final Set<String> ignoreColumns = new HashSet<>();
    /**
     * 表填充字段
     */
    protected final List<IFill> tableFillList = new ArrayList<>();

    // ===================过滤相关===================

    /**
     * 是否大写命名（默认 false）
     */
    protected boolean isCapitalMode;
    /**
     * 是否跳过视图（默认 false）
     */
    protected boolean skipView;
    /**
     * 模糊查询包含的表名, 需要自行拼接(%)
     */
    protected String tableNamePattern;

    /**
     * 过滤表前缀
     * example: addTablePrefix("t_")
     * result: t_simple -> Simple
     */
    protected final Set<String> tablePrefix = new HashSet<>();

    /**
     * 过滤表后缀
     * example: addTableSuffix("_0")
     * result: t_simple_0 -> Simple
     */
    protected final Set<String> tableSuffix = new HashSet<>();

    /**
     * 过滤字段前缀
     * example: addFieldPrefix("is_")
     * result: is_deleted -> deleted
     */
    protected final Set<String> fieldPrefix = new HashSet<>();

    /**
     * 过滤字段后缀
     * example: addFieldSuffix("_flag")
     * result: deleted_flag -> deleted
     */
    protected final Set<String> fieldSuffix = new HashSet<>();

    /**
     * 需要包含的表名（与exclude二选一配置）
     */
    protected final Set<String> include = new HashSet<>();

    /**
     * 需要排除的表名
     */
    protected final Set<String> exclude = new HashSet<>();
    
    /**
     * 新增和修改需要需要排除的字段
     */
    protected final Set<String> editExcludeColumns = new HashSet<>();
    
    // ===================额外字段===================

    /**
     * 额外字段后缀
     */
    protected Map<String, String> extraFieldSuffixMap = new LinkedHashMap<>();

    /**
     * 额外字段策略
     */
    protected ExtraFieldProvider extraFieldProvider = new DefaultExtraFieldProvider();


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

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        data.put("idType", idType == null ? null : idType.toString());
        data.put("logicDeleteFieldName", this.logicDeleteColumnName);
        data.put("versionFieldName", this.versionColumnName);
        return data;
    }
}