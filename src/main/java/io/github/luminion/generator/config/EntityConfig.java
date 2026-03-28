package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.IdType;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.internal.render.ImportPackageSupport;
import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.metadata.TableInfo;
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
        data.putAll(resolveEntityImports(tableInfo));
        return data;
    }

    private Map<String, Object> resolveEntityImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Set<String> importPackages = new TreeSet<>();

        ImportPackageSupport.addIfPresent(importPackages, entitySuperClass);
        if (tableFieldAnnotation) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getCanonicalName());
        }
        if (tableInfo.isConvert()) {
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
}