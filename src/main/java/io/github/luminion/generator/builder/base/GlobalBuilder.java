package io.github.luminion.generator.builder.base;

import io.github.luminion.generator.config.base.GlobalConfig;
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
    private final GlobalConfig config;

    /**
     * lombok模型
     *
     * @param enable 是否启用
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder lombok(boolean enable) {
        this.config.setLombok(enable);
        return this;
    }

    /**
     * 链式setter
     *
     * @param enable 是否启用
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder chainModel(boolean enable) {
        this.config.setChainModel(enable);
        return this;
    }


    /**
     * 添加序列化UID
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder serializableUID(boolean enable) {
        this.config.setSerializableUID(enable);
        return this;
    }

    /**
     * 添加序列化UID的@serial注解, 需要jdk14+
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder serializableAnnotation(boolean enable) {
        this.config.setSerializableAnnotation(enable);
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
        this.config.setDocType(docType);
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
        this.config.setDocLink(enable);
        return this;
    }

    /**
     * 配置文档作者
     *
     * @param author 文档作者
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder author(@NonNull String author) {
        this.config.setAuthor(author);
        return this;
    }

    /**
     * 指定注释日期格式化
     *
     * @param pattern 格式,默认"yyyy-MM-dd"
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder date(@NonNull String pattern) {
        String format = new SimpleDateFormat(pattern).format(new Date());
        this.config.setDate(format);
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
        this.config.setJavaEEApi(javaEEApi);
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
        this.config.setExcelApi(excelApi);
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
        this.config.setOutputDir(outputDir);
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
        this.config.setOpenOutputDir(enable);
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
        this.config.setFileOverride(enable);
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
        this.config.setParentPackage(packageName);
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
        this.config.setParentPackageModule(parentPackageModule);
        return this;
    }

    /**
     * 生成参数校验相关注解
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder validated(boolean enable) {
        this.config.setValidated(enable);
        return this;
    }

    /**
     * 生成新增方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateCreate(boolean enable) {
        this.config.setGenerateCreate(enable);
        return this;
    }

    /**
     * 生成更新方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateUpdate(boolean enable) {
        this.config.setGenerateUpdate(enable);
        return this;
    }

    /**
     * 生成删除方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateDelete(boolean enable) {
        this.config.setGenerateDelete(enable);
        return this;
    }

    /**
     * 生成id查询方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateVoById(boolean enable) {
        this.config.setGenerateVoById(enable);
        return this;
    }

    /**
     * 生成列表查询方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateVoList(boolean enable) {
        this.config.setGenerateVoList(enable);
        return this;
    }

    /**
     * 生成分页查询方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateVoPage(boolean enable) {
        this.config.setGenerateVoPage(enable);
        return this;
    }

    /**
     * 生成导入方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateImport(boolean enable) {
        this.config.setGenerateImport(enable);
        return this;
    }

    /**
     * 生成导出方法
     *
     * @param enable 是否启用
     * @return this
     */
    public GlobalBuilder generateExport(boolean enable) {
        this.config.setGenerateExport(enable);
        return this;
    }

}
