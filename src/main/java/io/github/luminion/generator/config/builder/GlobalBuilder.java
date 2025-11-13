package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.config.Configurer;
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
     * 作者
     */
    public GlobalBuilder author(String author) {
        this.configurer.getGlobalConfig().setAuthor(author);
        return this;
    }

    /**
     * 指定注释日期格式化
     *
     * @param pattern 格式
     * @return this
     */
    public GlobalBuilder commentDatePattern(String pattern) {
        String format = new SimpleDateFormat(pattern).format(new Date());
        this.configurer.getGlobalConfig().setCommentDate(format);
        return this;
    }

    /**
     * 开启lombok模型
     *
     * @return this
     * @since 3.5.0
     */
    public GlobalBuilder enableLombok() {
        this.configurer.getGlobalConfig().setLombok(true);
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
        this.configurer.getGlobalConfig().setLombok(false);
        return this;
    }

    /**
     * 开启链式getter和setter
     *
     * @return this
     * @since 3.5.0
     */
    public GlobalBuilder enableChainModel() {
        this.configurer.getGlobalConfig().setChainModel(true);
        return this;
    }

    /**
     * 文档注释添加相关类链接
     *
     * @return this
     */
    public GlobalBuilder enableCommentLink() {
        this.configurer.getGlobalConfig().setCommentLink(true);
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
        this.configurer.getGlobalConfig().setCommentLink(false);
        return this;
    }

//    /**
//     * 启用类注释随机UUID
//     *
//     * @return this
//     */
//    public GlobalBuilder enableCommentUUID() {
//        this.configurer.getGlobalConfig().setCommentUUID(true);
//        return this;
//    }

    /**
     * 禁用新增和修改的入参校验
     *
     * @return this
     */
    public GlobalBuilder disableValidated() {
        this.configurer.getGlobalConfig().setValidated(false);
        return this;
    }

    /**
     * 使用swagger文档
     */
    public GlobalBuilder enableSwagger() {
        this.configurer.getGlobalConfig().setSwagger(true);
        return this;
    }

    /**
     * 使用springdoc文档
     */
    public GlobalBuilder enableSpringdoc() {
        this.configurer.getGlobalConfig().setSpringdoc(true);
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
        this.configurer.getGlobalConfig().setJakartaApiPackagePrefix("javax");
        return this;
    }

    /**
     * 使用EasyExcel
     * <p>默认使用FastExcel</p>
     *
     * @return this
     */
    public GlobalBuilder enableEasyExcel() {
        this.configurer.getGlobalConfig().setExcelApiPackagePrefix("com.alibaba.excel");
        this.configurer.getGlobalConfig().setExcelApiClass("EasyExcel");
        return this;
    }


    /**
     * 不生成查询方法
     *
     * @return this
     */
    public GlobalBuilder disableQuery() {
        this.configurer.getGlobalConfig().setGenerateQuery(false);
        return this;
    }

    /**
     * 不生成新增方法
     *
     * @return this
     */
    public GlobalBuilder disableInsert() {
        this.configurer.getGlobalConfig().setGenerateInsert(false);
        return this;
    }

    /**
     * 不生成更新方法
     *
     * @return this
     */
    public GlobalBuilder disableUpdate() {
        this.configurer.getGlobalConfig().setGenerateUpdate(false);
        return this;
    }

    /**
     * 不生成删除方法
     *
     * @return this
     */
    public GlobalBuilder disableDelete() {
        this.configurer.getGlobalConfig().setGenerateDelete(false);
        return this;
    }

    /**
     * 不生成导入方法
     *
     * @return this
     */
    public GlobalBuilder disableImport() {
        this.configurer.getGlobalConfig().setGenerateImport(false);
        return this;
    }

    /**
     * 不生成导出方法
     *
     * @return this
     */
    public GlobalBuilder disableExport() {
        this.configurer.getGlobalConfig().setGenerateExport(false);
        return this;
    }

}
