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
     * 实体类格式化名称
     *
     * @param formatPattern 格式化名称
     * @return this
     */
    public OutputAdapter entityFormatPattern(String formatPattern){
        this.outputConfig.getEntity().setFormatPattern(formatPattern);
        return this;
    }

    /**
     * 实体类子包名
     *
     * @param subPackage 子包名
     * @return this
     */
    public OutputAdapter entitySubPackage(String subPackage){
        this.outputConfig.getEntity().setSubPackage(subPackage);
        return this;
    }


    /**
     * 实体类模板路径
     *
     * @param templatePath 模板路径
     * @return this
     */
    public OutputAdapter entityTemplatePath(String templatePath){
        this.outputConfig.getEntity().setTemplatePath(templatePath);
        return this;
    }


    /**
     * 实体类文件输出目录
     *
     * @param outputDir 输出方向
     * @return this
     */
    public OutputAdapter entityOutputDir(String outputDir){
        this.outputConfig.getEntity().setOutputDir(outputDir);
        return this;
    }

    /**
     * 禁用实体类
     *
     * @return this
     */
    public OutputAdapter entityDisable(){
        this.outputConfig.getEntity().setGenerate(false);
        return this;
    }


    

    /**
     * mapper配置
     *
     * @param adapter 适配器
     */
    public OutputAdapter mapper(Function<TemplateFile.Adapter, TemplateFile.Adapter> adapter) {
        adapter.apply(this.outputConfig.mapper.adapter());
        return this;
    }

    /**
     * mapperXml配置
     *
     * @param adapter 适配器
     */
    public OutputAdapter mapperXml(Function<TemplateFile.Adapter, TemplateFile.Adapter> adapter) {
        adapter.apply(this.outputConfig.mapperXml.adapter());
        return this;
    }

    /**
     * service配置
     *
     * @param adapter 适配器
     */
    public OutputAdapter service(Function<TemplateFile.Adapter, TemplateFile.Adapter> adapter) {
        adapter.apply(this.outputConfig.service.adapter());
        return this;
    }

    /**
     * serviceImpl配置
     *
     * @param adapter 适配器
     */
    public OutputAdapter serviceImpl(Function<TemplateFile.Adapter, TemplateFile.Adapter> adapter) {
        adapter.apply(this.outputConfig.serviceImpl.adapter());
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
