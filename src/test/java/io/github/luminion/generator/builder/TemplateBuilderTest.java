package io.github.luminion.generator.builder;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.metadata.TemplateClassFile;
import io.github.luminion.generator.metadata.TableInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemplateBuilderTest {

    @Test
    void addTemplateFileWithBuilderRegistersCustomTemplate() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        TemplateBuilder builder = new TemplateBuilder(configurer);

        builder.addTemplateFile(file -> file
                .key("queryCondition")
                .nameFormat("%sQueryCondition")
                .subPackage("model.query")
                .templatePath("/templates/queryParam.java")
                .outputFileSuffix(".java"));

        TableInfo tableInfo = new TableInfo();
        tableInfo.setEntityName("SysUser");

        TemplateClassFile resolved = configurer.resolveTemplateFiles(tableInfo).get("queryCondition");

        assertEquals("SysUserQueryCondition", resolved.getSimpleClassName());
        assertEquals("queryCondition", resolved.getKey());
    }

    @Test
    void addTemplateFileWithBuilderValidatesRequiredFieldsAfterConsumer() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        TemplateBuilder builder = new TemplateBuilder(configurer);

        assertThrows(IllegalArgumentException.class, () ->
                builder.addTemplateFile(file -> file
                        .key("queryCondition")
                        .subPackage("model.query")));
    }

    @Test
    void eachAddTemplateFileCallCreatesIndependentTemplateFile() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        TemplateBuilder builder = new TemplateBuilder(configurer);

        builder.addTemplateFile(file -> file
                .key("queryCondition")
                .nameFormat("%sQueryCondition")
                .subPackage("model.query")
                .templatePath("/templates/queryParam.java")
                .outputFileSuffix(".java"));

        builder.addTemplateFile(file -> file
                .key("exportView")
                .nameFormat("%sExportView")
                .subPackage("model.export")
                .templatePath("/templates/queryResult.java")
                .outputFileSuffix(".java"));

        TableInfo tableInfo = new TableInfo();
        tableInfo.setEntityName("SysUser");

        TemplateClassFile queryCondition = configurer.resolveTemplateFiles(tableInfo).get("queryCondition");
        TemplateClassFile exportView = configurer.resolveTemplateFiles(tableInfo).get("exportView");

        assertEquals("SysUserQueryCondition", queryCondition.getSimpleClassName());
        assertEquals("SysUserExportView", exportView.getSimpleClassName());
    }
}
