package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.RenderField;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.DocType;
import io.github.luminion.generator.enums.JavaEEApi;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;


/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class GlobalConfig implements TemplateRender {

    private final Configurer configurer;
    /**
     * 自定义配置渲染数据
     */
    private Map<String, Object> customRenderData = new HashMap<>();

    /**
     * 自定义渲染逻辑
     */
    private BiConsumer<TableInfo, Map<String, Object>> customRenderLogic;

    /**
     * 文档注释类型
     */
    private DocType docType = DocType.JAVA_DOC;
    /**
     * 文档注释添加相关类链接
     */
    @RenderField
    private boolean docLink = true;
    /**
     * 作者
     */
    @RenderField
    private String docAuthor = "luminion";
    /**
     * 注释日期
     */
    @RenderField
    private String docDate;

    /**
     * java ee api
     */
    private JavaEEApi javaEEApi = JavaEEApi.JAKARTA;

    /**
     * 外部运行环境
     */
    private RuntimeEnv runtimeEnv = RuntimeEnv.MYBATIS_PLUS;

    /**
     * 是否为lombok模型
     */
    @RenderField
    private boolean lombok = true;

    /**
     * 生成toString方法
     */
    @RenderField
    private boolean toString;

    /**
     * 是否为链式模型setter
     */
    @RenderField
    private boolean chainSetter;

    /**
     * 实体是否实现 serializable
     */
    @RenderField
    private boolean serializable = true;

    /**
     * serializable的实体是否启用java.io.Serial (需JAVA 14) 注解
     */
    @RenderField
    private boolean serializableAnnotation = true;
    
    /**
     * 数据库schema名
     */
    @RenderField
    private String schemaName;

    /**
     * 生成新增方法及配套类
     */
    @RenderField
    private boolean generateCreate = true;

    /**
     * 生成更新方法及配套
     */
    @RenderField
    private boolean generateUpdate = true;

    /**
     * 生成删除方法及配套类
     */
    @RenderField
    private boolean generateDelete = true;

    /**
     * 生成id查询及配套类
     */
    @RenderField
    private boolean generateQueryById = true;

    /**
     * 生成列表查询及配套类
     */
    @RenderField
    private boolean generateQueryList = true;

    /**
     * 生成分页查询及配套类
     */
    @RenderField
    private boolean generateQueryPage = true;

    /**
     * 生成导入方法及配套类
     */
    @RenderField
    private boolean generateExcelImport = true;

    /**
     * 生成导出方法
     */
    @RenderField
    private boolean generateExcelExport = true;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        data.put("javaApiPackagePrefix", javaEEApi.getPackagePrefix());
        if (RuntimeEnv.MP_BOOSTER.equals(runtimeEnv)){
            data.put("mpBooster", true);
        }
        if (DocType.SPRING_DOC.equals(docType)){
            data.put("springDoc", true);
        }
        if (DocType.SWAGGER.equals(docType)){
            data.put("swagger", true);
        }
        return data;
    }


    public Set<String> getModelDocImportPackages() {
        Set<String> importPackages = new TreeSet<>();
        switch (docType) {
            case SPRING_DOC:
                importPackages.add(RuntimeClass.SWAGGER_V3_SCHEMA.getClassName());
                break;
            case SWAGGER:
                importPackages.add(RuntimeClass.SWAGGER_V2_API_MODEL.getClassName());
                importPackages.add(RuntimeClass.SWAGGER_V2_API_MODEL_PROPERTY.getClassName());
                break;
        }
        return importPackages;
    }

    /**
     * 获取领域类需要导入的包
     *
     * @return 包
     */
    public Set<String> getModelImportPackages() {
        Set<String> importPackages = new TreeSet<>();
        if (lombok) {
            if (chainSetter) {
                importPackages.add(RuntimeClass.LOMBOK_ACCESSORS.getClassName());
            }
            if (toString){
                importPackages.add(RuntimeClass.LOMBOK_TO_STRING.getClassName());
            }
            importPackages.add(RuntimeClass.LOMBOK_GETTER.getClassName());
            importPackages.add(RuntimeClass.LOMBOK_SETTER.getClassName());
        }
        if (serializable) {
            importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
            if (serializableAnnotation) {
                importPackages.add(RuntimeClass.JAVA_IO_SERIAL.getClassName());
            }
        }
        return importPackages;
    }
    
}