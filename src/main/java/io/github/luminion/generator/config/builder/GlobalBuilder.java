package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.config.base.GlobalConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author luminion
 * @since 1.0.0
 */
public class GlobalBuilder {

    private final GlobalConfig globalConfig;

    public GlobalBuilder(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    /**
     * 作者
     */
    public GlobalBuilder author(String author) {
        this.globalConfig.setAuthor(author);
        return this;
    }



    /**
     * 指定注释日期格式化
     *
     * @param pattern 格式
     * @return this
     * @since 3.5.0
     */
    public GlobalBuilder commentDatePattern(String pattern) {
        String format = new SimpleDateFormat(pattern).format(new Date());
        this.globalConfig.setCommentDate(format);
        return this;
    }

    /**
     * 开启lombok模型
     *
     * @return this
     * @since 3.5.0
     */
    public GlobalBuilder enableLombok() {
        this.globalConfig.setLombok(true);
        return this;
    }

    /**
     * 禁用lombok模型
     *
     * @return this
     * @deprecated 默认值,无需设置
     */
    @Deprecated
    public GlobalBuilder disableLombok() {
        this.globalConfig.setLombok(false);
        return this;
    }

    /**
     * 开启链式getter和setter
     *
     * @return this
     * @since 3.5.0
     */
    public GlobalBuilder enableChainModel() {
        this.globalConfig.setChainModel(true);
        return this;
    }

    /**
     * 文档注释添加相关类链接
     *
     * @return this
     */
    public GlobalBuilder enableCommentLink() {
        this.globalConfig.setCommentLink(true);
        return this;
    }

    /**
     * 禁用文档注释添加相关类链接
     *
     * @return this
     * @deprecated 默认值,无需设置
     */
    @Deprecated
    public GlobalBuilder disableCommentLink() {
        this.globalConfig.setCommentLink(false);
        return this;
    }

    /**
     * 启用类注释随机UUID
     *
     * @return this
     */
    public GlobalBuilder enableCommentUUID() {
        this.globalConfig.setCommentUUID(true);
        return this;
    }

    /**
     * 禁用新增和修改的入参校验
     *
     * @return this
     */
    public GlobalBuilder disableValidated() {
        this.globalConfig.setValidated(false);
        return this;
    }

    /**
     * 使用swagger文档
     */
    public GlobalBuilder enableSwagger() {
        this.globalConfig.setSwagger(true);
        return this;
    }

    /**
     * 使用springdoc文档
     */
    public GlobalBuilder enableSpringdoc() {
        this.globalConfig.setSpringdoc(true);
        return this;
    }

    /**
     * 使用javax包作为javaEE api
     * <p>springboot2.x使用javax, springboot3.x使用jakarta</p>
     * 默认使用jakarta
     *
     * @return this
     */
    public GlobalBuilder enableJavaxApi() {
        this.globalConfig.setJakartaApiPackagePrefix("javax");
        return this;
    }

    /**
     * 使用EasyExcel
     * <p>默认使用FastExcel</p>
     *
     * @return this
     */
    public GlobalBuilder enableEasyExcel() {
        this.globalConfig.setExcelApiPackagePrefix("com.alibaba.excel");
        this.globalConfig.setExcelApiClass("EasyExcel");
        return this;
    }
    

    /**
     * 不生成查询方法
     *
     * @return this
     */
    public GlobalBuilder disableQuery() {
        this.globalConfig.setGenerateQuery(false);
        return this;
    }

    /**
     * 不生成新增方法
     *
     * @return this
     */
    public GlobalBuilder disableInsert() {
        this.globalConfig.setGenerateInsert(false);
        return this;
    }

    /**
     * 不生成更新方法
     *
     * @return this
     */
    public GlobalBuilder disableUpdate() {
        this.globalConfig.setGenerateUpdate(false);
        return this;
    }

    /**
     * 不生成删除方法
     *
     * @return this
     */
    public GlobalBuilder disableDelete() {
        this.globalConfig.setGenerateDelete(false);
        return this;
    }

    /**
     * 不生成导入方法
     *
     * @return this
     */
    public GlobalBuilder disableImport() {
        this.globalConfig.setGenerateImport(false);
        return this;
    }

    /**
     * 不生成导出方法
     *
     * @return this
     */
    public GlobalBuilder disableExport() {
        this.globalConfig.setGenerateExport(false);
        return this;
    }
    
}
