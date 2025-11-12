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

import io.github.luminion.generator.common.TableColumnTypeToJavaTypeConverter;
import io.github.luminion.generator.common.DatabaseKeyWordsHandler;
import io.github.luminion.generator.common.ITypeConvertHandler;
import io.github.luminion.generator.enums.IdType;
import io.github.luminion.generator.enums.NameConvertStrategy;
import io.github.luminion.generator.fill.IFill;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.enums.ExtraFieldStrategy;
import io.github.luminion.generator.common.ConfigRender;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.util.ReflectUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 策略配置项
 *
 * @author YangHu, tangguo, hubin
 * @author luminion
 * @since 1.0.0
 */
@Data
public class StrategyConfig implements ConfigRender {
    /**
     * 数据库表名转换实体类名
     */
    protected Function<String, String> tableNameToEntityName = NameConvertStrategy.UNDERLINE_TO_PASCAL_CASE.getFunction();
    
    /**
     * 数据库列名称转换为属性名的方法
     */
    protected Function<String, String> tableColumnNameToEntityFieldName = NameConvertStrategy.UNDERLINE_TO_CAMEL_CASE.getFunction();

    /**
     * 日期类型
     */
    private DateType dateType = DateType.TIME_PACK;

    /**
     * 类型转换处理
     */
    protected TableColumnTypeToJavaTypeConverter tableColumnTypeToJavaTypeConverter;

    /**
     * 关键字处理器
     */
    protected DatabaseKeyWordsHandler keyWordsHandler;

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
     * 乐观锁字段名称(数据库字段)
     */
    protected String versionColumnName;
    /**
     * 乐观锁属性名称(实体字段)
     */
    protected String versionPropertyName;
    /**
     * 逻辑删除字段名称(数据库字段)
     */
    protected String logicDeleteColumnName;

    /**
     * 逻辑删除属性名称(实体字段)
     */
    protected String logicDeletePropertyName;
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
     * 模糊查询包含的表名, 需要自行拼接(%)
     *
     */
    protected String tableNamePattern;


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
     */
    public void validate() {
        boolean isInclude = !this.include.isEmpty();
        boolean isExclude = !this.exclude.isEmpty();
        if (isInclude && isExclude) {
            throw new IllegalArgumentException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
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

    /**
     * <p>
     * 父类 Class 反射属性转换为公共字段
     * </p>
     *
     * @param clazz 实体父类 Class
     */
    public void convertSuperEntityColumns(Class<?> clazz) {
        Map<String, Field> fieldMap = ReflectUtils.fieldMap(clazz);
        // todo 待完善 原逻辑
//        List<Field> fields = TableInfoHelper.getAllFields(clazz);
//        this.superEntityColumns.addAll(fieldMap.values().stream().map(field -> {
//            TableId tableId = field.getAnnotation(Class.forName());
//            if (tableId != null && StringUtils.isNotBlank(tableId.value())) {
//                return tableId.value();
//            }
//            TableField tableField = field.getAnnotation(TableField.class);
//            if (tableField != null && StringUtils.isNotBlank(tableField.value())) {
//                return tableField.value();
//            }
//            if (null == columnNaming || columnNaming == NameConvertStrategy.no_change) {
//                return field.getName();
//            }
//            return StringUtils.camelToUnderline(field.getName());
//        }).collect(Collectors.toSet()));
    }


    /**
     * 匹配父类字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     * @since 3.5.0
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
     * @since 3.5.0
     */
    public boolean matchIgnoreColumns(String fieldName) {
        return ignoreColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = ConfigRender.super.renderData(tableInfo);
        data.put("idType", idType == null ? null : idType.toString());
        data.put("logicDeleteFieldName", this.logicDeleteColumnName);
        data.put("versionFieldName", this.versionColumnName);
        return data;
    }
}