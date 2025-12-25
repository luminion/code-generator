package io.github.luminion.generator.po;

import lombok.Data;

/**
 * 对应的某个输出文件
 * @author luminion
 * @since 1.0.0
 */
@Data
public class TemplateFile {
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
     * 输出文件路径
     */
    protected String outputDir;
    /**
     * 文件覆盖
     */
    protected boolean fileOverride;
    /**
     * 是否生成
     */
    protected boolean generate = true;

    /**
     * 

     * <pre>
     * TemplateFile templateFile = new TemplateFile(
     *             TemplateFileEnum.CREATE_DTO.getKey(),
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
     * 根据表信息转化输出文件名称
     *
     * @param tableInfo 表信息
     */
    public String convertFormatName(TableInfo tableInfo) {
        return String.format(nameFormat, tableInfo.getEntityName());
    }

    /**
     * 模板引擎输出之前, 验证输出文件信息
     */
    public void beforeOutputValidate() {
        if (templatePath == null) {
            throw new IllegalArgumentException("Template path cannot be empty");
        }
        if (outputDir == null) {
            throw new IllegalArgumentException("File outputDir cannot be empty");
        }
        if (nameFormat == null) {
            throw new IllegalArgumentException("File name formatting function cannot be empty");
        }
        if (outputFileSuffix == null) {
            throw new IllegalArgumentException("File name suffix cannot be empty");
        }
    }
    
}
