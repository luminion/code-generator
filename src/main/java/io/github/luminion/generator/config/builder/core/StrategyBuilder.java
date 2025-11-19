package io.github.luminion.generator.config.builder.core;

import io.github.luminion.generator.common.DatabaseKeywordsHandler;
import io.github.luminion.generator.common.ExtraFieldProvider;
import io.github.luminion.generator.common.JavaFieldProvider;
import io.github.luminion.generator.common.NameConverter;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.enums.IdType;
import io.github.luminion.generator.fill.IFill;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

/**
 * 策略构建
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class StrategyBuilder {
    private final Configurer configurer;

    /**
     * 数据库表/字段名转实体属性名策略
     *
     * @param nameConverter 名称转换器
     * @return this
     */
    public StrategyBuilder nameConverter(@NonNull NameConverter nameConverter) {
        this.configurer.getStrategyConfig().setNameConverter(nameConverter);
        return this;
    }

    /**
     * 数据库类型转Java类型策略
     *
     * @param javaFieldProvider Java字段提供者
     * @return this
     */
    public StrategyBuilder javaFieldProvider(@NonNull JavaFieldProvider javaFieldProvider) {
        this.configurer.getStrategyConfig().setJavaFieldProvider(javaFieldProvider);
        return this;
    }

    /**
     * 数据库关键字处理器
     *
     * @param keyWordsHandler 关键字处理器
     * @return this
     */
    public StrategyBuilder keyWordsHandler(@NonNull DatabaseKeywordsHandler keyWordsHandler) {
        this.configurer.getStrategyConfig().setKeyWordsHandler(keyWordsHandler);
        return this;
    }



    /**
     * 时间类型
     *
     * @param dateType 时间类型
     * @return this
     */
    public StrategyBuilder dateType(@NonNull DateType dateType) {
        this.configurer.getStrategyConfig().setDateType(dateType);
        return this;
    }



//    /**
//     * 全局主键类型
//     *
//     * @param idType 主键类型
//     * @return this
//     */
//    public StrategyBuilder idType(@NonNull IdType idType) {
//        this.configurer.getStrategyConfig().setIdType(idType);
//        return this;
//    }
//    /**
//     * 乐观锁字段名
//     *
//     * @param versionColumnName 字段名
//     * @return this
//     */
//    public StrategyBuilder versionColumnName(@NonNull String versionColumnName) {
//        this.configurer.getStrategyConfig().setVersionColumnName(versionColumnName);
//        return this;
//    }
//
//    /**
//     * 逻辑删除字段名
//     *
//     * @param logicDeleteColumnName 字段名
//     * @return this
//     */
//    public StrategyBuilder logicDeleteColumnName(@NonNull String logicDeleteColumnName) {
//        this.configurer.getStrategyConfig().setLogicDeleteColumnName(logicDeleteColumnName);
//        return this;
//    }

    /**
     * Boolean类型字段是否移除is前缀
     *
     * @param enabled 是否移除
     * @return this
     */
    public StrategyBuilder booleanColumnRemoveIsPrefix(boolean enabled) {
        this.configurer.getStrategyConfig().setBooleanColumnRemoveIsPrefix(enabled);
        return this;
    }

    /**
     * 添加父类公共字段
     *
     * @param superEntityColumns 父类公共字段
     * @return this
     */
    public StrategyBuilder superEntityColumns(String... superEntityColumns) {
        this.configurer.getStrategyConfig().getSuperEntityColumns().addAll(Arrays.asList(superEntityColumns));
        return this;
    }

    /**
     * 添加忽略字段
     *
     * @param ignoreColumns 忽略字段
     * @return this
     */
    public StrategyBuilder ignoreColumns(String... ignoreColumns) {
        this.configurer.getStrategyConfig().getIgnoreColumns().addAll(Arrays.asList(ignoreColumns));
        return this;
    }

