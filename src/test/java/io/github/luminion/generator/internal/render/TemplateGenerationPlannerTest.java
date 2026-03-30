package io.github.luminion.generator.internal.render;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.TemplateEnum;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemplateGenerationPlannerTest {

    @Test
    void noPrimaryKeyTableDisablesIdDependentArtifactsButKeepsCreateArtifact() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        configurer.getGlobalConfig().setGenerateQueryList(false);
        configurer.getGlobalConfig().setGenerateQueryPage(false);
        configurer.getGlobalConfig().setGenerateExcelExport(false);

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("audit_log");
        tableInfo.setEntityName("AuditLog");

        RenderContext context = configurer.createRenderContext(tableInfo);

        assertTrue(context.getTemplateFile(TemplateEnum.CREATE_PARAM.getKey()).isGenerate());
        assertFalse(context.getTemplateFile(TemplateEnum.UPDATE_PARAM.getKey()).isGenerate());
        assertEquals("Skipped because the table has no primary key", context.getTemplateFile(TemplateEnum.UPDATE_PARAM.getKey()).getSkipReason());
        assertFalse(context.getTemplateFile(TemplateEnum.QUERY_PARAM.getKey()).isGenerate());
        assertFalse(context.getTemplateFile(TemplateEnum.QUERY_RESULT.getKey()).isGenerate());
    }

    @Test
    void primaryKeyTableKeepsIdDependentArtifactsEnabled() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        configurer.getGlobalConfig().setGenerateQueryList(false);
        configurer.getGlobalConfig().setGenerateQueryPage(false);
        configurer.getGlobalConfig().setGenerateExcelExport(false);

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("SysUser");

        TableField idField = new TableField();
        idField.setRawColumnName("id");
        idField.setColumnName("id");
        idField.setPropertyName("id");
        idField.setPropertyType("Long");
        idField.setJavaTypeCanonicalName("java.lang.Long");
        idField.setPrimaryKey(true);
        tableInfo.setPrimaryKeyField(idField);
        tableInfo.setHasPrimaryKey(true);
        tableInfo.getFields().add(idField);

        RenderContext context = configurer.createRenderContext(tableInfo);

        assertTrue(context.getTemplateFile(TemplateEnum.CREATE_PARAM.getKey()).isGenerate());
        assertTrue(context.getTemplateFile(TemplateEnum.UPDATE_PARAM.getKey()).isGenerate());
        assertNull(context.getTemplateFile(TemplateEnum.UPDATE_PARAM.getKey()).getSkipReason());
        assertTrue(context.getTemplateFile(TemplateEnum.QUERY_PARAM.getKey()).isGenerate());
        assertTrue(context.getTemplateFile(TemplateEnum.QUERY_RESULT.getKey()).isGenerate());
    }

    @Test
    void userDisabledTemplateRemainsDisabledWhenFeatureWouldNormallyGenerateIt() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        configurer.getTemplateConfig().getQueryParam().setGenerate(false);

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("SysUser");

        TableField idField = new TableField();
        idField.setRawColumnName("id");
        idField.setColumnName("id");
        idField.setPropertyName("id");
        idField.setPropertyType("Long");
        idField.setJavaTypeCanonicalName("java.lang.Long");
        idField.setPrimaryKey(true);
        tableInfo.setPrimaryKeyField(idField);
        tableInfo.setHasPrimaryKey(true);
        tableInfo.getFields().add(idField);

        RenderContext context = configurer.createRenderContext(tableInfo);

        assertFalse(context.getTemplateFile(TemplateEnum.QUERY_PARAM.getKey()).isGenerate());
        assertEquals("Skipped because template generation is disabled",
                context.getTemplateFile(TemplateEnum.QUERY_PARAM.getKey()).getSkipReason());
    }
}
