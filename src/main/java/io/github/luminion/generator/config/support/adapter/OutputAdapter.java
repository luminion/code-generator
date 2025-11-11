package io.github.luminion.generator.config.support.adapter;

import io.github.luminion.generator.config.po.TemplateFile;
import io.github.luminion.generator.config.support.OutputConfig;

import java.util.function.Function;

/**
 * @author luminion
 * @since 1.0.0
 */
public class OutputAdapter {
    private final OutputConfig outputConfig;

    public OutputAdapter(OutputConfig config) {
        this.outputConfig = config;
    }

    /**
     * 文件输出目录
     *
     * @param outputDir 文件输出目录
     * @return this
     */
    public OutputAdapter outputDir(String outputDir) {
        this.outputConfig.setOutputDir(outputDir);
        return this;
    }

    /**
     * 父包名
     *
     * @param parentPackage 父包名
     * @return this
     */
    public OutputAdapter parentPackage(String parentPackage) {
        this.outputConfig.setParentPackage(parentPackage);
        return this;
    }

    /**
     * 模块名
     *
     * @param moduleName 模块名
     * @return this
     */
    public OutputAdapter moduleName(String moduleName) {
        this.outputConfig.setModuleName(moduleName);
        return this;
    }

    /**
     * 启用全局文件覆盖(仅影响本配置提供的模板文件)
     *
     * @return this
     */
    public OutputAdapter enableGlobalFileOverride() {
        this.outputConfig.setGlobalFileOverride(true);
        return this;
    }

    /**
     * 禁用打开输出目录
     *
     * @return this
     */
    public OutputAdapter disableOpenOutputDir() {
        this.outputConfig.setOpen(false);
        return this;
    }

    /**
     * 启用打开输出目录
     *
     * @return this
     * @deprecated 默认值,无需设置
     */
    @Deprecated
    public OutputAdapter enableOpenOutputDir() {
        this.outputConfig.setOpen(true);
        return this;
    }

    /**
     * entity格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputAdapter entityFormatPattern(String formatPattern){
        this.outputConfig.getEntity().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * entity子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputAdapter entitySubPackage(String subPackage){
        this.outputConfig.getEntity().setSubPackage(subPackage);
        return this;
    }


    /**
     * entity模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputAdapter entityTemplatePath(String templatePath){
        this.outputConfig.getEntity().setTemplatePath(templatePath);
        return this;
    }


    /**
     * entity文件输出目录
     *
     * @param outputDir 输出方向
     * @return this
     */
    public OutputAdapter entityOutputDir(String outputDir){
        this.outputConfig.getEntity().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成entity
     *
     * @return this
     */
    public OutputAdapter entityDisable(){
        this.outputConfig.getEntity().setGenerate(false);
        return this;
    }

    /**
     * mapper格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputAdapter mapperFormatPattern(String formatPattern){
        this.outputConfig.getMapper().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * mapper子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputAdapter mapperSubPackage(String subPackage){
        this.outputConfig.getMapper().setSubPackage(subPackage);
        return this;
    }

    /**
     * mapper模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputAdapter mapperTemplatePath(String templatePath){
        this.outputConfig.getMapper().setTemplatePath(templatePath);
        return this;
    }


    /**
     * mapper文件输出目录
     *
     * @param outputDir 输出方向
     * @return this
     */
    public OutputAdapter mapperOutputDir(String outputDir){
        this.outputConfig.getMapper().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成mapper
     *
     * @return this
     */
    public OutputAdapter mapperDisable(){
        this.outputConfig.getMapper().setGenerate(false);
        return this;
    }

    /**
     * mapperXml格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputAdapter mapperXmlFormatPattern(String formatPattern){
        this.outputConfig.getMapperXml().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * mapperXml模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputAdapter mapperXmlTemplatePath(String templatePath){
        this.outputConfig.getMapperXml().setTemplatePath(templatePath);
        return this;
    }
    
    /**
     * mapperXml文件输出目录
     *
     * @param outputDir 输出方向
     * @return this
     */
    public OutputAdapter mapperXmlOutputDir(String outputDir){
        this.outputConfig.getMapperXml().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成mapperXml
     *
     * @return this
     */
    public OutputAdapter mapperXmlDisable(){
        this.outputConfig.getMapperXml().setGenerate(false);
        return this;
    }
    
    /**
     * service格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputAdapter serviceFormatPattern(String formatPattern){
        this.outputConfig.getService().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * service子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputAdapter serviceSubPackage(String subPackage){
        this.outputConfig.getService().setSubPackage(subPackage);
        return this;
    }


    /**
     * service模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputAdapter serviceTemplatePath(String templatePath){
        this.outputConfig.getService().setTemplatePath(templatePath);
        return this;
    }


    /**
     * service文件输出目录
     *
     * @param outputDir 输出方向
     * @return this
     */
    public OutputAdapter serviceOutputDir(String outputDir){
        this.outputConfig.getService().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成service
     *
     * @return this
     */
    public OutputAdapter serviceDisable(){
        this.outputConfig.getService().setGenerate(false);
        return this;
    }
    
    /**
     * serviceImpl格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputAdapter serviceImplFormatPattern(String formatPattern){
        this.outputConfig.getServiceImpl().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * serviceImpl子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputAdapter serviceImplSubPackage(String subPackage){
        this.outputConfig.getServiceImpl().setSubPackage(subPackage);
        return this;
    }


    /**
     * serviceImpl模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputAdapter serviceImplTemplatePath(String templatePath){
        this.outputConfig.getServiceImpl().setTemplatePath(templatePath);
        return this;
    }


    /**
     * serviceImpl文件输出目录
     *
     * @param outputDir 输出方向
     * @return this
     */
    public OutputAdapter serviceImplOutputDir(String outputDir){
        this.outputConfig.getServiceImpl().setOutputDir(outputDir);
        return this;
    }

    /**
     * 不生成serviceImpl
     *
     * @return this
     */
    public OutputAdapter serviceImplDisable(){
        this.outputConfig.getServiceImpl().setGenerate(false);
        return this;
    }
    

    /**
     * controller配置
     *
     * @param adapter 适配器
     */
    public OutputAdapter controller(Function<TemplateFile.Adapter, TemplateFile.Adapter> adapter) {
        adapter.apply(this.outputConfig.controller.adapter());
        return this;
    }

    /**
     * insertDTO配置
     *
     * @param adapter 适配器
     */
    public OutputAdapter insertDTO(Function<TemplateFile.Adapter, TemplateFile.Adapter> adapter) {
        adapter.apply(this.outputConfig.insertDTO.adapter());
        return this;
    }

    /**
     * updateDTO配置
     *
     * @param adapter 适配器
     */
    public OutputAdapter updateDTO(Function<TemplateFile.Adapter, TemplateFile.Adapter> adapter) {
        adapter.apply(this.outputConfig.updateDTO.adapter());
        return this;
    }

    /**
     * queryDTO配置
     *
     * @param adapter 适配器
     */
    public OutputAdapter queryDTO(Function<TemplateFile.Adapter, TemplateFile.Adapter> adapter) {
        adapter.apply(this.outputConfig.queryDTO.adapter());
        return this;
    }

    /**
     * vo配置
     *
     * @param adapter 适配器
     */
    public OutputAdapter queryVO(Function<TemplateFile.Adapter, TemplateFile.Adapter> adapter) {
        adapter.apply(this.outputConfig.queryVO.adapter());
        return this;
    }
}