//    /**
//     * 添加表填充字段
//     *
//     * @param tableFills 表填充字段
//     * @return this
//     */
//    public StrategyBuilder tableFills(IFill... tableFills) {
//        this.configurer.getStrategyConfig().getTableFillList().addAll(Arrays.asList(tableFills));
//        return this;
//    }

    /**
     * 跳过视图
     *
     * @param enabled 是否跳过
     * @return this
     */
    public StrategyBuilder skipView(boolean enabled) {
        this.configurer.getStrategyConfig().setSkipView(enabled);
        return this;
    }

    /**
     * 表名匹配
     *
     * @param tableNamePattern 表名匹配
     * @return this
     */
    public StrategyBuilder tableNamePattern(@NonNull String tableNamePattern) {
        this.configurer.getStrategyConfig().setTableNamePattern(tableNamePattern);
        return this;
    }

    /**
     * 添加表前缀
     *
     * @param tablePrefix 表前缀
     * @return this
     */
    public StrategyBuilder tablePrefix(String... tablePrefix) {
        this.configurer.getStrategyConfig().getTablePrefix().addAll(Arrays.asList(tablePrefix));
        return this;
    }

    /**
     * 添加表后缀
     *
     * @param tableSuffix 表后缀
     * @return this
     */
    public StrategyBuilder tableSuffix(String... tableSuffix) {
        this.configurer.getStrategyConfig().getTableSuffix().addAll(Arrays.asList(tableSuffix));
        return this;
    }

    /**
     * 添加字段前缀
     *
     * @param fieldPrefix 字段前缀
     * @return this
     */
    public StrategyBuilder fieldPrefix(String... fieldPrefix) {
        this.configurer.getStrategyConfig().getFieldPrefix().addAll(Arrays.asList(fieldPrefix));
        return this;
    }

    /**
     * 添加字段后缀
     *
     * @param fieldSuffix 字段后缀
     * @return this
     */
    public StrategyBuilder fieldSuffix(String... fieldSuffix) {
        this.configurer.getStrategyConfig().getFieldSuffix().addAll(Arrays.asList(fieldSuffix));
        return this;
    }

    /**
     * 添加包含的表
     *
     * @param include 包含的表
     * @return this
     */
    public StrategyBuilder include(String... include) {
        this.configurer.getStrategyConfig().getInclude().addAll(Arrays.asList(include));
        return this;
    }

    /**
     * 添加排除的表
     *
     * @param exclude 排除的表
     * @return this
     */
    public StrategyBuilder exclude(String... exclude) {
        this.configurer.getStrategyConfig().getExclude().addAll(Arrays.asList(exclude));
        return this;
    }

    /**
     * 添加编辑时排除的字段
     *
     * @param editExcludeColumns 编辑时排除的字段
     * @return this
     */
    public StrategyBuilder editExcludeColumn(String... editExcludeColumns) {
        this.configurer.getStrategyConfig().getEditExcludeColumns().addAll(Arrays.asList(editExcludeColumns));
        return this;
    }

    /**
     * 添加编辑时排除的字段
     *
     * @param editExcludeColumns 编辑时排除的字段
     * @return this
     */
    public StrategyBuilder editExcludeColumnsClear() {
        this.configurer.getStrategyConfig().getEditExcludeColumns().clear();
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
        this.configurer.getStrategyConfig().getExtraFieldSuffixMap().put(suffix, operator);
        return this;
    }

    /**
     * 额外字段后缀
     *
     * @param extraFieldSuffixMap 额外字段后缀Map
     * @return this
     */
    public StrategyBuilder extraFieldSuffix(@NonNull Map<String, String> extraFieldSuffixMap) {
        this.configurer.getStrategyConfig().getExtraFieldSuffixMap().putAll(extraFieldSuffixMap);
        return this;
    }

    /**
     * 清空额外字段后缀
     *
     * @return this
     */
    public StrategyBuilder clearExtraFieldSuffix() {
        this.configurer.getStrategyConfig().getExtraFieldSuffixMap().clear();
        return this;
    }

    /**
     * 额外字段提供者
     *
     * @param extraFieldProvider 额外字段提供者
     * @return this
     */
    public StrategyBuilder extraFieldProvider(@NonNull ExtraFieldProvider extraFieldProvider) {
        this.configurer.getStrategyConfig().setExtraFieldProvider(extraFieldProvider);
        return this;
    }
}
