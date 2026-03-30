package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.enums.TemplateEnum;
import io.github.luminion.generator.internal.render.ImportPackageSupport;
import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ServiceConfig implements TemplateRender {
    private final Configurer configurer;

    @RenderField
    protected String serviceSuperClass;

    @RenderField
    protected String serviceImplSuperClass;

    private String pageType = RuntimeClass.MYBATIS_PLUS_I_PAGE.getCanonicalName();

    @Override
    public Map<String, Object> renderData(RenderContext context) {
        Map<String, Object> data = TemplateRender.super.renderData(context);
        if (serviceSuperClass != null) {
            data.put("serviceSuperClass", ClassUtils.getSimpleName(serviceSuperClass));
        }
        if (serviceImplSuperClass != null) {
            data.put("serviceImplSuperClass", ClassUtils.getSimpleName(serviceImplSuperClass));
        }
        if (pageType != null) {
            data.put("servicePageType", ClassUtils.getSimpleName(pageType));
            data.put("servicePageTypeCanonicalName", pageType);
        }
        data.putAll(resolveServiceImports(context));
        data.putAll(resolveServiceImplImports(context));
        return data;
    }

    private Map<String, Object> resolveServiceImports(RenderContext context) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TableInfo tableInfo = context.getTableInfo();
        Map<String, TemplateClassFile> templateFileMap = context.getTemplateFiles();
        TableField idField = tableInfo.getPrimaryKeyField();
        String idFieldPropertyPkg = idField != null ? idField.getJavaTypeCanonicalName() : null;

        Set<String> importPackages = new TreeSet<>();
        ImportPackageSupport.addIfPresent(importPackages, serviceSuperClass);
        importPackages.add(templateFileMap.get(TemplateEnum.ENTITY.getKey()).getFullyQualifiedClassName());

        if (RuntimeEnv.MP_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
            importPackages.add(RuntimeClass.SQL_BOOSTER_MP_SERVICE.getCanonicalName());
        }
        if (RuntimeEnv.MYBATIS_PLUS.equals(globalConfig.getRuntimeEnv())) {
            if (globalConfig.isGenerateQueryById() && idField != null) {
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
                ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
            }
            if (globalConfig.isGenerateQueryList()) {
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            }
            if (globalConfig.isGenerateQueryPage()) {
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getCanonicalName());
                ImportPackageSupport.addIfPresent(importPackages, pageType);
            }
        }
        if (globalConfig.isGenerateCreate()) {
            importPackages.add(templateFileMap.get(TemplateEnum.CREATE_PARAM.getKey()).getFullyQualifiedClassName());
            ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
        }
        if (globalConfig.isGenerateUpdate() && idField != null) {
            importPackages.add(templateFileMap.get(TemplateEnum.UPDATE_PARAM.getKey()).getFullyQualifiedClassName());
            ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
        }
        if (globalConfig.isGenerateDelete() && idField != null) {
            ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
        }
        if (globalConfig.isGenerateExcelImport()) {
            importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getCanonicalName());
        }
        if (globalConfig.isGenerateExcelExport()) {
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getCanonicalName());
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
        }
        return ImportPackageSupport.splitImportPackages(importPackages, "serviceFramePkg", "serviceJavaPkg");
    }

    private Map<String, Object> resolveServiceImplImports(RenderContext context) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        TableInfo tableInfo = context.getTableInfo();
        Map<String, TemplateClassFile> templateFileMap = context.getTemplateFiles();
        TableField idField = tableInfo.getPrimaryKeyField();
        String idFieldPropertyPkg = idField != null ? idField.getJavaTypeCanonicalName() : null;

        Set<String> importPackages = new TreeSet<>();
        ImportPackageSupport.addIfPresent(importPackages, serviceImplSuperClass);
        importPackages.add(templateFileMap.get(TemplateEnum.ENTITY.getKey()).getFullyQualifiedClassName());
        importPackages.add(templateFileMap.get(TemplateEnum.MAPPER.getKey()).getFullyQualifiedClassName());

        TemplateClassFile service = templateFileMap.get(TemplateEnum.SERVICE.getKey());
        if (service.isGenerate()) {
            importPackages.add(service.getFullyQualifiedClassName());
        }
        importPackages.add(RuntimeClass.SPRING_BOOT_SERVICE.getCanonicalName());

        if (RuntimeEnv.MP_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
        }
        if (RuntimeEnv.MYBATIS_PLUS.equals(globalConfig.getRuntimeEnv())) {
//            importPackages.add(RuntimeClass.MYBATIS_PLUS_BASE_MAPPER.getCanonicalName());
            if (globalConfig.isGenerateQueryById() && idField != null) {
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
                ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
            }
            if (globalConfig.isGenerateQueryList()) {
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            }
            if (globalConfig.isGenerateQueryPage()) {
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_PAGE.getCanonicalName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
                ImportPackageSupport.addIfPresent(importPackages, pageType);
            }
        }
        if (globalConfig.isGenerateCreate()) {
            importPackages.add(templateFileMap.get(TemplateEnum.CREATE_PARAM.getKey()).getFullyQualifiedClassName());
            ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
        }
        if (globalConfig.isGenerateUpdate() && idField != null) {
            importPackages.add(templateFileMap.get(TemplateEnum.UPDATE_PARAM.getKey()).getFullyQualifiedClassName());
            ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
        }
        if (globalConfig.isGenerateDelete() && idField != null) {
            ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
        }

        String excelClass = excelConfig.getExcelApi().getPackagePrefix() + excelConfig.getExcelApi().getMainEntrance();
        String longestMatchColumnWidthStyleStrategyClass = excelConfig.getExcelApi().getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_LONGEST_MATCH_COLUMN_WIDTH_STYLE_STRATEGY.getCanonicalName();
        if (globalConfig.isGenerateExcelImport()) {
            importPackages.add(templateFileMap.get(TemplateEnum.EXCEL_IMPORT_PARAM.getKey()).getFullyQualifiedClassName());
            importPackages.add(excelClass);
            importPackages.add(longestMatchColumnWidthStyleStrategyClass);
            importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_UTIL_COLLECTIONS.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_STREAM_COLLECTORS.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            importPackages.add(RuntimeClass.SPRING_BOOT_BEAN_UTILS.getCanonicalName());
        }
        if (globalConfig.isGenerateExcelExport()) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
            importPackages.add(templateFileMap.get(TemplateEnum.EXCEL_EXPORT_PARAM.getKey()).getFullyQualifiedClassName());
            importPackages.add(excelClass);
            importPackages.add(longestMatchColumnWidthStyleStrategyClass);
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getCanonicalName());
        }
        return ImportPackageSupport.splitImportPackages(importPackages, "serviceImplFramePkg", "serviceImplJavaPkg");
    }
}
