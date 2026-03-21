package io.github.luminion.generator.builder.v2;

import io.github.luminion.generator.config.Configurer;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;

/**
 * Command配置构建器（创建/更新/删除）
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class CommandBuilder {
    private final Configurer configurer;

    /**
     * 禁用参数校验
     *
     * @return this
     */
    public CommandBuilder disableValid() {
        configurer.getCommandConfig().setValid(false);
        return this;
    }

    /**
     * 创建方法的名称
     *
     * @param methodName 方法名称
     * @return Command配置构建器（创建/更新/删除）
     */
    public CommandBuilder createMethodName(String methodName) {
        configurer.getCommandConfig().setCreateMethodName(methodName);
        return this;
    }

    /**
     * 更新方法的名称
     *
     * @param methodName 方法名称
     * @return Command配置构建器（创建/更新/删除）
     */
    public CommandBuilder updateMethodName(String methodName) {
        configurer.getCommandConfig().setUpdateMethodName(methodName);
        return this;
    }

    /**
     * 删除方法名称
     *
     * @param methodName 方法名称
     * @return Command配置构建器（创建/更新/删除）
     */
    public CommandBuilder deleteMethodName(String methodName) {
        configurer.getCommandConfig().setDeleteMethodName(methodName);
        return this;
    }

    /**
     * 添加创建/新增排除的字段
     *
     * @param columns 排除的字段
     * @return this
     */
    public CommandBuilder excludeColumns(String... columns) {
        configurer.getCommandConfig().getCommandExcludeColumns().addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * 添加创建/新增排除的字段
     *
     * @param columns 排除的字段
     * @return this
     */
    public CommandBuilder excludeColumns(Collection<String> columns) {
        configurer.getCommandConfig().getCommandExcludeColumns().addAll(columns);
        return this;
    }

    /**
     * 清空创建/新增排除字段
     *
     * @return this
     */
    public CommandBuilder clearExcludeColumns() {
        configurer.getCommandConfig().getCommandExcludeColumns().clear();
        return this;
    }
}
