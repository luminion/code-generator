package io.github.luminion.generator.builder;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.DocType;
import io.github.luminion.generator.enums.JavaEEApi;
import io.github.luminion.generator.metadata.TableInfo;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 全局配置构建器
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class GlobalBuilder {
    private final Configurer configurer;

    /**
     * 自定义渲染数据
     *
     * @return 全局配置构建器
     */
    public GlobalBuilder customRenderData(Map<String, Object> renderData) {
        configurer.getGlobalConfig().setCustomRenderData(renderData);
        return this;
    }
    
    /**
     * 自定义渲染逻辑(输出之前修改数据)
     *
     * @param customRenderLogic 自定义渲染逻辑
     * @return 全局配置构建器
     */
    public GlobalBuilder customRenderLogic(BiConsumer<TableInfo, Map<String, Object>> customRenderLogic) {
        configurer.getGlobalConfig().setCustomRenderLogic(customRenderLogic);
        return this;
    }

    /**
     * 配置文档类型
     *
     * @param docType 文档类型
     * @return this
     */
    public GlobalBuilder docType(DocType docType) {
        configurer.getGlobalConfig().setDocType(docType);
        return this;
    }

    /**
     * 关闭文档注释添加相关类链接
     *
     * @return this
     */
    public GlobalBuilder disableJavadocWithSeeTags() {
        configurer.getGlobalConfig().setJavadocWithSeeTags(true);
        return this;
    }

    /**
     * 配置文档作者
     *
     * @param author 文档作者
     * @return this
     */
    public GlobalBuilder author(String author) {
        configurer.getGlobalConfig().setAuthor(author);
        return this;
    }

    /**
     * 指定注释日期格式化
     *
     * @param pattern 格式,默认"yyyy-MM-dd"
     * @return this
     */
    public GlobalBuilder date(String pattern) {
        String format = new SimpleDateFormat(pattern).format(new Date());
        configurer.getGlobalConfig().setDate(format);
        return this;
    }
    
    /**
     * 配置java EE框架
     *
     * @param javaEEApi java EE框架
     * @return this
     */
    public GlobalBuilder javaEEApi(JavaEEApi javaEEApi) {
        configurer.getGlobalConfig().setJavaEEApi(javaEEApi);
        return this;
    }
    
    /**
     * 禁用lombok模型
     *
     * @return this
     */
    public GlobalBuilder disableLombok() {
        configurer.getGlobalConfig().setLombok(false);
        return this;
    }

    /**
     * 启用toString方法
     *
     * @return this
     */
    public GlobalBuilder enableToString() {
        configurer.getGlobalConfig().setLombok(false);
        return this;
    }

    /**
     * 启用链式setter
     *
     * @return this
     */
    public GlobalBuilder enableChainSetter() {
        configurer.getGlobalConfig().setChainSetter(true);
        return this;
    }

    /**
     * 禁止实现Serializable接口
     *
     * @return this
     */
    public GlobalBuilder disableSerializable() {
        configurer.getGlobalConfig().setSerializable(false);
        return this;
    }

    /**
     * 禁用@serial注解（需要jdk14+）
     *
     * @return this
     */
    public GlobalBuilder disableSerializableAnnotation() {
        configurer.getGlobalConfig().setSerializableAnnotation(false);
        return this;
    }

    /**
     * 禁用生成新增方法
     *
     * @return this
     */
    public GlobalBuilder disableGenerateCreate() {
        configurer.getGlobalConfig().setGenerateCreate(false);
        return this;
    }

    /**
     * 禁用生成更新方法
     *
     * @return this
     */
    public GlobalBuilder disableGenerateUpdate() {
        configurer.getGlobalConfig().setGenerateUpdate(false);
        return this;
    }

    /**
     * 禁用生成删除方法
     *
     * @return this
     */
    public GlobalBuilder disableGenerateDelete() {
        configurer.getGlobalConfig().setGenerateDelete(false);
        return this;
    }

    /**
     * 禁用生成id查询方法
     *
     * @return this
     */
    public GlobalBuilder disableGenerateQueryById() {
        configurer.getGlobalConfig().setGenerateQueryById(false);
        return this;
    }

    /**
     * 禁用生成列表查询方法
     *
     * @return this
     */
    public GlobalBuilder disableGenerateQueryList() {
        configurer.getGlobalConfig().setGenerateQueryList(false);
        return this;
    }

    /**
     * 禁用生成分页查询方法
     *
     * @return this
     */
    public GlobalBuilder disableGenerateQueryPage() {
        configurer.getGlobalConfig().setGenerateQueryPage(false);
        return this;
    }

    /**
     * 禁用生成导入方法
     *
     * @return this
     */
    public GlobalBuilder disableGenerateExcelImport() {
        configurer.getGlobalConfig().setGenerateExcelImport(false);
        return this;
    }

    /**
     * 禁用生成导出方法
     *
     * @return this
     */
    public GlobalBuilder disableGenerateExcelExport() {
        configurer.getGlobalConfig().setGenerateExcelExport(false);
        return this;
    }
}
