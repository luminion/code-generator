package io.github.luminion.generator.config.common;

import io.github.luminion.generator.config.po.TableField;
import io.github.luminion.generator.config.po.TableInfo;
import io.github.luminion.generator.config.enums.NamingStrategy;
import io.github.luminion.generator.config.base.StrategyConfig;
import io.github.luminion.generator.util.StringUtils;

import java.util.Set;

/**
 * @author luminion
 * @since 1.0.0
 */
public class DefaultNameConvert implements INameConvert {
    private final StrategyConfig strategyConfig ;

    public DefaultNameConvert(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
    }

    @Override
    public String entityNameConvert(TableInfo tableInfo) {
        String processedName = processName(tableInfo.getName(), strategyConfig.getEntityNaming(), strategyConfig.getTablePrefix(), strategyConfig.getTableSuffix());
        return NamingStrategy.capitalFirst(processedName);
    }

    @Override
    public String propertyNameConvert(TableField field) {
        return processName(field.getName(), strategyConfig.getColumnNaming(), strategyConfig.getFieldPrefix(), strategyConfig.getFieldSuffix());
    }

    private String processName(String name, NamingStrategy strategy, Set<String> prefix, Set<String> suffix) {
        String propertyName = name;
        // 删除前缀
        if (!prefix.isEmpty()) {
            propertyName = NamingStrategy.removePrefix(propertyName, prefix);
        }
        // 删除后缀
        if (!suffix.isEmpty()) {
            propertyName = NamingStrategy.removeSuffix(propertyName, suffix);
        }
        if (StringUtils.isBlank(propertyName)) {
            throw new RuntimeException(String.format("%s 的名称转换结果为空，请检查是否配置问题", name));
        }
        // 下划线转驼峰
        if (NamingStrategy.underline_to_camel.equals(strategy)) {
            return NamingStrategy.underlineToCamel(propertyName);
        }
        return propertyName;
    }
}
