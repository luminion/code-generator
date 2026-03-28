package io.github.luminion.generator.builder;

import io.github.luminion.generator.config.Configurer;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

/**
 * 模板配置构建器
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class TemplateBuilder {
    private final Configurer configurer;

    public TemplateBuilder outputDir(String outputDir) {
        configurer.getTemplateConfig().setOutputDir(outputDir);
        return this;
    }

    public TemplateBuilder enableOpenOutputDir() {
        configurer.getTemplateConfig().setOpenOutputDir(true);
        return this;
    }

    public TemplateBuilder enableFileOverride() {
        configurer.getTemplateConfig().setFileOverride(true);
        return this;
    }

    public TemplateBuilder parentPackage(String parentPackage) {
        configurer.getTemplateConfig().setParentPackage(parentPackage);
        return this;
    }

    public TemplateBuilder parentModule(String parentModule) {
        configurer.getTemplateConfig().setParentModule(parentModule);
        return this;
    }

    public TemplateBuilder controller(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getController()));
        return this;
    }

    public TemplateBuilder service(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getService()));
        return this;
    }

    public TemplateBuilder serviceImpl(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getServiceImpl()));
        return this;
    }

    public TemplateBuilder mapper(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getMapper()));
        return this;
    }

    public TemplateBuilder mapperXml(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getMapperXml()));
        return this;
    }

    public TemplateBuilder entity(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getEntity()));
        return this;
    }

    public TemplateBuilder queryParam(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getQueryParam()));
        return this;
    }

    public TemplateBuilder queryResult(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getQueryResult()));
        return this;
    }

    public TemplateBuilder createParam(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getCreateParam()));
        return this;
    }

    public TemplateBuilder updateParam(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getUpdateParam()));
        return this;
    }

    public TemplateBuilder excelExportParam(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getExcelExportParam()));
        return this;
    }

    public TemplateBuilder excelImportParam(Consumer<TemplateFileBuilder> customizer) {
        customizer.accept(new TemplateFileBuilder(configurer.getTemplateConfig().getExcelImportParam()));
        return this;
    }
}