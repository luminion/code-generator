package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.ExcelApi;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.internal.render.ImportPackageSupport;
import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.metadata.TableField;
import lombok.Data;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ExcelConfig implements TemplateRender {
    private final Configurer configurer;

    private ExcelApi excelApi = ExcelApi.EASY_EXCEL;

    @RenderField
    private String excelImportMethodName = "excelImport";

    @RenderField
    private String excelImportTemplateMethodName = "excelTemplate";

    @RenderField
    private String excelExportMethodName = "excelExport";

    @Override
    public Map<String, Object> renderData(RenderContext context) {
        Map<String, Object> data = TemplateRender.super.renderData(context);
        data.put("excelApiPackagePrefix", excelApi.getPackagePrefix());
        data.put("excelApiClass", excelApi.getMainEntrance());
        data.putAll(resolveExcelImportDtoImports(context));
        data.putAll(resolveExcelExportDtoImports(context));
        return data;
    }

    private Map<String, Object> resolveExcelImportDtoImports(RenderContext context) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        Set<String> importPackages = new TreeSet<>();
        importPackages.add(excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_IGNORE_UNANNOTATED.getCanonicalName());
        importPackages.add(excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_PROPERTY.getCanonicalName());

        for (TableField field : context.getTableInfo().getFields()) {
            if (field.isLogicDeleteField() || field.isPrimaryKey() || field.isVersionField()) {
                continue;
            }
            if (field.getFill() != null && ("INSERT".equals(field.getFill()) || "INSERT_UPDATE".equals(field.getFill()))) {
                continue;
            }
            if (commandConfig.getCommandExcludeColumns().contains(field.getColumnName())) {
                continue;
            }
            ImportPackageSupport.addIfPresent(importPackages, field.getJavaTypeCanonicalName());
        }
        importPackages.addAll(globalConfig.getModelImportPackages());
        importPackages.remove(RuntimeClass.LOMBOK_ACCESSORS.getCanonicalName());
        return ImportPackageSupport.splitImportPackages(importPackages, "excelImportParamFramePkg", "excelImportParamJavaPkg");
    }

    private Map<String, Object> resolveExcelExportDtoImports(RenderContext context) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Set<String> importPackages = new TreeSet<>();
        importPackages.add(excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_IGNORE_UNANNOTATED.getCanonicalName());
        importPackages.add(excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_PROPERTY.getCanonicalName());
        for (TableField field : context.getTableInfo().getFields()) {
            ImportPackageSupport.addIfPresent(importPackages, field.getJavaTypeCanonicalName());
        }
        importPackages.addAll(globalConfig.getModelImportPackages());
        return ImportPackageSupport.splitImportPackages(importPackages, "excelExportParamFramePkg", "excelExportParamJavaPkg");
    }
}