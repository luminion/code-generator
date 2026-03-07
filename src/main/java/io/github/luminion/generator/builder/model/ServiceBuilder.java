package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.ServiceConfig;

/**
 * Service功能配置构建器
 * <p>
 * 整合Service接口和ServiceImpl实现类的配置
 *
 * @author luminion
 * @since 1.0.0
 */
public class ServiceBuilder {

    private final ServiceConfig config;

    /**
     * Service接口构建器
     */
    private final ServiceInterfaceBuilder serviceBuilder;

    /**
     * ServiceImpl实现类构建器
     */
    private final ServiceImplBuilder serviceImplBuilder;

    public ServiceBuilder(ServiceConfig config) {
        this.config = config;
        this.serviceBuilder = new ServiceInterfaceBuilder(config);
        this.serviceImplBuilder = new ServiceImplBuilder(config);
    }

    /**
     * 配置Service接口
     *
     * @param consumer Service接口配置
     * @return this
     */
    public ServiceBuilder service(java.util.function.Consumer<ServiceInterfaceBuilder> consumer) {
        consumer.accept(serviceBuilder);
        return this;
    }

    /**
     * 配置ServiceImpl实现类
     *
     * @param consumer ServiceImpl配置
     * @return this
     */
    public ServiceBuilder serviceImpl(java.util.function.Consumer<ServiceImplBuilder> consumer) {
        consumer.accept(serviceImplBuilder);
        return this;
    }

    /**
     * Service接口构建器
     */
    public static class ServiceInterfaceBuilder {
        private final ServiceConfig config;

        public ServiceInterfaceBuilder(ServiceConfig config) {
            this.config = config;
        }

        /**
         * 设置父类
         *
         * @param superClass 父类全限定名
         * @return this
         */
        public ServiceInterfaceBuilder superClass(String superClass) {
            config.setServiceSuperClass(superClass);
            return this;
        }

        /**
         * 禁用生成
         *
         * @return this
         */
        public ServiceInterfaceBuilder generateDisable() {
            config.getServiceTemplateFile().setGenerate(false);
            return this;
        }

        /**
         * 设置名称格式
         *
         * @param nameFormat 名称格式
         * @return this
         */
        public ServiceInterfaceBuilder nameFormat(String nameFormat) {
            config.getServiceTemplateFile().setNameFormat(nameFormat);
            return this;
        }

        /**
         * 设置子包名
         *
         * @param subPackage 子包名
         * @return this
         */
        public ServiceInterfaceBuilder subPackage(String subPackage) {
            config.getServiceTemplateFile().setSubPackage(subPackage);
            return this;
        }

        /**
         * 设置模板路径
         *
         * @param templatePath 模板路径
         * @return this
         */
        public ServiceInterfaceBuilder templatePath(String templatePath) {
            config.getServiceTemplateFile().setTemplatePath(templatePath);
            return this;
        }

        /**
         * 设置输出目录
         *
         * @param outputDir 输出目录
         * @return this
         */
        public ServiceInterfaceBuilder outputDir(String outputDir) {
            config.getServiceTemplateFile().setOutputDir(outputDir);
            return this;
        }

        /**
         * 启用文件覆盖
         *
         * @return this
         */
        public ServiceInterfaceBuilder fileOverrideEnable() {
            config.getServiceTemplateFile().setFileOverride(true);
            return this;
        }
    }

    /**
     * ServiceImpl实现类构建器
     */
    public static class ServiceImplBuilder {
        private final ServiceConfig config;

        public ServiceImplBuilder(ServiceConfig config) {
            this.config = config;
        }

        /**
         * 设置父类
         *
         * @param superClass 父类全限定名
         * @return this
         */
        public ServiceImplBuilder superClass(String superClass) {
            config.setServiceImplSuperClass(superClass);
            return this;
        }

        /**
         * 禁用生成
         *
         * @return this
         */
        public ServiceImplBuilder generateDisable() {
            config.setGenerateServiceImpl(false);
            return this;
        }

        /**
         * 设置名称格式
         *
         * @param nameFormat 名称格式
         * @return this
         */
        public ServiceImplBuilder nameFormat(String nameFormat) {
            config.getServiceImplTemplateFile().setNameFormat(nameFormat);
            return this;
        }

        /**
         * 设置子包名
         *
         * @param subPackage 子包名
         * @return this
         */
        public ServiceImplBuilder subPackage(String subPackage) {
            config.getServiceImplTemplateFile().setSubPackage(subPackage);
            return this;
        }

        /**
         * 设置模板路径
         *
         * @param templatePath 模板路径
         * @return this
         */
        public ServiceImplBuilder templatePath(String templatePath) {
            config.getServiceImplTemplateFile().setTemplatePath(templatePath);
            return this;
        }

        /**
         * 设置输出目录
         *
         * @param outputDir 输出目录
         * @return this
         */
        public ServiceImplBuilder outputDir(String outputDir) {
            config.getServiceImplTemplateFile().setOutputDir(outputDir);
            return this;
        }

        /**
         * 启用文件覆盖
         *
         * @return this
         */
        public ServiceImplBuilder fileOverrideEnable() {
            config.getServiceImplTemplateFile().setFileOverride(true);
            return this;
        }
    }
}
