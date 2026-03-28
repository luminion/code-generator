package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.internal.render.ImportPackageSupport;
import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.metadata.TableField;
import lombok.Data;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class CommandConfig implements TemplateRender {
    private final Configurer configurer;

    @RenderField
    private boolean valid = true;

    @RenderField
    private String createMethodName = "create";

    @RenderField
    private String updateMethodName = "update";

    @RenderField
    private String deleteMethodName = "delete";

    @RenderField
    private Set<String> commandExcludeColumns = new HashSet<>();

    @Override
    public Map<String, Object> renderData(RenderContext context) {
        Map<String, Object> data = TemplateRender.super.renderData(context);
        data.putAll(resolveCreateDtoImports(context));
        data.putAll(resolveUpdateDtoImports(context));
        return data;
    }

    private Map<String, Object> resolveCreateDtoImports(RenderContext context) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Set<String> importPackages = new TreeSet<>();
        String size = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_SIZE.getCanonicalName();
        String notBlank = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_BLANK.getCanonicalName();
        String notNull = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_NULL.getCanonicalName();

        for (TableField field : context.getTableInfo().getFields()) {
            if (field.isPrimaryKey() || field.isVersionField() || field.isLogicDeleteField()) {
                continue;
            }
            if (field.getFill() != null && ("INSERT".equals(field.getFill()) || "INSERT_UPDATE".equals(field.getFill()))) {
                continue;
            }
            if (commandExcludeColumns.contains(field.getColumnName())) {
                continue;
            }
            ImportPackageSupport.addIfPresent(importPackages, field.getJavaTypeCanonicalName());
            boolean isString = "String".equals(field.getPropertyType());
            boolean notNullField = !field.getMetaInfo().isNullable();
            if (valid) {
                if (notNullField) {
                    importPackages.add(isString ? notBlank : notNull);
                }
                if (isString) {
                    importPackages.add(size);
                }
            }
        }

        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());
        return ImportPackageSupport.splitImportPackages(importPackages, "createParamFramePkg", "createParamJavaPkg");
    }

    private Map<String, Object> resolveUpdateDtoImports(RenderContext context) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Set<String> importPackages = new TreeSet<>();
        String size = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_SIZE.getCanonicalName();
        String notBlank = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_BLANK.getCanonicalName();
        String notNull = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_NULL.getCanonicalName();

        for (TableField field : context.getTableInfo().getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            boolean isInsertFill = "INSERT".equals(field.getFill()) || "INSERT_UPDATE".equals(field.getFill());
            boolean isUpdateFill = "UPDATE".equals(field.getFill());
            if (field.getFill() != null && (isInsertFill || isUpdateFill)) {
                continue;
            }
            if (commandExcludeColumns.contains(field.getColumnName())) {
                continue;
            }
            ImportPackageSupport.addIfPresent(importPackages, field.getJavaTypeCanonicalName());
            boolean notNullField = field.isPrimaryKey() || field.isVersionField();
            boolean isString = "String".equals(field.getPropertyType());
            if (valid) {
                if (field.isPrimaryKey()) {
                    importPackages.add(notNull);
                }
                if (notNullField) {
                    importPackages.add(isString ? notBlank : notNull);
                }
                if (isString) {
                    importPackages.add(size);
                }
            }
        }

        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());
        return ImportPackageSupport.splitImportPackages(importPackages, "updateParamFramePkg", "updateParamJavaPkg");
    }
}