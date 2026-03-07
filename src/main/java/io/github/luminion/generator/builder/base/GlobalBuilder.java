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
 * 全局配置构建器
 * <p>
 * 用于配置代码生成的全局选项，包括：
 * <ul>
 *   <li>代码生成的基本信息（作者、日期、包名等）</li>
 *   <li>实体模型配置（lombok、链式调用、序列化等）</li>
 *   <li>生成内容配置（是否生成增删改查等方法）</li>
 *   <li>输出配置（输出目录、是否覆盖等）</li>
 * </ul>
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class GlobalBuilder {
    private final GlobalConfig config;

    /**
     * 禁用lombok模型（默认启用）
     *
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder lombokDisable() {
        this.config.setLombok(false);
        return this;
    }

    /**
     * 启用链式setter（默认关闭）
     *
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder chainModelEnable() {
        this.config.setChainModel(true);
        return this;
    }


    /**
     * 添加序列化UID（默认关闭）
     *
     * @return this
     */
    public GlobalBuilder serializableUIDEnable() {
        this.config.setSerializableUID(true);
        return this;
    }

    /**
     * 禁用@serial注解（默认启用,需要jdk14+）
     *
     * @return this
     */
    public GlobalBuilder serializableAnnotationDisable() {
        this.config.setSerializableAnnotation(false);
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
     * 启用文档注释添加相关类链接（默认关闭）
     *
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder docLinkEnable() {
        this.config.setDocLink(true);
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
     * 启用打开输出目录（默认关闭）
     *
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder openOutputDirEnable() {
        this.config.setOpenOutputDir(true);
        return this;
    }

    /**
     * 启用覆盖已有文件(全局,为true时始终覆盖)（默认关闭）
     *
     * @return this
     * @since 1.0.0
     */
    public GlobalBuilder fileOverrideEnable() {
        this.config.setFileOverride(true);
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
     * 启用生成参数校验相关注解（默认关闭）
     *
     * @return this
     */
    public GlobalBuilder validatedEnable() {
        this.config.setValidated(true);
        return this;
    }

    /**
     * 禁用生成新增方法（默认启用）
     *
     * @return this
     */
    public GlobalBuilder generateCreateDisable() {
        this.config.setGenerateCreate(false);
        return this;
    }

    /**
     * 禁用生成更新方法（默认启用）
     *
     * @return this
     */
    public GlobalBuilder generateUpdateDisable() {
        this.config.setGenerateUpdate(false);
        return this;
    }

    /**
     * 禁用生成删除方法（默认启用）
     *
     * @return this
     */
    public GlobalBuilder generateDeleteDisable() {
        this.config.setGenerateDelete(false);
        return this;
    }

    /**
     * 禁用生成id查询方法（默认启用）
     *
     * @return this
     */
    public GlobalBuilder generateVoByIdDisable() {
        this.config.setGenerateVoById(false);
        return this;
    }

    /**
     * 禁用生成列表查询方法（默认启用）
     *
     * @return this
     */
    public GlobalBuilder generateVoListDisable() {
        this.config.setGenerateVoList(false);
        return this;
    }

    /**
     * 禁用生成分页查询方法（默认启用）
     *
     * @return this
     */
    public GlobalBuilder generateVoPageDisable() {
        this.config.setGenerateVoPage(false);
        return this;
    }

    /**
     * 禁用生成导入方法（默认启用）
     *
     * @return this
     */
    public GlobalBuilder generateImportDisable() {
        this.config.setGenerateImport(false);
        return this;
    }

    /**
     * 禁用生成导出方法（默认启用）
     *
     * @return this
     */
    public GlobalBuilder generateExportDisable() {
        this.config.setGenerateExport(false);
        return this;
    }

}
