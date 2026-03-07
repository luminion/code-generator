package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.MapperConfig;

/**
 * Mapper功能配置构建器
 * <p>
 * 整合Mapper接口和MapperXml的配置
 *
 * @author luminion
 * @since 1.0.0
 */
public class MapperBuilder {

    private final MapperConfig config;

    /**
     * Mapper接口构建器
     */
    private final MapperInterfaceBuilder mapperBuilder;

    /**
     * MapperXml构建器
     */
    private final MapperXmlBuilder mapperXmlBuilder;

    public MapperBuilder(MapperConfig config) {
        this.config = config;
        this.mapperBuilder = new MapperInterfaceBuilder(config);
        this.mapperXmlBuilder = new MapperXmlBuilder(config);
    }

    /**
     * 配置Mapper接口
     *
     * @param consumer Mapper接口配置
     * @return this
     */
    public MapperBuilder mapper(java.util.function.Consumer<MapperInterfaceBuilder> consumer) {
        consumer.accept(mapperBuilder);
        return this;
    }

    /**
     * 配置MapperXml
     *
     * @param consumer MapperXml配置
     * @return this
     */
    public MapperBuilder mapperXml(java.util.function.Consumer<MapperXmlBuilder> consumer) {
        consumer.accept(mapperXmlBuilder);
        return this;
    }

    /**
     * Mapper接口构建器
     */
    public static class MapperInterfaceBuilder {
        private final MapperConfig config;

        public MapperInterfaceBuilder(MapperConfig config) {
            this.config = config;
        }

        /**
         * 设置父类
         *
         * @param superClass 父类全限定名
         * @return this
         */
        public MapperInterfaceBuilder superClass(String superClass) {
            config.setMapperSuperClass(superClass);
            return this;
        }

        /**
         * 设置Mapper注解类
         *
         * @param annotationClass 注解类全限定名
         * @return this
         */
        public MapperInterfaceBuilder annotationClass(String annotationClass) {
            config.setMapperAnnotationClass(annotationClass);
            return this;
        }

        /**
         * 禁用生成
         *
         * @return this
         */
        public MapperInterfaceBuilder generateDisable() {
            config.getMapperTemplateFile().setGenerate(false);
            return this;
        }

        /**
         * 设置名称格式
         *
         * @param nameFormat 名称格式
         * @return this
         */
        public MapperInterfaceBuilder nameFormat(String nameFormat) {
            config.getMapperTemplateFile().setNameFormat(nameFormat);
            return this;
        }

        /**
         * 设置子包名
         *
         * @param subPackage 子包名
         * @return this
         */
        public MapperInterfaceBuilder subPackage(String subPackage) {
            config.getMapperTemplateFile().setSubPackage(subPackage);
            return this;
        }

        /**
         * 设置模板路径
         *
         * @param templatePath 模板路径
         * @return this
         */
        public MapperInterfaceBuilder templatePath(String templatePath) {
            config.getMapperTemplateFile().setTemplatePath(templatePath);
            return this;
        }

        /**
         * 设置输出目录
         *
         * @param outputDir 输出目录
         * @return this
         */
        public MapperInterfaceBuilder outputDir(String outputDir) {
            config.getMapperTemplateFile().setOutputDir(outputDir);
            return this;
        }

        /**
         * 启用文件覆盖
         *
         * @return this
         */
        public MapperInterfaceBuilder fileOverrideEnable() {
            config.getMapperTemplateFile().setFileOverride(true);
            return this;
        }
    }

    /**
     * MapperXml构建器
     */
    public static class MapperXmlBuilder {
        private final MapperConfig config;

        public MapperXmlBuilder(MapperConfig config) {
            this.config = config;
        }

        /**
         * 禁用生成
         *
         * @return this
         */
        public MapperXmlBuilder generateDisable() {
            config.setGenerateMapperXml(false);
            return this;
        }

        /**
         * 启用BaseResultMap
         *
         * @return this
         */
        public MapperXmlBuilder baseResultMapEnable() {
            config.setBaseResultMap(true);
            return this;
        }

        /**
         * 启用baseColumnList
         *
         * @return this
         */
        public MapperXmlBuilder baseColumnListEnable() {
            config.setBaseColumnList(true);
            return this;
        }

        /**
         * 设置缓存实现类
         *
         * @param cacheClass 缓存类全限定名
         * @return this
         */
        public MapperXmlBuilder cacheClass(String cacheClass) {
            config.setCacheClass(cacheClass);
            return this;
        }

        /**
         * 添加排序列
         *
         * @param column 列名
         * @param desc 是否倒序
         * @return this
         */
        public MapperXmlBuilder sortColumn(String column, boolean desc) {
            config.getSortColumnMap().put(column, desc);
            return this;
        }

        /**
         * 清空排序列
         *
         * @return this
         */
        public MapperXmlBuilder sortColumnClear() {
            config.getSortColumnMap().clear();
            return this;
        }

        /**
         * 设置名称格式
         *
         * @param nameFormat 名称格式
         * @return this
         */
        public MapperXmlBuilder nameFormat(String nameFormat) {
            config.getMapperXmlTemplateFile().setNameFormat(nameFormat);
            return this;
        }

        /**
         * 设置子包名
         *
         * @param subPackage 子包名
         * @return this
         */
        public MapperXmlBuilder subPackage(String subPackage) {
            config.getMapperXmlTemplateFile().setSubPackage(subPackage);
            return this;
        }

        /**
         * 设置模板路径
         *
         * @param templatePath 模板路径
         * @return this
         */
        public MapperXmlBuilder templatePath(String templatePath) {
            config.getMapperXmlTemplateFile().setTemplatePath(templatePath);
            return this;
        }

        /**
         * 设置输出目录
         *
         * @param outputDir 输出目录
         * @return this
         */
        public MapperXmlBuilder outputDir(String outputDir) {
            config.getMapperXmlTemplateFile().setOutputDir(outputDir);
            return this;
        }

        /**
         * 启用文件覆盖
         *
         * @return this
         */
        public MapperXmlBuilder fileOverrideEnable() {
            config.getMapperXmlTemplateFile().setFileOverride(true);
            return this;
        }
    }
}
