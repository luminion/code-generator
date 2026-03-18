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

import java.time.LocalDate;
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
     * todo 运行时获取
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
    private boolean docLink = false;
    /**
     * 作者
     */
    @RenderField
    private String docAuthor = "luminion";
    /**
     * 注释日期
     */
    @RenderField
    private String docDate = LocalDate.now().toString();

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
    private boolean enableLombok = true;

    /**
     * 生成toString方法
     * todo 处理toString方法
     */
    @RenderField
    private boolean enableToString;

    /**
     * 是否为链式模型setter
     */
    @RenderField
    private boolean enableChainSetter;

    /**
     * 实体是否生成 serialVersionUID
     */
    @RenderField
    private boolean enableSerializableUID = false;

    /**
     * 实体是否启用java.io.Serial (需JAVA 14) 注解
     *
     */
    @RenderField
    private boolean enableSerializableAnnotation = true;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        data.put("javaApiPackagePrefix", javaEEApi.getPackagePrefix());
        switch (docType) {
            case OPEN_API_V3:
                data.put("enableOpenApi3", true);
                break;
            case OPEN_API_V2:
                data.put("enableOpenApi2", true);
                break;
        }
        
        switch (runtimeEnv) {
            case MY_BATIS_PLUS_SQL_BOOSTER:
                data.put("sqlBooster", true);
            case MYBATIS_PLUS:
            default:
        }
        return data;
    }


    public Set<String> getModelDocImportPackages() {
        Set<String> importPackages = new TreeSet<>();
        switch (docType) {
            case OPEN_API_V3:
                importPackages.add(RuntimeClass.SWAGGER_V3_SCHEMA.getClassName());
                break;
            case OPEN_API_V2:
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
        if (enableLombok) {
            if (enableChainSetter) {
                importPackages.add(RuntimeClass.LOMBOK_ACCESSORS.getClassName());
            }
            if (enableToString){
                importPackages.add(RuntimeClass.LOMBOK_TO_STRING.getClassName());
            }
            importPackages.add(RuntimeClass.LOMBOK_GETTER.getClassName());
            importPackages.add(RuntimeClass.LOMBOK_SETTER.getClassName());
        }
        if (enableSerializableUID) {
            importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
            if (enableSerializableAnnotation) {
                importPackages.add(RuntimeClass.JAVA_IO_SERIAL.getClassName());
            }
        }
        return importPackages;
    }
    
}