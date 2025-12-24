package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.common.TemplateRender;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class AbstractModelBuilder<C extends TemplateRender, B extends AbstractModelBuilder<C, B>> {
    protected C config;

    public AbstractModelBuilder(C render) {
        this.config = render;
    }

    @SuppressWarnings("unchecked")
    protected B self() {
        return (B) this;
    }

    /**
     * 是否生成
     *
     * @param generate 是否生成
     * @return this
     */
    public B generate(boolean generate) {
        this.config.getTemplateFile().setGenerate(false);
        return self();
    }

    /**
     * 名称格式化
     * <p>
     * 会使用{@link String#format(String, Object...)}进行转化, 其中会传入当前表转化后的名称,
     * 例如: 表名为user,  %sEntity  -> UserEntity
     *
     * @param nameFormat 格式
     * @return this
     */
    public B nameFormat(String nameFormat) {
        this.config.getTemplateFile().setNameFormat(nameFormat);
        return self();
    }

    /**
     * 模板文件子包名
     * <p>
     * 父包名通过全局配置
     *
     * @param subPackage 子包名
     * @return this
     */
    public B subPackage(String subPackage) {
        this.config.getTemplateFile().setSubPackage(subPackage);
        return self();
    }

    /**
     * 模板文件路径(从classpath开始的相对路径, 不带后缀)
     * <p>
     * /templates/entity.java.vm -> /templates/entity.java
     *
     * @param templatePath 模板文件路径
     * @return this
     */
    public B templatePath(String templatePath) {
        this.config.getTemplateFile().setTemplatePath(templatePath);
        return self();
    }

    /**
     * 输出文件路径(全路径)
     * <p>
     * 默认为: {global.outputDir}/{package}/{module}/{subPackage}
     *
     * @param outputDir 输出文件路径
     * @return this
     */
    public B outputDir(String outputDir) {
        this.config.getTemplateFile().setOutputDir(outputDir);
        return self();
    }

    /**
     * 生成时覆盖已存在的文件
     *
     * @param enable 是否启用
     * @return this
     */
    public B fileOverride(boolean enable) {
        this.config.getTemplateFile().setFileOverride(enable);
        return self();
    }
}
