package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.TemplateRender;
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

    /**
     * 自定义配置渲染数据
     * todo 运行时获取
     */
    protected Map<String, Object> customRenderData = new HashMap<>();

    /**
     * 自定义渲染逻辑
     */
    protected BiConsumer<TableInfo, Map<String, Object>> customRenderLogic;

    /**
     * 文档注释类型
     */
    protected DocType docType = DocType.JAVA_DOC;
    /**
     * 文档注释添加相关类链接
     */
    protected boolean docLink = false;
    /**
     * 作者
     */
    protected String docAuthor = "luminion";
    /**
     * 注释日期
     */
    protected String docDate = LocalDate.now().toString();

    /**
     * java ee api
     */
    protected JavaEEApi javaEEApi = JavaEEApi.JAKARTA;

    /**
     * 外部运行环境
     */
    protected RuntimeEnv runtimeEnv = RuntimeEnv.MYBATIS_PLUS;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("docAuthor", this.docAuthor);
        data.put("docDate", this.docDate);
        data.put("docLink", this.docLink);
        data.put("javaApiPackagePrefix", javaEEApi.getPackagePrefix());

        switch (this.docType) {
            case SWAGGER_V3:
                data.put("swagger3", true);
                break;
            case SWAGGER_V2:
                data.put("swagger2", true);
                break;
        }
        
        switch (this.runtimeEnv) {
            case MY_BATIS_PLUS_SQL_BOOSTER:
                data.put("sqlBooster", true);
            case MYBATIS_PLUS:
            default:
        }
        return data;
    }


    public Set<String> getModelDocImportPackages() {
        Set<String> importPackages = new TreeSet<>();
        switch (this.docType) {
            case SWAGGER_V3:
                importPackages.add(RuntimeClass.SWAGGER_V3_SCHEMA.getClassName());
                break;
            case SWAGGER_V2:
                importPackages.add(RuntimeClass.SWAGGER_V2_API_MODEL.getClassName());
                importPackages.add(RuntimeClass.SWAGGER_V2_API_MODEL_PROPERTY.getClassName());
                break;
        }
        return importPackages;
    }
    
}