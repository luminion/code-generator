package io.github.luminion.generator.config.model.builder;

import io.github.luminion.generator.config.Configurer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EntityQueryDTOBuilder {
    private final Configurer configurer;

    /**
     * 名称格式化
     * <p> 
     * 会使用{@link String#format(String, Object...)}进行转化, 其中会传入当前表转化后的名称,
     * 例如: 表名为user,  %sEntity  -> UserEntity
     * @param nameFormat 格式
     * @return this
     */
    public EntityQueryDTOBuilder nameFormat(@NonNull String nameFormat) {
        this.configurer.getEntityQueryDTOConfig().getTemplateFile().setNameFormat(nameFormat);
        return this;
    }

    /**
     * 模板文件子包名
     * <p> 
     * 父包名通过全局配置
     * @param subPackage 子包名
     * @return this
     */
    public EntityQueryDTOBuilder subPackage(@NonNull String subPackage) {
        this.configurer.getEntityQueryDTOConfig().getTemplateFile().setSubPackage(subPackage);
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
    public EntityQueryDTOBuilder templatePath(@NonNull String templatePath) {
        this.configurer.getEntityQueryDTOConfig().getTemplateFile().setTemplatePath(templatePath);
        return this;
    }

    /**
     * 输出文件路径(全路径)
     * <p> 
     * 默认为: {global.outputDir}/{package}/{module}/{subPackage}
     * @param outputDir 输出文件路径
     * @return this
     */
    public EntityQueryDTOBuilder outputDir(@NonNull String outputDir) {
        this.configurer.getEntityQueryDTOConfig().getTemplateFile().setOutputDir(outputDir);
        return this;
    }

    /**
     * 生成时覆盖已存在的文件
     *
     * @param enable 是否启用
     * @return this
     */
    public EntityQueryDTOBuilder fileOverride(boolean enable) {
        this.configurer.getEntityQueryDTOConfig().getTemplateFile().setFileOverride(enable);
        return this;
    }

    /**
     * 是否继承实体类
     *
     * @param enable 是否启用
     * @return this
     */
    public EntityQueryDTOBuilder extendsEntity(boolean enable) {
        this.configurer.getEntityQueryDTOConfig().setExtendsEntity(enable);
        return this;
    }

}
