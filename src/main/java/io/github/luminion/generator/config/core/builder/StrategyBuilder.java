package io.github.luminion.generator.config.core.builder;

import io.github.luminion.generator.config.Configurer;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class StrategyBuilder {
    private final Configurer configurer;


    /**
     * 增加过滤表前缀
     *
     * @param tablePrefix 过滤表前缀
     * @return this
     */
    public StrategyBuilder addTablePrefix(String... tablePrefix) {
        return addTablePrefix(Arrays.asList(tablePrefix));
    }

    /**
     * 添加表前缀
     *
     * @param tablePrefixList 表前缀列表
     * @return {@link StrategyBuilder }
     * @since 1.0.0
     */
    public StrategyBuilder addTablePrefix(List<String> tablePrefixList) {
        this.configurer.getStrategyConfig().getTablePrefix().addAll(tablePrefixList);
        return this;
    }

    /**
     * 增加过滤表后缀
     *
     * @param tableSuffix 过滤表后缀
     * @return this
     * @since 3.5.1
     */
    public StrategyBuilder addTableSuffix(String... tableSuffix) {
        return addTableSuffix(Arrays.asList(tableSuffix));
    }

    public StrategyBuilder addTableSuffix(List<String> tableSuffixList) {
        this.configurer.getStrategyConfig().getTableSuffix().addAll(tableSuffixList);
        return this;
    }

    /**
     * 增加过滤字段前缀
     *
     * @param fieldPrefix 过滤字段前缀
     * @return this
     * @since 3.5.0
     */
    public StrategyBuilder addFieldPrefix(String... fieldPrefix) {
        return addFieldPrefix(Arrays.asList(fieldPrefix));
    }

    public StrategyBuilder addFieldPrefix(List<String> fieldPrefix) {
        this.configurer.getStrategyConfig().getFieldPrefix().addAll(fieldPrefix);
        return this;
    }

    /**
     * 增加过滤字段后缀
     *
     * @param fieldSuffix 过滤字段后缀
     * @return this
     * @since 3.5.1
     */
    public StrategyBuilder addFieldSuffix(String... fieldSuffix) {
        return addFieldSuffix(Arrays.asList(fieldSuffix));
    }

    public StrategyBuilder addFieldSuffix(List<String> fieldSuffixList) {
        this.configurer.getStrategyConfig().getFieldSuffix().addAll(fieldSuffixList);
        return this;
    }

    /**
     * 增加包含的表名
     *
     * @param include 包含表
     * @return this
     */
    public StrategyBuilder addInclude(String... include) {
        this.configurer.getStrategyConfig().getInclude().addAll(Arrays.asList(include));
        return this;
    }

    public StrategyBuilder addInclude(List<String> includes) {
        this.configurer.getStrategyConfig().getInclude().addAll(includes);
        return this;
    }

    public StrategyBuilder addInclude(String include) {
        this.configurer.getStrategyConfig().getInclude().addAll(Arrays.asList(include.split(",")));
        return this;
    }

    /**
     * 增加排除表
     *
     * @param exclude 排除表
     * @return this
     * @since 3.5.0
     */
    public StrategyBuilder addExclude(String... exclude) {
        return addExclude(Arrays.asList(exclude));
    }

    public StrategyBuilder addExclude(List<String> excludeList) {
        this.configurer.getStrategyConfig().getExclude().addAll(excludeList);
        return this;
    }

    public StrategyBuilder addExclude(String exclude) {
        this.configurer.getStrategyConfig().getExclude().addAll(Arrays.asList(exclude.split(",")));
        return this;
    }

//    /**
//     * 包含表名
//     *
//     * @return this
//     */
//    public StrategyBuilder likeTable(LikeTable likeTable) {
//        this.configurer.getStrategyConfig().setLikeTable(likeTable);
//        return this;
//    }
//
//    /**
//     * 不包含表名
//     *
//     * @return this
//     */
//    public StrategyBuilder notLikeTable(LikeTable notLikeTable) {
//        this.configurer.getStrategyConfig().setNotLikeTable(notLikeTable);
//        return this;
//    }

    /**
     * 额外字段后缀
     *
     * @param suffix   后缀
     * @param operator sql运算符
     * @return this
     */
    public StrategyBuilder extraFieldSuffix(String suffix, String operator) {
        this.configurer.getStrategyConfig().getExtraFieldSuffixMap().put(suffix, operator);
        return this;
    }

    /**
     * 清除额外字段后缀
     *
     * @return this
     */
    public StrategyBuilder clearExtraFieldSuffix() {
        this.configurer.getStrategyConfig().getExtraFieldSuffixMap().clear();
        return this;
    }

//    /**
//     * 额外字段策略
//     *
//     * @param extraFieldStrategy 额外字段策略, 3个泛型参数分别为sql运算符,表字段信息,是否生成
//     * @return this
//     */
//    public StrategyBuilder extraFieldStrategy(BiFunction<String, TableField, Boolean> extraFieldStrategy) {
//        this.configurer.getStrategyConfig().setExtraFieldProvider(extraFieldStrategy);
//        return this;
//    }

    /**
     * 开启大写命名
     *
     * @return this
     * @since 3.5.0
     */
    public StrategyBuilder enableCapitalMode() {
        this.configurer.getStrategyConfig().setCapitalMode(true);
        return this;
    }

    /**
     * 开启跳过视图
     *
     * @return this
     * @since 3.5.0
     */
    public StrategyBuilder enableSkipView() {
        this.configurer.getStrategyConfig().setSkipView(true);
        return this;
    }

}
