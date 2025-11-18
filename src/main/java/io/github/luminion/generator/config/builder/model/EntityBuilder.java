package io.github.luminion.generator.config.builder.model;

import io.github.luminion.generator.config.Configurer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class EntityBuilder {
    private final Configurer configurer;

    /**
     * 名称格式化
     * <p> 
     * 会使用{@link String#format(String, Object...)}进行转化, 其中会传入当前表转化后的名称,
     * 例如: 表名为user,  %sEntity  -> UserEntity
     * @param nameFormat 格式
     * @return this
     */
    public EntityBuilder nameFormat(@NonNull String nameFormat) {
        this.configurer.getEntityConfig().getTemplateFile().setNameFormat(nameFormat);
        return this;
    }

    /**
     * 模板文件子包名
     * <p> 
     * 父包名通过全局配置
     * @param subPackage 子包名
     * @return this
     */
    public EntityBuilder subPackage(@NonNull String subPackage) {
        this.configurer.getEntityConfig().getTemplateFile().setSubPackage(subPackage);
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
    public EntityBuilder templatePath(@NonNull String templatePath) {
        this.configurer.getEntityConfig().getTemplateFile().setTemplatePath(templatePath);
        return this;
    }

    /**
     * 输出文件路径(全路径)
     * <p> 
     * 默认为: {global.outputDir}/{package}/{module}/{subPackage}
     * @param outputDir 输出文件路径
     * @return this
     */
    public EntityBuilder outputDir(@NonNull String outputDir) {
        this.configurer.getEntityConfig().getTemplateFile().setOutputDir(outputDir);
        return this;
    }

    /**
     * 生成时覆盖已存在的文件
     *
     * @param enable 是否启用
     * @return this
     */
    public EntityBuilder fileOverride(boolean enable) {
        this.configurer.getEntityConfig().getTemplateFile().setFileOverride(enable);
        return this;
    }

    /**
     * 自定义继承的Entity类全称，带包名
     *
     * @param superClass 父类
     * @return this
     */
    public EntityBuilder superClass(@NonNull String superClass) {
        this.configurer.getEntityConfig().setSuperClass(superClass);
        return this;
    }

//    /**
//     * 开启 ActiveRecord 模式
//     *
//     * @param enable 是否启用
//     * @return this
//     */
//    public EntityBuilder activeRecord(boolean enable) {
//        this.configurer.getEntityConfig().setActiveRecord(enable);
//        return this;
//    }

//    /**
//     * 是否生成实体时，生成字段注解
//     *
//     * @param enable 是否启用
//     * @return this
//     */
//    public EntityBuilder tableFieldAnnotation(boolean enable) {
//        this.configurer.getEntityConfig().setTableFieldAnnotation(enable);
//        return this;
//    }
}
