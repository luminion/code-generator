package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.ExcelConfig;

/**
 * Excel功能配置构建器
 * <p>
 * 整合导入和导出DTO的配置
 *
 * @author luminion
 * @since 1.0.0
 */
public class ExcelBuilder {

    private final ExcelConfig config;

    /**
     * 导入Excel构建器 (对应 ImportDTO)
     */
    private final ImportExcelBuilder importExcelBuilder;

    /**
     * 导出Excel构建器 (对应 ExportDTO)
     */
    private final ExportExcelBuilder exportExcelBuilder;

    public ExcelBuilder(ExcelConfig config) {
        this.config = config;
        this.importExcelBuilder = new ImportExcelBuilder(config);
        this.exportExcelBuilder = new ExportExcelBuilder(config);
    }

    /**
     * 配置导入Excel (ImportDTO)
     *
     * @param consumer 导入配置
     * @return this
     */
    public ExcelBuilder importExcel(java.util.function.Consumer<ImportExcelBuilder> consumer) {
        consumer.accept(importExcelBuilder);
        return this;
    }

    /**
     * 配置导出Excel (ExportDTO)
     *
     * @param consumer 导出配置
     * @return this
     */
    public ExcelBuilder exportExcel(java.util.function.Consumer<ExportExcelBuilder> consumer) {
        consumer.accept(exportExcelBuilder);
        return this;
    }

    /**
     * 导入Excel构建器
     * 对应 ImportDTO 配置
     */
    public static class ImportExcelBuilder {
        private final ExcelConfig config;

        public ImportExcelBuilder(ExcelConfig config) {
            this.config = config;
        }

        /**
         * 禁用生成
         *
         * @return this
         */
        public ImportExcelBuilder generateDisable() {
            config.getImportTemplateFile().setGenerate(false);
            return this;
        }

        /**
         * 设置名称格式
         *
         * @param nameFormat 名称格式，如 "%sImportDTO"
         * @return this
         */
        public ImportExcelBuilder nameFormat(String nameFormat) {
            config.getImportTemplateFile().setNameFormat(nameFormat);
            return this;
        }

        /**
         * 设置子包名
         *
         * @param subPackage 子包名
         * @return this
         */
        public ImportExcelBuilder subPackage(String subPackage) {
            config.getImportTemplateFile().setSubPackage(subPackage);
            return this;
        }

        /**
         * 设置模板路径
         *
         * @param templatePath 模板路径
         * @return this
         */
        public ImportExcelBuilder templatePath(String templatePath) {
            config.getImportTemplateFile().setTemplatePath(templatePath);
            return this;
        }

        /**
         * 设置输出目录
         *
         * @param outputDir 输出目录
         * @return this
         */
        public ImportExcelBuilder outputDir(String outputDir) {
            config.getImportTemplateFile().setOutputDir(outputDir);
            return this;
        }

        /**
         * 启用文件覆盖
         *
         * @return this
         */
        public ImportExcelBuilder fileOverrideEnable() {
            config.getImportTemplateFile().setFileOverride(true);
            return this;
        }
    }

    /**
     * 导出Excel构建器
     * 对应 ExportDTO 配置
     */
    public static class ExportExcelBuilder {
        private final ExcelConfig config;

        public ExportExcelBuilder(ExcelConfig config) {
            this.config = config;
        }

        /**
         * 禁用生成
         *
         * @return this
         */
        public ExportExcelBuilder generateDisable() {
            config.getExportTemplateFile().setGenerate(false);
            return this;
        }

        /**
         * 设置名称格式
         *
         * @param nameFormat 名称格式，如 "%sExportDTO"
         * @return this
         */
        public ExportExcelBuilder nameFormat(String nameFormat) {
            config.getExportTemplateFile().setNameFormat(nameFormat);
            return this;
        }

        /**
         * 设置子包名
         *
         * @param subPackage 子包名
         * @return this
         */
        public ExportExcelBuilder subPackage(String subPackage) {
            config.getExportTemplateFile().setSubPackage(subPackage);
            return this;
        }

        /**
         * 设置模板路径
         *
         * @param templatePath 模板路径
         * @return this
         */
        public ExportExcelBuilder templatePath(String templatePath) {
            config.getExportTemplateFile().setTemplatePath(templatePath);
            return this;
        }

        /**
         * 设置输出目录
         *
         * @param outputDir 输出目录
         * @return this
         */
        public ExportExcelBuilder outputDir(String outputDir) {
            config.getExportTemplateFile().setOutputDir(outputDir);
            return this;
        }

        /**
         * 启用文件覆盖
         *
         * @return this
         */
        public ExportExcelBuilder fileOverrideEnable() {
            config.getExportTemplateFile().setFileOverride(true);
            return this;
        }
    }
}
