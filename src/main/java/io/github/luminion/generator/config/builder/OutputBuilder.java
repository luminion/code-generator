package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.config.base.OutputConfig;

/**
 * @author luminion
 * @since 1.0.0
 */
public class OutputBuilder {
    private final OutputConfig outputConfig;

    public OutputBuilder(OutputConfig config) {
        this.outputConfig = config;
    }

    /**
     * 文件输出目录
     *
     * @param outputDir 文件输出目录
     * @return this
     */
    public OutputBuilder outputDir(String outputDir) {
        this.outputConfig.setOutputDir(outputDir);
        return this;
    }

    /**
     * 父包名
     *
     * @param parentPackage 父包名
     * @return this
     */
    public OutputBuilder parentPackage(String parentPackage) {
        this.outputConfig.setParentPackage(parentPackage);
        return this;
    }

    /**
     * 模块名
     *
     * @param moduleName 模块名
     * @return this
     */
    public OutputBuilder moduleName(String moduleName) {
        this.outputConfig.setModuleName(moduleName);
        return this;
    }

    /**
     * 启用全局文件覆盖(仅影响本配置提供的模板文件)
     *
     * @return this
     */
    public OutputBuilder enableGlobalFileOverride() {
        this.outputConfig.setOutputFileGlobalOverride(true);
        return this;
    }

    /**
     * 禁用打开输出目录
     *
     * @return this
     */
    public OutputBuilder disableOpenOutputDir() {
        this.outputConfig.setOutputDirOpen(false);
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
        this.outputConfig.setOutputDirOpen(true);
        return this;
    }

    /**
     * entity格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder entityFormatPattern(String formatPattern){
        this.outputConfig.getEntity().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * entity子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder entitySubPackage(String subPackage){
        this.outputConfig.getEntity().setSubPackage(subPackage);
        return this;
    }


    /**
     * entity模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder entityTemplatePath(String templatePath){
        this.outputConfig.getEntity().setTemplatePath(templatePath);
        return this;
    }


    /**
     * entity文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder entityOutputDir(String outputDir){
        this.outputConfig.getEntity().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成entity
     *
     * @return this
     */
    public OutputBuilder entityDisable(){
        this.outputConfig.getEntity().setGenerate(false);
        return this;
    }

    /**
     * mapper格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder mapperFormatPattern(String formatPattern){
        this.outputConfig.getMapper().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * mapper子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder mapperSubPackage(String subPackage){
        this.outputConfig.getMapper().setSubPackage(subPackage);
        return this;
    }

    /**
     * mapper模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder mapperTemplatePath(String templatePath){
        this.outputConfig.getMapper().setTemplatePath(templatePath);
        return this;
    }


    /**
     * mapper文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder mapperOutputDir(String outputDir){
        this.outputConfig.getMapper().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成mapper
     *
     * @return this
     */
    public OutputBuilder mapperDisable(){
        this.outputConfig.getMapper().setGenerate(false);
        return this;
    }

    /**
     * mapperXml格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder mapperXmlFormatPattern(String formatPattern){
        this.outputConfig.getMapperXml().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * mapperXml模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder mapperXmlTemplatePath(String templatePath){
        this.outputConfig.getMapperXml().setTemplatePath(templatePath);
        return this;
    }
    
    /**
     * mapperXml文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder mapperXmlOutputDir(String outputDir){
        this.outputConfig.getMapperXml().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成mapperXml
     *
     * @return this
     */
    public OutputBuilder mapperXmlDisable(){
        this.outputConfig.getMapperXml().setGenerate(false);
        return this;
    }
    
    /**
     * service格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder serviceFormatPattern(String formatPattern){
        this.outputConfig.getService().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * service子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder serviceSubPackage(String subPackage){
        this.outputConfig.getService().setSubPackage(subPackage);
        return this;
    }


    /**
     * service模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder serviceTemplatePath(String templatePath){
        this.outputConfig.getService().setTemplatePath(templatePath);
        return this;
    }


    /**
     * service文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder serviceOutputDir(String outputDir){
        this.outputConfig.getService().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成service
     *
     * @return this
     */
    public OutputBuilder serviceDisable(){
        this.outputConfig.getService().setGenerate(false);
        return this;
    }
    
