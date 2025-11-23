package io.github.luminion.generator.config.builder.model;

import io.github.luminion.generator.config.Configurer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class EntityExcelExportDTOBuilder {
    private final Configurer<?> configurer;

    /**
     * 名称格式化
     * <p> 
     * 会使用{@link String#format(String, Object...)}进行转化, 其中会传入当前表转化后的名称,
     * 例如: 表名为user,  %sEntity  -> UserEntity
     * @param nameFormat 格式
     * @return this
     */
    public EntityExcelExportDTOBuilder nameFormat(@NonNull String nameFormat) {
        this.configurer.getEntityExcelExportDTOConfig().getTemplateFile().setNameFormat(nameFormat);
        return this;
    }

    /**
     * 模板文件子包名
     * <p> 
     * 父包名通过全局配置
     * @param subPackage 子包名
     * @return this
     */
    public EntityExcelExportDTOBuilder subPackage(@NonNull String subPackage) {
        this.configurer.getEntityExcelExportDTOConfig().getTemplateFile().setSubPackage(subPackage);
        return this;
    }

    /**
     * 模板文件路径(从classpath开始的相对路径, 不带后缀)
     * <p> 
     * /templates/entity.java.vm -> /templates/entity.java
     *
     * @param templatePath 模板文件路径
     * @return this
     */
    public EntityExcelExportDTOBuilder templatePath(@NonNull String templatePath) {
        this.configurer.getEntityExcelExportDTOConfig().getTemplateFile().setTemplatePath(templatePath);
        return this;
    }

    /**
     * 输出文件路径(全路径)
     * <p> 
     * 默认为: {global.outputDir}/{package}/{module}/{subPackage}
     * @param outputDir 输出文件路径
     * @return this
     */
    public EntityExcelExportDTOBuilder outputDir(@NonNull String outputDir) {
        this.configurer.getEntityExcelExportDTOConfig().getTemplateFile().setOutputDir(outputDir);
        return this;
    }

    /**
     * 生成时覆盖已存在的文件
     *
     * @param enable 是否启用
     * @return this
     */
    public EntityExcelExportDTOBuilder fileOverride(boolean enable) {
        this.configurer.getEntityExcelExportDTOConfig().getTemplateFile().setFileOverride(enable);
        return this;
    }

}
