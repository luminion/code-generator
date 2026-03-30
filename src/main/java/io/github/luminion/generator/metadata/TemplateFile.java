package io.github.luminion.generator.metadata;

import lombok.Data;

import java.io.File;

/**
 * 对应的某个输出文件
 * @author luminion
 * @since 1.0.0
 */
@Data
public class TemplateFile {
    /**
     * 是否生成该文件
     */
    protected boolean generate = true;
    /**
     * 用于标识文件的key,不可重复
     */
    protected String key;
    /**
     * 格式化文件名称
     */
    protected String nameFormat;
    /**
     * 子包名(可空)
     */
    protected String subPackage;
    /**
     * 模板路径
     */
    protected String templatePath;
    /**
     * 输出文件后缀
     */
    protected String outputFileSuffix;
    /**
     * 文件覆盖
     */
    protected boolean fileOverride;
    /**
     * 输出文件路径
     */
    protected String fileOutputDir;
    /**
     * 跳过生成的原因
     */
    protected String skipReason;

    public TemplateFile() {
    }

    /**
     *
     * <pre>
     * TemplateFile templateFile = new TemplateFile(
     *             TemplateEnum.CREATE_DTO.getKey(),
     *             "%sCreateDTO",
     *             "model.dto",
     *             "/templates/model/createDTO.java",
     *             ".java"
     *     );
     * </pre>
     */
    public TemplateFile(String key, String nameFormat, String subPackage, String templatePath, String outputFileSuffix) {
        this.key = key;
        this.nameFormat = nameFormat;
        this.subPackage = subPackage;
        this.templatePath = templatePath;
        this.outputFileSuffix = outputFileSuffix;
    }

    /**
     * 根据entity名转化输出文件名称
     *
     * @param entityName entity名
     */
    public String convertFormatName(String entityName) {
        return String.format(nameFormat, entityName);
    }

    public String resolveFileName(String entityName) {
        return convertFormatName(entityName) + outputFileSuffix;
    }

    public File resolveOutputFile(String entityName) {
        return new File(fileOutputDir, resolveFileName(entityName));
    }

    public String getFileNamePattern() {
        return nameFormat;
    }

    public void setFileNamePattern(String fileNamePattern) {
        this.nameFormat = fileNamePattern;
    }

    public String getOutputDirectory() {
        return fileOutputDir;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.fileOutputDir = outputDirectory;
    }

    /**
     * 模板引擎输出之前, 验证输出文件信息
     */
    public void validate() {
        if (templatePath == null) {
            throw new IllegalArgumentException("Template path cannot be empty");
        }
        if (nameFormat == null) {
            throw new IllegalArgumentException("File name formatting function cannot be empty");
        }
        if (outputFileSuffix == null) {
            throw new IllegalArgumentException("File name suffix cannot be empty");
        }
    }
}