    /**
     * serviceImpl格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder serviceImplFormatPattern(String formatPattern){
        this.outputConfig.getServiceImpl().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * serviceImpl子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder serviceImplSubPackage(String subPackage){
        this.outputConfig.getServiceImpl().setSubPackage(subPackage);
        return this;
    }


    /**
     * serviceImpl模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder serviceImplTemplatePath(String templatePath){
        this.outputConfig.getServiceImpl().setTemplatePath(templatePath);
        return this;
    }


    /**
     * serviceImpl文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder serviceImplOutputDir(String outputDir){
        this.outputConfig.getServiceImpl().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成serviceImpl
     *
     * @return this
     */
    public OutputBuilder serviceImplDisable(){
        this.outputConfig.getServiceImpl().setGenerate(false);
        return this;
    }

    /**
     * controller格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder controllerFormatPattern(String formatPattern){
        this.outputConfig.getController().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * controller子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder controllerSubPackage(String subPackage){
        this.outputConfig.getController().setSubPackage(subPackage);
        return this;
    }


    /**
     * controller模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder controllerTemplatePath(String templatePath){
        this.outputConfig.getController().setTemplatePath(templatePath);
        return this;
    }

    public OutputBuilder controllerOutputDir(String outputDir){
        this.outputConfig.getController().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成controller
     *
     * @return this
     */
    public OutputBuilder controllerDisable(){
        this.outputConfig.getController().setGenerate(false);
        return this;
    }
    
    /**
     * insertDTO格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder insertDTOFormatPattern(String formatPattern){
        this.outputConfig.getInsertDTO().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * insertDTO子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder insertDTOSubPackage(String subPackage){
        this.outputConfig.getInsertDTO().setSubPackage(subPackage);
        return this;
    }

    /**
     * insertDTO模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder insertDTOTemplatePath(String templatePath){
        this.outputConfig.getInsertDTO().setTemplatePath(templatePath);
        return this;
    }

    /**
     * insertDTO文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder insertDTOOutputDir(String outputDir){
        this.outputConfig.getInsertDTO().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成insertDTO
     *
     * @return this
     */
    public OutputBuilder insertDTODisable(){
        this.outputConfig.getInsertDTO().setGenerate(false);
        return this;
    }

    /**
     * updateDTO格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder updateDTOFormatPattern(String formatPattern){
        this.outputConfig.getUpdateDTO().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * updateDTO子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder updateDTOSubPackage(String subPackage){
        this.outputConfig.getUpdateDTO().setSubPackage(subPackage);
        return this;
    }

    /**
     * updateDTO模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder updateDTOTemplatePath(String templatePath){
        this.outputConfig.getUpdateDTO().setTemplatePath(templatePath);
        return this;
    }

    /**
     * updateDTO文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder updateDTOOutputDir(String outputDir){
        this.outputConfig.getUpdateDTO().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成updateDTO
     *
     * @return this
     */
    public OutputBuilder updateDTODisable(){
        this.outputConfig.getUpdateDTO().setGenerate(false);
        return this;
    }

    /**
     * queryDTO格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder queryDTOFormatPattern(String formatPattern){
        this.outputConfig.getQueryDTO().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * queryDTO子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder queryDTOSubPackage(String subPackage){
        this.outputConfig.getQueryDTO().setSubPackage(subPackage);
        return this;
    }

    /**
     * queryDTO模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder queryDTOTemplatePath(String templatePath){
        this.outputConfig.getQueryDTO().setTemplatePath(templatePath);
        return this;
    }

    /**
     * queryDTO文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder queryDTOOutputDir(String outputDir){
        this.outputConfig.getQueryDTO().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成queryDTO
     *
     * @return this
     */
    public OutputBuilder queryDTODisable(){
        this.outputConfig.getQueryDTO().setGenerate(false);
        return this;
    }

    /**
     * queryVO格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputBuilder queryVOFormatPattern(String formatPattern){
        this.outputConfig.getQueryVO().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * queryVO子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputBuilder queryVOSubPackage(String subPackage){
        this.outputConfig.getQueryVO().setSubPackage(subPackage);
        return this;
    }

    /**
     * queryVO模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputBuilder queryVOTemplatePath(String templatePath){
        this.outputConfig.getQueryVO().setTemplatePath(templatePath);
        return this;
    }

    /**
     * queryVO文件输出目录
     *
     * @param outputDir 输出目录
     * @return this
     */
    public OutputBuilder queryVOOutputDir(String outputDir){
        this.outputConfig.getQueryVO().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成queryVO
     *
     * @return this
     */
    public OutputBuilder queryVODisable(){
        this.outputConfig.getQueryVO().setGenerate(false);
        return this;
    }


}
