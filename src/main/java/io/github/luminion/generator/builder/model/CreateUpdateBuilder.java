package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.CreateUpdateConfig;

/**
 * 创建更新功能配置构建器
 * <p>
 * 整合创建DTO和更新DTO的配置
 *
 * @author luminion
 * @since 1.0.0
 */
public class CreateUpdateBuilder {

    private final CreateUpdateConfig config;

    /**
     * 创建参数构建器 (对应 CreateDTO)
     */
    private final CreateParamBuilder createParamBuilder;

    /**
     * 更新参数构建器 (对应 UpdateDTO)
     */
    private final UpdateParamBuilder updateParamBuilder;

    public CreateUpdateBuilder(CreateUpdateConfig config) {
        this.config = config;
        this.createParamBuilder = new CreateParamBuilder(config);
        this.updateParamBuilder = new UpdateParamBuilder(config);
    }

    /**
     * 配置创建参数 (CreateDTO)
     *
     * @param consumer 创建配置
     * @return this
     */
    public CreateUpdateBuilder createParam(java.util.function.Consumer<CreateParamBuilder> consumer) {
        consumer.accept(createParamBuilder);
        return this;
    }

    /**
     * 配置更新参数 (UpdateDTO)
     *
     * @param consumer 更新配置
     * @return this
     */
    public CreateUpdateBuilder updateParam(java.util.function.Consumer<UpdateParamBuilder> consumer) {
        consumer.accept(updateParamBuilder);
        return this;
    }

    /**
     * 创建参数构建器
     * 对应 CreateDTO 配置
     */
    public static class CreateParamBuilder {
        private final CreateUpdateConfig config;

        public CreateParamBuilder(CreateUpdateConfig config) {
            this.config = config;
        }

        /**
         * 禁用生成
         *
         * @return this
         */
        public CreateParamBuilder generateDisable() {
            config.getCreateTemplateFile().setGenerate(false);
            return this;
        }

        /**
         * 设置名称格式
         *
         * @param nameFormat 名称格式，如 "%sCreateDTO"
         * @return this
         */
        public CreateParamBuilder nameFormat(String nameFormat) {
            config.getCreateTemplateFile().setNameFormat(nameFormat);
            return this;
        }

        /**
         * 设置子包名
         *
         * @param subPackage 子包名
         * @return this
         */
        public CreateParamBuilder subPackage(String subPackage) {
            config.getCreateTemplateFile().setSubPackage(subPackage);
            return this;
        }

        /**
         * 设置模板路径
         *
         * @param templatePath 模板路径
         * @return this
         */
        public CreateParamBuilder templatePath(String templatePath) {
            config.getCreateTemplateFile().setTemplatePath(templatePath);
            return this;
        }

        /**
         * 设置输出目录
         *
         * @param outputDir 输出目录
         * @return this
         */
        public CreateParamBuilder outputDir(String outputDir) {
            config.getCreateTemplateFile().setOutputDir(outputDir);
            return this;
        }

        /**
         * 启用文件覆盖
         *
         * @return this
         */
        public CreateParamBuilder fileOverrideEnable() {
            config.getCreateTemplateFile().setFileOverride(true);
            return this;
        }
    }

    /**
     * 更新参数构建器
     * 对应 UpdateDTO 配置
     */
    public static class UpdateParamBuilder {
        private final CreateUpdateConfig config;

        public UpdateParamBuilder(CreateUpdateConfig config) {
            this.config = config;
        }

        /**
         * 禁用生成
         *
         * @return this
         */
        public UpdateParamBuilder generateDisable() {
            config.getUpdateTemplateFile().setGenerate(false);
            return this;
        }

        /**
         * 设置名称格式
         *
         * @param nameFormat 名称格式，如 "%sUpdateDTO"
         * @return this
         */
        public UpdateParamBuilder nameFormat(String nameFormat) {
            config.getUpdateTemplateFile().setNameFormat(nameFormat);
            return this;
        }

        /**
         * 设置子包名
         *
         * @param subPackage 子包名
         * @return this
         */
        public UpdateParamBuilder subPackage(String subPackage) {
            config.getUpdateTemplateFile().setSubPackage(subPackage);
            return this;
        }

        /**
         * 设置模板路径
         *
         * @param templatePath 模板路径
         * @return this
         */
        public UpdateParamBuilder templatePath(String templatePath) {
            config.getUpdateTemplateFile().setTemplatePath(templatePath);
            return this;
        }

        /**
         * 设置输出目录
         *
         * @param outputDir 输出目录
         * @return this
         */
        public UpdateParamBuilder outputDir(String outputDir) {
            config.getUpdateTemplateFile().setOutputDir(outputDir);
            return this;
        }

        /**
         * 启用文件覆盖
         *
         * @return this
         */
        public UpdateParamBuilder fileOverrideEnable() {
            config.getUpdateTemplateFile().setFileOverride(true);
            return this;
        }
    }
}
