package io.github.luminion.generator.metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对应的某个输出文件
 *
 * @author luminion
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateClassFile extends TemplateFile {
    /**
     * 类包名
     */
    protected String packageName;
    /**
     * 类名
     */
    protected String classSimpleName;
    /**
     * 类全名
     */
    protected String classCanonicalName;

    public TemplateClassFile(TemplateFile templateFile) {
        super(templateFile.getKey(),
                templateFile.getNameFormat(),
                templateFile.getSubPackage(),
                templateFile.getTemplatePath(),
                templateFile.getOutputFileSuffix());
        this.setGenerate(templateFile.isGenerate());
        this.setFileOverride(templateFile.isFileOverride());
        this.setFileOutputDir(templateFile.getFileOutputDir());
        this.setSkipReason(templateFile.getSkipReason());
    }

    @Override
    public String toString() {
        return classSimpleName;
    }

    public String getSimpleClassName() {
        return classSimpleName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.classSimpleName = simpleClassName;
    }

    public String getFullyQualifiedClassName() {
        return classCanonicalName;
    }

    public void setFullyQualifiedClassName(String fullyQualifiedClassName) {
        this.classCanonicalName = fullyQualifiedClassName;
    }
}