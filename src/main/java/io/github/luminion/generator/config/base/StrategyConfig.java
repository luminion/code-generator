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

import io.github.luminion.generator.config.common.IKeyWordsHandler;
import io.github.luminion.generator.config.common.INameConvert;
import io.github.luminion.generator.config.common.TypeRegistry;
import io.github.luminion.generator.config.enums.IdType;
import io.github.luminion.generator.config.po.LikeTable;
import io.github.luminion.generator.config.po.TableField;
import io.github.luminion.generator.config.rules.DateType;
import io.github.luminion.generator.config.rules.ExtraFieldStrategy;
import io.github.luminion.generator.config.fill.ITemplate;
import io.github.luminion.generator.config.rules.NamingStrategy;
import io.github.luminion.generator.config.common.ITypeConvertHandler;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * 策略配置项
 *
 * @author YangHu, tangguo, hubin
 * @author luminion
 * @since 1.0.0
 */
@Data
public class StrategyConfig implements ITemplate {

    /**
     * 名称转换
     */
    protected INameConvert nameConvert;
    /**
     * 数据库表映射到实体的命名策略
     */
    protected NamingStrategy entityNaming = NamingStrategy.underline_to_camel;
    /**
     * 数据库表字段映射到实体的命名策略
     */
    protected NamingStrategy columnNaming = NamingStrategy.underline_to_camel;

    /**
     * 类型转换处理
     */
    protected ITypeConvertHandler typeConvertHandler;

    /**
     * 关键字处理器
     */
    protected IKeyWordsHandler keyWordsHandler;
//    /**
//     * 时间类型对应策略(todo 直接设置类型转化)
//     */
//    protected DateType dateType = DateType.TIME_PACK;
    /**
     * 类型转化策略
     */
    protected TypeRegistry typeRegistry = new TypeRegistry(DateType.TIME_PACK);

    /**
     * 指定生成的主键的ID类型
     */
    protected IdType idType;

    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    protected boolean booleanColumnRemoveIsPrefix;

    /**
     * 是否生成实体时，生成字段注解（默认 false）
     */
    protected boolean tableFieldAnnotationEnable;

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
     * 需要包含的表名，允许正则表达式（与exclude二选一配置）<br/>
     * 当{@link #enableSqlFilter}为true时，正则表达式无效.
     */
    protected final Set<String> include = new HashSet<>();

    /**
     * 需要排除的表名，允许正则表达式<br/>
     * 当{@link #enableSqlFilter}为true时，正则表达式无效.
     */
    protected final Set<String> exclude = new HashSet<>();

    /**
     * 包含表名
     *
     * @since 3.3.0
     */
    protected LikeTable likeTable;

    /**
     * 不包含表名
     * <p>
     * 只在{@link SQLQuery}模式下生效.
     * </p>
     *
     * @since 3.3.0
     */
    protected LikeTable notLikeTable;

    /**
     * 额外字段后缀
     */
    protected Map<String, String> extraFieldSuffixMap = new LinkedHashMap<>();

    /**
     * 额外字段策略
     */
    protected BiFunction<String, TableField, Boolean> extraFieldStrategy = new ExtraFieldStrategy();

    /**
     * 启用 schema 默认 false
     */
    protected boolean enableSchema;

    /**
     * 是否大写命名（默认 false）
     */
    protected boolean isCapitalMode;

    /**
     * 是否跳过视图（默认 false）
     */
    protected boolean skipView;

    /**
     * 启用sql过滤，语法不能支持使用sql过滤表的话，可以考虑关闭此开关.
     *
     * @since 3.3.1
     */
    protected boolean enableSqlFilter = true;

    /**
     * 大写命名、字段符合大写字母数字下划线命名
     *
     * @param word 待判断字符串
     */
    public boolean isCapitalModeNaming(String word) {
        return isCapitalMode && StringUtils.isCapitalMode(word);
    }

    /**
     * 表名称匹配过滤表前缀
     *
     * @param tableName 表名称
     * @since 3.3.2
     */
    public boolean startsWithTablePrefix(String tableName) {
        return this.tablePrefix.stream().anyMatch(tableName::startsWith);
    }

    /**
     * 验证配置项
     *
     * @since 3.5.0
     */
    public void validate() {
        boolean isInclude = !this.include.isEmpty();
        boolean isExclude = !this.exclude.isEmpty();
        if (isInclude && isExclude) {
            throw new IllegalArgumentException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        }
        if (this.notLikeTable != null && this.likeTable != null) {
            throw new IllegalArgumentException("<strategy> 标签中 <likeTable> 与 <notLikeTable> 只能配置一项！");
        }
    }

    /**
     * 包含表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     * @since 3.5.0
     */
    public boolean matchIncludeTable(String tableName) {
        return matchTable(tableName, this.include);
    }

    /**
     * 排除表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     * @since 3.5.0
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
     * @since 3.5.0
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

}