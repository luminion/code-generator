package io.github.luminion.generator.config.builder.model;

import io.github.luminion.generator.config.Configurer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class MapperXmlBuilder {
    private final Configurer configurer;

    /**
     * 名称格式化
     * <p> 
     * 会使用{@link String#format(String, Object...)}进行转化, 其中会传入当前表转化后的名称,
     * 例如: 表名为user,  %sEntity  -> UserEntity
     * @param nameFormat 格式
     * @return this
     */
    public MapperXmlBuilder nameFormat(@NonNull String nameFormat) {
        this.configurer.getMapperXmlConfig().getTemplateFile().setNameFormat(nameFormat);
        return this;
    }

    /**
     * 模板文件子包名
     * <p> 
     * 父包名通过全局配置
     * @param subPackage 子包名
     * @return this
     */
    public MapperXmlBuilder subPackage(@NonNull String subPackage) {
        this.configurer.getMapperXmlConfig().getTemplateFile().setSubPackage(subPackage);
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
    public MapperXmlBuilder templatePath(@NonNull String templatePath) {
        this.configurer.getMapperXmlConfig().getTemplateFile().setTemplatePath(templatePath);
        return this;
    }

    /**
     * 输出文件路径(全路径)
     * <p> 
     * 默认为: {global.outputDir}/{package}/{module}/{subPackage}
     * @param outputDir 输出文件路径
     * @return this
     */
    public MapperXmlBuilder outputDir(@NonNull String outputDir) {
        this.configurer.getMapperXmlConfig().getTemplateFile().setOutputDir(outputDir);
        return this;
    }

    /**
     * 生成时覆盖已存在的文件
     *
     * @param enable 是否启用
     * @return this
     */
    public MapperXmlBuilder fileOverride(boolean enable) {
        this.configurer.getMapperXmlConfig().getTemplateFile().setFileOverride(enable);
        return this;
    }

    /**
     * 是否开启BaseResultMap
     *
     * @param enable 是否启用
     * @return this
     */
    public MapperXmlBuilder baseResultMap(boolean enable) {
        this.configurer.getMapperXmlConfig().setBaseResultMap(enable);
        return this;
    }

    /**
     * 是否开启baseColumnList
     *
     * @param enable 是否启用
     * @return this
     */
    public MapperXmlBuilder baseColumnList(boolean enable) {
        this.configurer.getMapperXmlConfig().setBaseColumnList(enable);
        return this;
    }

    /**
     * 设置缓存实现类
     *
     * @param cacheClass 缓存类, 该类需要是org.apache.ibatis.cache.Cache的子类
     * @return this
     */
    public MapperXmlBuilder cacheClass(@NonNull String cacheClass) {
        this.configurer.getMapperXmlConfig().setCacheClass(cacheClass);
        return this;
    }

    /**
     * 添加排序列
     *
     * @param column 排序列名
     * @param desc 是否倒序
     * @return this
     */
    public MapperXmlBuilder sortColumn(String column, boolean desc) {
        this.configurer.getMapperXmlConfig().getSortColumnMap().put(column, desc);
        return this;
    }

    /**
     * 清空排序列
     *
     * @return this
     */
    public MapperXmlBuilder sortColumnClear() {
        this.configurer.getMapperXmlConfig().getSortColumnMap().clear();
        return this;
    }
}
