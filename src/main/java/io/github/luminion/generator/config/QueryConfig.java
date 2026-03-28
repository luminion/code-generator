package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.internal.render.ImportPackageSupport;
import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.naming.DefaultExtraFieldStrategy;
import io.github.luminion.generator.naming.ExtraFieldStrategy;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class QueryConfig implements TemplateRender {
    private final Configurer configurer;

    @RenderField
    private String queryByIdMethodName = "voById";

    @RenderField
    private String queryListMethodName = "voList";

    @RenderField
    private String queryPageMethodName = "voPage";

    @RenderField
    private Map<String, String> extraFieldSuffixMap = new LinkedHashMap<>();

    @RenderField
    private ExtraFieldStrategy extraFieldStrategy = new DefaultExtraFieldStrategy();

    @RenderField
    private String pageParamName = "current";

    @RenderField
    private String sizeParamName = "size";

    @RenderField
    private boolean queryParamPageFields = true;

    private String queryParamSuperClass;
    private String queryResultSuperClass;

    @Override
    public Map<String, Object> renderData(RenderContext context) {
        Map<String, Object> data = TemplateRender.super.renderData(context);
        if (queryParamSuperClass != null) {
            data.put("queryParamSuperClass", ClassUtils.getSimpleName(queryParamSuperClass));
            data.put("queryParamSuperClassCanonicalName", queryParamSuperClass);
        }
        if (queryResultSuperClass != null) {
            data.put("queryResultSuperClass", ClassUtils.getSimpleName(queryResultSuperClass));
            data.put("queryResultSuperClassCanonicalName", queryResultSuperClass);
        }
        data.put("pageCapitalName", capitalize(pageParamName));
        data.put("sizeCapitalName", capitalize(sizeParamName));
        data.putAll(resolveQueryParamImports(context));
        data.putAll(resolveQueryResultImports(context));
        return data;
    }

    private String capitalize(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    private Map<String, Object> resolveQueryParamImports(RenderContext context) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Set<String> importPackages = new TreeSet<>();
        importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
        for (TableField field : context.getTableInfo().getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            ImportPackageSupport.addIfPresent(importPackages, field.getJavaTypeCanonicalName());
        }
        ImportPackageSupport.addIfPresent(importPackages, queryParamSuperClass);
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());
        return ImportPackageSupport.splitImportPackages(importPackages, "queryParamFramePkg", "queryParamJavaPkg");
    }

    private Map<String, Object> resolveQueryResultImports(RenderContext context) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Set<String> importPackages = new TreeSet<>();
        for (TableField field : context.getTableInfo().getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            ImportPackageSupport.addIfPresent(importPackages, field.getJavaTypeCanonicalName());
        }
        ImportPackageSupport.addIfPresent(importPackages, queryResultSuperClass);
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());
        return ImportPackageSupport.splitImportPackages(importPackages, "queryResultFramePkg", "queryResultJavaPkg");
    }
}