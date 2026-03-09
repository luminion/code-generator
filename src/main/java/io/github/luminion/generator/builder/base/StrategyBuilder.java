package io.github.luminion.generator.builder.base;

import io.github.luminion.generator.common.DatabaseKeywordsHandler;
import io.github.luminion.generator.common.ExtraFieldStrategy;
import io.github.luminion.generator.common.FieldTypeConverter;
import io.github.luminion.generator.common.NamingConverter;
import io.github.luminion.generator.config.base.StrategyConfig;
import io.github.luminion.generator.enums.DateType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

/**
 * 策略配置构建器
 * <p>
 * 用于配置代码生成的策略选项，包括：
 * <ul>
 *   <li>名称转换策略（表名、字段名转换为实体名、属性名）</li>
 *   <li>数据库类型转换策略</li>
 *   <li>表和字段过滤策略（表前缀、后缀、包含、排除等）</li>
 *   <li>时间类型配置</li>
 *   <li>额外字段配置</li>
 * </ul>
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class StrategyBuilder {
    private final StrategyConfig config;

    /**
     * 数据库表/字段名转实体属性名策略
     *
     * @param namingConverter 名称转换器
     * @return this
     */
    public StrategyBuilder nameConverter(@NonNull NamingConverter namingConverter) {
        this.config.setNamingConverter(namingConverter);
        return this;
    }

    /**
     * 数据库类型转Java类型策略
     *
     * @param fieldTypeConverter Java字段提供者
     * @return this
     */
    public StrategyBuilder javaFieldProvider(@NonNull FieldTypeConverter fieldTypeConverter) {
        this.config.setFieldTypeConverter(fieldTypeConverter);
        return this;
    }

    /**
     * 数据库关键字处理器
     *
     * @param keyWordsHandler 关键字处理器
     * @return this
     */
    public StrategyBuilder keyWordsHandler(@NonNull DatabaseKeywordsHandler keyWordsHandler) {
        this.config.setKeyWordsHandler(keyWordsHandler);
        return this;
    }



    /**
     * 时间类型
     *
     * @param dateType 时间类型
     * @return this
     */
    public StrategyBuilder dateType(@NonNull DateType dateType) {
        this.config.setDateType(dateType);
        return this;
    }

    /**
     * 启用Boolean类型字段移除is前缀（默认关闭）
     * <p>
     * 例如：数据库字段名称 'is_xxx'，类型为 tinyint，
     * 映射实体时会去掉is，最终结果为 xxx
     *
     * @return this
     */
    public StrategyBuilder booleanColumnRemoveIsPrefixEnable() {
        this.config.setBooleanColumnRemoveIsPrefix(true);
        return this;
    }

    /**
     * 添加父类公共字段
     *
     * @param superEntityColumns 父类公共字段
     * @return this
     */
    public StrategyBuilder superEntityColumns(String... superEntityColumns) {
        this.config.getSuperEntityColumns().addAll(Arrays.asList(superEntityColumns));
        return this;
    }

    /**
     * 添加忽略字段
     *
     * @param ignoreColumns 忽略字段
     * @return this
     */
    public StrategyBuilder ignoreColumns(String... ignoreColumns) {
        this.config.getIgnoreColumns().addAll(Arrays.asList(ignoreColumns));
        return this;
    }

    /**
     * 启用显示数据库schema名（默认关闭）
     *
     * @return this
     */
    public StrategyBuilder showSchemaEnable() {
        this.config.setShowSchema(true);
        return this;
    }

    /**
     * 启用跳过视图（默认关闭）
     *
     * @return this
     */
    public StrategyBuilder skipViewEnable() {
        this.config.setSkipView(true);
        return this;
    }

    /**
     * 表名匹配
     *
     * @param tableNamePattern 表名匹配
     * @return this
     */
    public StrategyBuilder tableNamePattern(@NonNull String tableNamePattern) {
        this.config.setTableNamePattern(tableNamePattern);
        return this;
    }

    /**
     * 添加表前缀
     *
     * @param tablePrefix 表前缀
     * @return this
     */
    public StrategyBuilder tablePrefix(String... tablePrefix) {
        this.config.getTablePrefix().addAll(Arrays.asList(tablePrefix));
        return this;
    }

    /**
     * 添加表后缀
     *
     * @param tableSuffix 表后缀
     * @return this
     */
    public StrategyBuilder tableSuffix(String... tableSuffix) {
        this.config.getTableSuffix().addAll(Arrays.asList(tableSuffix));
        return this;
    }

    /**
     * 添加字段前缀
     *
     * @param fieldPrefix 字段前缀
     * @return this
     */
    public StrategyBuilder fieldPrefix(String... fieldPrefix) {
        this.config.getFieldPrefix().addAll(Arrays.asList(fieldPrefix));
        return this;
    }

    /**
     * 添加字段后缀
     *
     * @param fieldSuffix 字段后缀
     * @return this
     */
    public StrategyBuilder fieldSuffix(String... fieldSuffix) {
        this.config.getFieldSuffix().addAll(Arrays.asList(fieldSuffix));
        return this;
    }

    /**
     * 添加包含的表
     *
     * @param include 包含的表
     * @return this
     */
    public StrategyBuilder include(String... include) {
        this.config.getInclude().addAll(Arrays.asList(include));
        return this;
    }

    /**
     * 添加排除的表
     *
     * @param exclude 排除的表
     * @return this
     */
    public StrategyBuilder exclude(String... exclude) {
        this.config.getExclude().addAll(Arrays.asList(exclude));
        return this;
    }

    /**
     * 添加编辑时排除的字段
     *
     * @param editExcludeColumns 编辑时排除的字段
     * @return this
     */
    public StrategyBuilder editExcludeColumn(String... editExcludeColumns) {
        this.config.getEditExcludeColumns().addAll(Arrays.asList(editExcludeColumns));
        return this;
    }

    /**
     * 添加编辑时排除的字段
     *
     * @return this
     */
    public StrategyBuilder editExcludeColumnsClear() {
        this.config.getEditExcludeColumns().clear();
        return this;
    }
    
    

    /**
     * 额外字段后缀
     *
     * @param suffix   后缀
     * @param operator sql运算符
     * @return this
     */
    public StrategyBuilder extraFieldSuffix(@NonNull String suffix, @NonNull String operator) {
        this.config.getExtraFieldSuffixMap().put(suffix, operator);
        return this;
    }

    /**
     * 额外字段后缀
     *
     * @param extraFieldSuffixMap 额外字段后缀Map
     * @return this
     */
    public StrategyBuilder extraFieldSuffix(@NonNull Map<String, String> extraFieldSuffixMap) {
        this.config.getExtraFieldSuffixMap().putAll(extraFieldSuffixMap);
        return this;
    }

    /**
     * 清空额外字段后缀
     *
     * @return this
     */
    public StrategyBuilder clearExtraFieldSuffix() {
        this.config.getExtraFieldSuffixMap().clear();
        return this;
    }

    /**
     * 额外字段提供者
     *
     * @param extraFieldStrategy 额外字段提供者
     * @return this
     */
    public StrategyBuilder extraFieldProvider(@NonNull ExtraFieldStrategy extraFieldStrategy) {
        this.config.setExtraFieldProvider(extraFieldStrategy);
        return this;
    }
}
