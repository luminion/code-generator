package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.config.Configurer;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class OutputBuilder {
    private final Configurer configurer;


    /**
     * 文件输出目录
     *
     * @param outputDir 文件输出目录
     * @return this
     */
    public OutputBuilder outputDir(String outputDir) {
        this.configurer.getOutputConfig().setOutputDir(outputDir);
        return this;
    }

    /**
     * 父包名
     *
     * @param parentPackage 父包名
     * @return this
     */
    public OutputBuilder parentPackage(String parentPackage) {
        this.configurer.getOutputConfig().setParentPackage(parentPackage);
        return this;
    }

    /**
     * 模块名
     *
     * @param moduleName 模块名
     * @return this
     */
    public OutputBuilder moduleName(String moduleName) {
        this.configurer.getOutputConfig().setModuleName(moduleName);
        return this;
    }

    /**
     * 启用全局文件覆盖(仅影响本配置提供的模板文件)
     *
     * @return this
     */
    public OutputBuilder enableGlobalFileOverride() {
        this.configurer.getOutputConfig().setOutputFileGlobalOverride(true);
        return this;
    }

    /**
     * 禁用打开输出目录
     *
     * @return this
     */
    public OutputBuilder disableOpenOutputDir() {
        this.configurer.getOutputConfig().setOutputDirOpen(false);
        return this;
    }

    /**
     * 启用打开输出目录
     *
     * @return this
     * @deprecated 默认值,无需设置
     */
    @Deprecated
    public OutputBuilder enableOpenOutputDir() {
        this.configurer.getOutputConfig().setOutputDirOpen(true);
        return this;
    }

    /**
     * entity格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder entityFormatPattern(String formatPattern) {
        this.configurer.getOutputConfig().getEntity().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * entity子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder entitySubPackage(String subPackage) {
        this.configurer.getOutputConfig().getEntity().setSubPackage(subPackage);
        return this;
    }


    /**
     * entity模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder entityTemplatePath(String templatePath) {
        this.configurer.getOutputConfig().getEntity().setTemplatePath(templatePath);
        return this;
    }


    /**
     * entity文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder entityOutputDir(String outputDir) {
        this.configurer.getOutputConfig().getEntity().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成entity
     *
     * @return this
     */
    public OutputBuilder entityDisable() {
        this.configurer.getOutputConfig().getEntity().setGenerate(false);
        return this;
    }

    /**
     * mapper格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder mapperFormatPattern(String formatPattern) {
        this.configurer.getOutputConfig().getMapper().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * mapper子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder mapperSubPackage(String subPackage) {
        this.configurer.getOutputConfig().getMapper().setSubPackage(subPackage);
        return this;
    }

    /**
     * mapper模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder mapperTemplatePath(String templatePath) {
        this.configurer.getOutputConfig().getMapper().setTemplatePath(templatePath);
        return this;
    }


    /**
     * mapper文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder mapperOutputDir(String outputDir) {
        this.configurer.getOutputConfig().getMapper().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成mapper
     *
     * @return this
     */
    public OutputBuilder mapperDisable() {
        this.configurer.getOutputConfig().getMapper().setGenerate(false);
        return this;
    }

    /**
     * mapperXml格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder mapperXmlFormatPattern(String formatPattern) {
        this.configurer.getOutputConfig().getMapperXml().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * mapperXml模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder mapperXmlTemplatePath(String templatePath) {
        this.configurer.getOutputConfig().getMapperXml().setTemplatePath(templatePath);
        return this;
    }

    /**
     * mapperXml文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder mapperXmlOutputDir(String outputDir) {
        this.configurer.getOutputConfig().getMapperXml().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成mapperXml
     *
     * @return this
     */
    public OutputBuilder mapperXmlDisable() {
        this.configurer.getOutputConfig().getMapperXml().setGenerate(false);
        return this;
    }

    /**
     * service格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder serviceFormatPattern(String formatPattern) {
        this.configurer.getOutputConfig().getService().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * service子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder serviceSubPackage(String subPackage) {
        this.configurer.getOutputConfig().getService().setSubPackage(subPackage);
        return this;
    }


    /**
     * service模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder serviceTemplatePath(String templatePath) {
        this.configurer.getOutputConfig().getService().setTemplatePath(templatePath);
        return this;
    }


    /**
     * service文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder serviceOutputDir(String outputDir) {
        this.configurer.getOutputConfig().getService().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成service
     *
     * @return this
     */
    public OutputBuilder serviceDisable() {
        this.configurer.getOutputConfig().getService().setGenerate(false);
        return this;
    }

    /**
     * serviceImpl格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder serviceImplFormatPattern(String formatPattern) {
        this.configurer.getOutputConfig().getServiceImpl().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * serviceImpl子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder serviceImplSubPackage(String subPackage) {
        this.configurer.getOutputConfig().getServiceImpl().setSubPackage(subPackage);
        return this;
    }


    /**
     * serviceImpl模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder serviceImplTemplatePath(String templatePath) {
        this.configurer.getOutputConfig().getServiceImpl().setTemplatePath(templatePath);
        return this;
    }


    /**
     * serviceImpl文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder serviceImplOutputDir(String outputDir) {
        this.configurer.getOutputConfig().getServiceImpl().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成serviceImpl
     *
     * @return this
     */
    public OutputBuilder serviceImplDisable() {
        this.configurer.getOutputConfig().getServiceImpl().setGenerate(false);
        return this;
    }

    /**
     * controller格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder controllerFormatPattern(String formatPattern) {
        this.configurer.getOutputConfig().getController().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * controller子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder controllerSubPackage(String subPackage) {
        this.configurer.getOutputConfig().getController().setSubPackage(subPackage);
        return this;
    }


    /**
     * controller模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder controllerTemplatePath(String templatePath) {
        this.configurer.getOutputConfig().getController().setTemplatePath(templatePath);
        return this;
    }

    public OutputBuilder controllerOutputDir(String outputDir) {
        this.configurer.getOutputConfig().getController().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成controller
     *
     * @return this
     */
    public OutputBuilder controllerDisable() {
        this.configurer.getOutputConfig().getController().setGenerate(false);
        return this;
    }

    /**
     * insertDTO格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder insertDTOFormatPattern(String formatPattern) {
        this.configurer.getOutputConfig().getInsertDTO().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * insertDTO子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder insertDTOSubPackage(String subPackage) {
        this.configurer.getOutputConfig().getInsertDTO().setSubPackage(subPackage);
        return this;
    }

    /**
     * insertDTO模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder insertDTOTemplatePath(String templatePath) {
        this.configurer.getOutputConfig().getInsertDTO().setTemplatePath(templatePath);
        return this;
    }

    /**
     * insertDTO文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder insertDTOOutputDir(String outputDir) {
        this.configurer.getOutputConfig().getInsertDTO().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成insertDTO
     *
     * @return this
     */
    public OutputBuilder insertDTODisable() {
        this.configurer.getOutputConfig().getInsertDTO().setGenerate(false);
        return this;
    }

    /**
     * updateDTO格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder updateDTOFormatPattern(String formatPattern) {
        this.configurer.getOutputConfig().getUpdateDTO().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * updateDTO子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder updateDTOSubPackage(String subPackage) {
        this.configurer.getOutputConfig().getUpdateDTO().setSubPackage(subPackage);
        return this;
    }

    /**
     * updateDTO模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder updateDTOTemplatePath(String templatePath) {
        this.configurer.getOutputConfig().getUpdateDTO().setTemplatePath(templatePath);
        return this;
    }

    /**
     * updateDTO文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder updateDTOOutputDir(String outputDir) {
        this.configurer.getOutputConfig().getUpdateDTO().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成updateDTO
     *
     * @return this
     */
    public OutputBuilder updateDTODisable() {
        this.configurer.getOutputConfig().getUpdateDTO().setGenerate(false);
        return this;
    }

    /**
     * queryDTO格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder queryDTOFormatPattern(String formatPattern) {
        this.configurer.getOutputConfig().getQueryDTO().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * queryDTO子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder queryDTOSubPackage(String subPackage) {
        this.configurer.getOutputConfig().getQueryDTO().setSubPackage(subPackage);
        return this;
    }

    /**
     * queryDTO模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder queryDTOTemplatePath(String templatePath) {
        this.configurer.getOutputConfig().getQueryDTO().setTemplatePath(templatePath);
        return this;
    }

    /**
     * queryDTO文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder queryDTOOutputDir(String outputDir) {
        this.configurer.getOutputConfig().getQueryDTO().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成queryDTO
     *
     * @return this
     */
    public OutputBuilder queryDTODisable() {
        this.configurer.getOutputConfig().getQueryDTO().setGenerate(false);
        return this;
    }

    /**
     * queryVO格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder queryVOFormatPattern(String formatPattern) {
        this.configurer.getOutputConfig().getQueryVO().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * queryVO子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder queryVOSubPackage(String subPackage) {
        this.configurer.getOutputConfig().getQueryVO().setSubPackage(subPackage);
        return this;
    }

    /**
     * queryVO模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder queryVOTemplatePath(String templatePath) {
        this.configurer.getOutputConfig().getQueryVO().setTemplatePath(templatePath);
        return this;
    }

    /**
     * queryVO文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder queryVOOutputDir(String outputDir) {
        this.configurer.getOutputConfig().getQueryVO().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成queryVO
     *
     * @return this
     */
    public OutputBuilder queryVODisable() {
        this.configurer.getOutputConfig().getQueryVO().setGenerate(false);
        return this;
    }


}
