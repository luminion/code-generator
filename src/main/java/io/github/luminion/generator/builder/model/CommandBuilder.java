package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.CommandConfig;

/**
 * 命令功能配置构建器
 * <p>
 * 整合创建DTO和更新DTO的配置，对应CQRS模式的Command
 *
 * @author luminion
 * @since 1.0.0
 */
public class CommandBuilder {

    private final CommandConfig config;

    /**
     * 创建参数构建器 (对应 CreateDTO)
     */
    private final CreateBuilder createBuilder;

    /**
     * 更新参数构建器 (对应 UpdateDTO)
     */
    private final UpdateBuilder updateBuilder;

    public CommandBuilder(CommandConfig config) {
        this.config = config;
        this.createBuilder = new CreateBuilder(config);
        this.updateBuilder = new UpdateBuilder(config);
    }

    /**
     * 配置创建参数 (CreateDTO)
     *
     * @param consumer 创建配置
     * @return this
     */
    public CommandBuilder create(java.util.function.Consumer<CreateBuilder> consumer) {
        consumer.accept(createBuilder);
        return this;
    }

    /**
     * 配置更新参数 (UpdateDTO)
     *
     * @param consumer 更新配置
     * @return this
     */
    public CommandBuilder update(java.util.function.Consumer<UpdateBuilder> consumer) {
        consumer.accept(updateBuilder);
        return this;
    }

    /**
     * 创建参数构建器
     * 对应 CreateDTO 配置
     */
    public static class CreateBuilder {
        private final CommandConfig config;

        public CreateBuilder(CommandConfig config) {
            this.config = config;
        }

        /**
         * 禁用生成
         *
         * @return this
         */
        public CreateBuilder generateDisable() {
            config.getCreateTemplateFile().setGenerate(false);
            return this;
        }

        /**
         * 设置名称格式
         *
         * @param nameFormat 名称格式，如 "%sCreateDTO" 或 "%sAddDTO"
         * @return this
         */
        public CreateBuilder nameFormat(String nameFormat) {
            config.getCreateTemplateFile().setNameFormat(nameFormat);
            return this;
        }

        /**
         * 设置子包名
         *
         * @param subPackage 子包名
         * @return this
         */
        public CreateBuilder subPackage(String subPackage) {
            config.getCreateTemplateFile().setSubPackage(subPackage);
            return this;
        }

        /**
         * 设置模板路径
         *
         * @param templatePath 模板路径
         * @return this
         */
        public CreateBuilder templatePath(String templatePath) {
            config.getCreateTemplateFile().setTemplatePath(templatePath);
            return this;
        }

        /**
         * 设置输出目录
         *
         * @param outputDir 输出目录
         * @return this
         */
        public CreateBuilder outputDir(String outputDir) {
            config.getCreateTemplateFile().setOutputDir(outputDir);
            return this;
        }

        /**
         * 启用文件覆盖
         *
         * @return this
         */
        public CreateBuilder fileOverrideEnable() {
            config.getCreateTemplateFile().setFileOverride(true);
            return this;
        }
    }

    /**
     * 更新参数构建器
     * 对应 UpdateDTO 配置
     */
    public static class UpdateBuilder {
        private final CommandConfig config;

        public UpdateBuilder(CommandConfig config) {
            this.config = config;
        }

        /**
         * 禁用生成
         *
         * @return this
         */
        public UpdateBuilder generateDisable() {
            config.getUpdateTemplateFile().setGenerate(false);
            return this;
        }

        /**
         * 设置名称格式
         *
         * @param nameFormat 名称格式，如 "%sUpdateDTO" 或 "%sEditDTO"
         * @return this
         */
        public UpdateBuilder nameFormat(String nameFormat) {
            config.getUpdateTemplateFile().setNameFormat(nameFormat);
            return this;
        }

        /**
         * 设置子包名
         *
         * @param subPackage 子包名
         * @return this
         */
        public UpdateBuilder subPackage(String subPackage) {
            config.getUpdateTemplateFile().setSubPackage(subPackage);
            return this;
        }

        /**
         * 设置模板路径
         *
         * @param templatePath 模板路径
         * @return this
         */
        public UpdateBuilder templatePath(String templatePath) {
            config.getUpdateTemplateFile().setTemplatePath(templatePath);
            return this;
        }

        /**
         * 设置输出目录
         *
         * @param outputDir 输出目录
         * @return this
         */
        public UpdateBuilder outputDir(String outputDir) {
            config.getUpdateTemplateFile().setOutputDir(outputDir);
            return this;
        }

        /**
         * 启用文件覆盖
         *
         * @return this
         */
        public UpdateBuilder fileOverrideEnable() {
            config.getUpdateTemplateFile().setFileOverride(true);
            return this;
        }
    }
}
