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
public class MapperBuilder {
    private final Configurer<?> configurer;

    /**
     * 名称格式化
     * <p> 
     * 会使用{@link String#format(String, Object...)}进行转化, 其中会传入当前表转化后的名称,
     * 例如: 表名为user,  %sEntity  -> UserEntity
     * @param nameFormat 格式
     * @return this
     */
    public MapperBuilder nameFormat(@NonNull String nameFormat) {
        this.configurer.getMapperConfig().getTemplateFile().setNameFormat(nameFormat);
        return this;
    }

    /**
     * 模板文件子包名
     * <p> 
     * 父包名通过全局配置
     * @param subPackage 子包名
     * @return this
     */
    public MapperBuilder subPackage(@NonNull String subPackage) {
        this.configurer.getMapperConfig().getTemplateFile().setSubPackage(subPackage);
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
    public MapperBuilder templatePath(@NonNull String templatePath) {
        this.configurer.getMapperConfig().getTemplateFile().setTemplatePath(templatePath);
        return this;
    }

    /**
     * 输出文件路径(全路径)
     * <p> 
     * 默认为: {global.outputDir}/{package}/{module}/{subPackage}
     * @param outputDir 输出文件路径
     * @return this
     */
    public MapperBuilder outputDir(@NonNull String outputDir) {
        this.configurer.getMapperConfig().getTemplateFile().setOutputDir(outputDir);
        return this;
    }

    /**
     * 生成时覆盖已存在的文件
     *
     * @param enable 是否启用
     * @return this
     */
    public MapperBuilder fileOverride(boolean enable) {
        this.configurer.getMapperConfig().getTemplateFile().setFileOverride(enable);
        return this;
    }

    /**
     * 自定义继承的Mapper类全称，带包名
     *
     * @param superClass 父类
     * @return this
     */
    public MapperBuilder superClass(@NonNull String superClass) {
        this.configurer.getMapperConfig().setSuperClass(superClass);
        return this;
    }

    /**
     * Mapper标记注解
     *
     * @param mapperAnnotationClass 注解类
     * @return this
     */
    public MapperBuilder mapperAnnotationClass(@NonNull String mapperAnnotationClass) {
        this.configurer.getMapperConfig().setMapperAnnotationClass(mapperAnnotationClass);
        return this;
    }
}
