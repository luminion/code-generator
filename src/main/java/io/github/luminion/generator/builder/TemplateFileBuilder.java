package io.github.luminion.generator.builder;

import io.github.luminion.generator.metadata.TemplateFile;
import io.github.luminion.generator.util.StringUtils;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class TemplateFileBuilder {
    private final TemplateFile templateFile;

    public TemplateFileBuilder() {
        this(new TemplateFile());
    }

    public TemplateFileBuilder key(String key) {
        templateFile.setKey(key);
        return this;
    }
    
    public TemplateFileBuilder disable() {
        templateFile.setGenerate(false);
        return this;
    }
    
    public TemplateFileBuilder nameFormat(String nameFormat) {
        templateFile.setNameFormat(nameFormat);
        return this;
    }
    
    public TemplateFileBuilder subPackage(String subPackage) {
        templateFile.setSubPackage(subPackage);
        return this;
    }
    
    public TemplateFileBuilder templatePath(String templatePath) {
        templateFile.setTemplatePath(templatePath);
        return this;
    }
    
    public TemplateFileBuilder outputFileSuffix(String outputFileSuffix) {
        templateFile.setOutputFileSuffix(outputFileSuffix);
        return this;
    }
    public TemplateFileBuilder enableFileOverride() {
        templateFile.setFileOverride(true);
        return this;
    }
    
    public TemplateFileBuilder fileOutputDir(String fileOutputDir) {
        templateFile.setFileOutputDir(fileOutputDir);
        return this;
    }

    public TemplateFile build() {
        if (StringUtils.isBlank(templateFile.getKey())) {
            throw new IllegalArgumentException("Template key cannot be empty");
        }
        templateFile.validate();
        return templateFile;
    }
}
