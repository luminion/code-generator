package io.github.luminion.generator;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.metadata.GenerationSummary;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateFile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LambdaGeneratorTest {

    @Test
    void executeTableFilterOverwritesPreviousIncludeTables() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        LambdaGenerator generator = new LambdaGenerator(configurer);
        configurer.getDataSourceConfig().getIncludeTables().add("legacy_table");
        configurer.getDataSourceConfig().getExcludeTables().add("sys_log");

        generator.replaceIncludeTables("sys_user", "sys_role");

        assertEquals(new LinkedHashSet<>(Arrays.asList("sys_user", "sys_role")),
                configurer.getDataSourceConfig().getIncludeTables());
        assertTrue(configurer.getDataSourceConfig().getExcludeTables().isEmpty());

        generator.replaceIncludeTables("audit_log");

        assertEquals(new LinkedHashSet<>(Arrays.asList("audit_log")),
                configurer.getDataSourceConfig().getIncludeTables());
    }

    @Test
    void buildGenerationSummaryMessagePrintsGeneratedTablesAndTemplates() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        LambdaGenerator generator = new LambdaGenerator(configurer);
        GenerationSummary generationSummary = new GenerationSummary();

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("SysUser");
        generationSummary.recordMatchedTable(tableInfo);
        generationSummary.recordGeneratedFile(tableInfo,
                new TemplateFile("entity", "%s", "", "/templates/entity.java", ".java"));
        generationSummary.recordGeneratedFile(tableInfo,
                new TemplateFile("mapper", "%sMapper", "mapper", "/templates/mapper.java", ".java"));

        String message = generator.buildGenerationSummaryMessage(generationSummary,
                new File("target/test-generated/demo/src/main/java"));

        assertTrue(message.contains("matched tables: 1"));
        assertTrue(message.contains("generated files: 2"));
        assertTrue(message.contains("- sys_user (SysUser): entity, mapper"));
        assertTrue(message.contains("generated file output dir:"));
        assertFalse(message.contains("[WARNING]"));
    }

    @Test
    void buildGenerationSummaryMessageWarnsWhenNoTablesMatched() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        LambdaGenerator generator = new LambdaGenerator(configurer);

        String message = generator.buildGenerationSummaryMessage(new GenerationSummary(),
                new File("target/test-generated/demo/src/main/java"));

        assertTrue(message.contains("[WARNING] No tables matched for code generation."));
        assertTrue(message.contains("database connection"));
        assertTrue(message.contains("configured output dir:"));
    }
}
