package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.DocType;
import io.github.luminion.generator.enums.ExcelApi;
import io.github.luminion.generator.enums.JavaEEApi;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class GlobalBuilder {
    private final Configurer configurer;

    /**
     * lombok模型
     *
     * @param enable 是否启用
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder lombok(boolean enable) {
        this.configurer.getGlobalConfig().setLombok(enable);
        return this;
    }

    /**
     * lombok链式getter和setter(需启用lombok)
     *
     * @param enable 是否启用
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder lombokChainModel(boolean enable) {
        this.configurer.getGlobalConfig().setLombokChainModel(enable);
        return this;
    }


    /**
     * 配置文档类型
     *
     * @param docType 文档类型
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder docType(@NonNull DocType docType) {
        this.configurer.getGlobalConfig().setDocType(docType);
        return this;
    }

    /**
     * 配置文档作者
     *
     * @param author 文档作者
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder docAuthor(@NonNull String author) {
        this.configurer.getGlobalConfig().setDocAuthor(author);
        return this;
    }

    /**
     * 指定注释日期格式化
     *
     * @param pattern 格式,默认"yyyy-MM-dd"
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder docDate(@NonNull String pattern) {
        String format = new SimpleDateFormat(pattern).format(new Date());
        this.configurer.getGlobalConfig().setDocDate(format);
        return this;
    }


    /**
     * 文档注释添加相关类链接
     *
     * @param enable 是否启用
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder docLink(boolean enable) {
        this.configurer.getGlobalConfig().setDocLink(enable);
        return this;
    }

    /**
     * java EE 框架
     *
     * @param javaEEApi javaEE 框架
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder javaEEApi(@NonNull JavaEEApi javaEEApi) {
        this.configurer.getGlobalConfig().setJavaEEApi(javaEEApi);
        return this;
    }

    /**
     * excel框架
     *
     * @param excelApi excel框架
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder excelApi(@NonNull ExcelApi excelApi) {
        this.configurer.getGlobalConfig().setExcelApi(excelApi);
        return this;
    }


    /**
     * 输出根目录
     *
     * @param outputDir 输出目录,全路径
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder outputDir(@NonNull String outputDir) {
        this.configurer.getGlobalConfig().setOutputDir(outputDir);
        return this;
    }

    /**
     * 是否打开输出目录
     *
     * @param enable 是否启用
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder openOutputDir(boolean enable) {
        this.configurer.getGlobalConfig().setOpenOutputDir(enable);
        return this;
    }

    /**
     * 是否覆盖已有文件(全局,为true时始终覆盖)
     *
     * @param enable 是否启用
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder fileOverride(boolean enable) {
        this.configurer.getGlobalConfig().setFileOverride(enable);
        return this;
    }

    /**
     * 设置父包名
     *
     * @param packageName 父包名
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder parentPackage(@NonNull String packageName) {
        this.configurer.getGlobalConfig().setParentPackage(packageName);
        return this;
    }

    /**
     * 父包模块名
     *
     * @param parentPackageModule 父包模块名
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder parentPackageModule(@NonNull String parentPackageModule) {
        this.configurer.getGlobalConfig().setParentPackage(parentPackageModule);
        return this;
    }
    
    /**
     * 生成参数校验相关注解
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder validated(boolean enable) {
        this.configurer.getGlobalConfig().setValidated(enable);
        return this;
    }

    /**
     * 生成查询方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateQuery(boolean enable) {
        this.configurer.getGlobalConfig().setGenerateQuery(enable);
        return this;
    }

    /**
     * 生成新增方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateInsert(boolean enable) {
        this.configurer.getGlobalConfig().setGenerateInsert(enable);
        return this;
    }

    /**
     * 生成更新方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateUpdate(boolean enable) {
        this.configurer.getGlobalConfig().setGenerateUpdate(enable);
        return this;
    }

    /**
     * 生成删除方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateDelete(boolean enable) {
        this.configurer.getGlobalConfig().setGenerateDelete(enable);
        return this;
    }

    /**
     * 生成导入方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateImport(boolean enable) {
        this.configurer.getGlobalConfig().setGenerateImport(enable);
        return this;
    }

    /**
     * 生成导出方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateExport(boolean enable) {
        this.configurer.getGlobalConfig().setGenerateExport(enable);
        return this;
    }

}
