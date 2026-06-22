package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.IdType;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.internal.render.ImportPackageSupport;
import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class EntityConfig implements TemplateRender {
    private final Configurer configurer;

    private String entitySuperClass;

    @RenderField
    protected IdType idType = IdType.AUTO;

    @RenderField
    protected boolean activeRecord;

    @RenderField
    protected boolean tableFieldAnnotation;

    @Override
    public Map<String, Object> renderData(RenderContext context) {
        TableInfo tableInfo = context.getTableInfo();
        Map<String, Object> data = TemplateRender.super.renderData(context);
        data.put("entitySuperClass", ClassUtils.getSimpleName(entitySuperClass));
        data.put("tableNameAnnotation", requiresTableNameAnnotation(tableInfo));
        data.putAll(resolveEntityImports(tableInfo));
        return data;
    }

    private Map<String, Object> resolveEntityImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Set<String> importPackages = new TreeSet<>();
        boolean tableNameAnnotation = requiresTableNameAnnotation(tableInfo);

        ImportPackageSupport.addIfPresent(importPackages, entitySuperClass);
        if (tableFieldAnnotation) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getCanonicalName());
        }
        if (tableNameAnnotation) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_NAME.getCanonicalName());
        }
        if (activeRecord) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_ACTIVE_RECORD_MODEL.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getCanonicalName());
        }
        tableInfo.getFields().forEach(field -> {
            ImportPackageSupport.addIfPresent(importPackages, field.getJavaTypeCanonicalName());
            if (field.isPrimaryKey()) {
                importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_ID.getCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_ID_TYPE.getCanonicalName());
            } else if (field.requiresColumnAnnotation() || tableFieldAnnotation) {
                importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getCanonicalName());
            }
            if (field.getFill() != null) {
                importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_FIELD_FILL.getCanonicalName());
            }
            if (field.isVersionField()) {
                importPackages.add(RuntimeClass.MYBATIS_PLUS_VERSION.getCanonicalName());
            }
            if (field.isLogicDeleteField()) {
                importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_LOGIC.getCanonicalName());
            }
        });
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());

        return ImportPackageSupport.splitImportPackages(importPackages, "entityFramePkg", "entityJavaPkg");
    }

    private boolean requiresTableNameAnnotation(TableInfo tableInfo) {
        String schemaName = configurer.getGlobalConfig().getSchemaName();
        if (StringUtils.isNotBlank(schemaName)) {
            return true;
        }
        String tableName = tableInfo.getTableName();
        String entityName = tableInfo.getEntityName();
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(entityName)) {
            return false;
        }
        return !tableName.equalsIgnoreCase(StringUtils.camelToUnderline(entityName));
    }
}
