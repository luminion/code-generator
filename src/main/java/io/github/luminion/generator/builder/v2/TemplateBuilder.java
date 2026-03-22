package io.github.luminion.generator.builder.v2;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.po.TemplateFile;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

/**
 * 模板配置构建器
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class TemplateBuilder {
    private final Configurer configurer;

    public TemplateBuilder outputDir(String outPutDir){
        configurer.getTemplateConfig().setOutputDir(outPutDir);
        return this;
    }
    
    public TemplateBuilder enableOpenOutputDir(){
        configurer.getTemplateConfig().setOpenOutputDir(true);
        return this;
    }
    
    public TemplateBuilder enableFileOverride(){
        configurer.getTemplateConfig().setFileOverride(true);
        return this;
    }
    
    public TemplateBuilder parentPackage(String parentPackage){
        configurer.getTemplateConfig().setParentPackage(parentPackage);
        return this;
    }

    public TemplateBuilder parentModule(String parentModule){
        configurer.getTemplateConfig().setParentModule(parentModule);
        return this;
    }
    
    public TemplateBuilder controller(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getController()));
        return this;
    }
    
    public TemplateBuilder service(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getService()));
        return this;
    }
    
    public TemplateBuilder serviceImpl(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getServiceImpl()));
        return this;
    }
    
    public TemplateBuilder mapper(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getMapper()));
        return this;
    }
    
    public TemplateBuilder mapperXml(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getMapperXml()));
        return this;
    }
    
    public TemplateBuilder entity(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getEntity()));
        return this;
    }
    
    public TemplateBuilder queryParam(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getQueryParam()));
        return this;
    }

    public TemplateBuilder queryResult(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getQueryResult()));
        return this;
    }
    
    public TemplateBuilder createParam(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getCreateParam()));
        return this;
    }
    
    public TemplateBuilder updateParam(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getUpdateParam()));
        return this;
    }
    
    public TemplateBuilder excelExportParam(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getExcelExportParam()));
        return this;
    }
    
    public TemplateBuilder excelImportParam(Function<TemplateFileBuilder, TemplateFileBuilder> func){
        func.apply(new TemplateFileBuilder(configurer.getTemplateConfig().getExcelImportParam()));
        return this;
    }
}
