package io.github.luminion.generator.config.support.adapter;

import io.github.luminion.generator.config.rules.DateType;
import io.github.luminion.generator.config.support.GlobalConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author luminion
 * @since 1.0.0
 */
public class GlobalAdapter {

    private final GlobalConfig globalConfig;

    public GlobalAdapter(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    /**
     * 作者
     */
    public GlobalAdapter author(String author) {
        this.globalConfig.setAuthor(author);
        return this;
    }

    /**
     * 时间类型对应策略
     */
    public GlobalAdapter dateType(DateType dateType) {
        // todo 迁移到数据库设置
        this.globalConfig.setDateType(dateType);
        return this;
    }

    /**
     * 指定注释日期格式化
     *
     * @param pattern 格式
     * @return this
     * @since 3.5.0
     */
    public GlobalAdapter commentDatePattern(String pattern) {
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
    public GlobalAdapter enableLombok() {
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
    public GlobalAdapter disableLombok() {
        this.globalConfig.setLombok(false);
        return this;
    }

    /**
     * 开启链式getter和setter
     *
     * @return this
     * @since 3.5.0
     */
    public GlobalAdapter enableChainModel() {
        this.globalConfig.setChainModel(true);
        return this;
    }

    /**
     * 文档注释添加相关类链接
     *
     * @return this
     */
    public GlobalAdapter enableCommentLink() {
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
    public GlobalAdapter disableCommentLink() {
        this.globalConfig.setCommentLink(false);
        return this;
    }

    /**
     * 启用类注释随机UUID
     *
     * @return this
     */
    public GlobalAdapter enableCommentUUID() {
        this.globalConfig.setCommentUUID(true);
        return this;
    }

    /**
     * 禁用新增和修改的入参校验
     *
     * @return this
     */
    public GlobalAdapter disableValidated() {
        this.globalConfig.setValidated(false);
        return this;
    }

    /**
     * 使用swagger文档
     */
    public GlobalAdapter enableSwagger() {
        this.globalConfig.setSwagger(true);
        return this;
    }

    /**
     * 使用springdoc文档
     */
    public GlobalAdapter enableSpringdoc() {
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
    public GlobalAdapter enableJavaxApi() {
        this.globalConfig.setJakartaApiPackagePrefix("javax");
        return this;
    }

    /**
     * 使用EasyExcel
     * <p>默认使用FastExcel</p>
     *
     * @return this
     */
    public GlobalAdapter enableEasyExcel() {
        this.globalConfig.setExcelApiPackagePrefix("com.alibaba.excel");
        this.globalConfig.setExcelApiClass("EasyExcel");
        return this;
    }
    

    /**
     * 不生成查询方法
     *
     * @return this
     */
    public GlobalAdapter disableQuery() {
        this.globalConfig.setGenerateQuery(false);
        return this;
    }

    /**
     * 不生成新增方法
     *
     * @return this
     */
    public GlobalAdapter disableInsert() {
        this.globalConfig.setGenerateInsert(false);
        return this;
    }

    /**
     * 不生成更新方法
     *
     * @return this
     */
    public GlobalAdapter disableUpdate() {
        this.globalConfig.setGenerateUpdate(false);
        return this;
    }

    /**
     * 不生成删除方法
     *
     * @return this
     */
    public GlobalAdapter disableDelete() {
        this.globalConfig.setGenerateDelete(false);
        return this;
    }

    /**
     * 不生成导入方法
     *
     * @return this
     */
    public GlobalAdapter disableImport() {
        this.globalConfig.setGenerateImport(false);
        return this;
    }

    /**
     * 不生成导出方法
     *
     * @return this
     */
    public GlobalAdapter disableExport() {
        this.globalConfig.setGenerateExport(false);
        return this;
    }
    
}
