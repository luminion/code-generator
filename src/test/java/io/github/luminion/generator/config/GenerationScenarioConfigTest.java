package io.github.luminion.generator.config;

import io.github.luminion.generator.builder.ControllerBuilder;
import io.github.luminion.generator.builder.ExcelBuilder;
import io.github.luminion.generator.builder.QueryBuilder;
import io.github.luminion.generator.builder.TemplateBuilder;
import io.github.luminion.generator.enums.BaseUrlStyle;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.enums.ExcelApi;
import io.github.luminion.generator.enums.JavaEEApi;
import io.github.luminion.generator.enums.TemplateEnum;
import io.github.luminion.generator.metadata.InvokeInfo;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Self-tests for the example generation scenario migrated from the demo project.
 * These tests only verify configuration wiring and output conventions.
 * They intentionally avoid external databases and real file generation.
 */
class GenerationScenarioConfigTest {

    @Test
    void exampleScenarioKeepsGeneratedFilesUnderIgnoredTargetDirectory() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        new TemplateBuilder(configurer)
                .enableFileOverride()
                .outputDir(testOutputDir("mysql-mybatis-plus"))
                .parentPackage("com.example.codegen.mysql");

        TableInfo tableInfo = sampleTableInfo();
        TemplateClassFile entityFile = configurer.resolveTemplateFiles(tableInfo).get(TemplateEnum.ENTITY.getKey());

        // Generated test artifacts should stay under target/test-generated and never touch src/main/java.
        assertTrue(entityFile.getFileOutputDir().contains(Paths.get("target", "test-generated").toString()));
        assertEquals(
                Paths.get(
                        testOutputDir("mysql-mybatis-plus"),
                        "com",
                        "example",
                        "codegen",
                        "mysql",
                        "model",
                        "entity"
                ).toString(),
                entityFile.getFileOutputDir()
        );
    }

    @Test
    void exampleScenarioRetainsMysqlDemoConfigurationContract() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        new TemplateBuilder(configurer)
                .outputDir(testOutputDir("mysql-mybatis-plus"))
                .parentPackage("com.example.codegen.mysql");
        new QueryBuilder(configurer)
                .pageParamName("pageNum")
                .sizeParamName("pageSize");
        new ExcelBuilder(configurer)
                .api(ExcelApi.APACHE_FESOD);
        new ControllerBuilder(configurer)
                .baseUrl("admin")
                .returnMethod("com.common.R", "of")
                .pageMethod("com.common.P4MpPage", "of");
        configurer.getGlobalConfig().setJavaEEApi(JavaEEApi.JAKARTA);
        configurer.getGlobalConfig().setSeeTags(false);
        configurer.getDataSourceConfig().setDateType(DateType.TIME_PACK);

        Map<String, Object> data = configurer.renderMap(configurer.createRenderContext(sampleTableInfo()));
        InvokeInfo returnType = (InvokeInfo) data.get("returnType");
        InvokeInfo pageType = (InvokeInfo) data.get("pageType");

        // Keep the demo-style request path and paging contract stable for templates.
        assertEquals("/admin/sys-user", data.get("requestBaseUrl"));
        assertEquals("pageNum", data.get("pageParamName"));
        assertEquals("pageSize", data.get("sizeParamName"));
        assertEquals("com.common.R", returnType.getFullyQualifiedClassName());
        assertEquals("com.common.P4MpPage", pageType.getFullyQualifiedClassName());
        assertEquals(DateType.TIME_PACK, configurer.getDataSourceConfig().getDateType());
        assertEquals(ExcelApi.APACHE_FESOD, configurer.getExcelConfig().getExcelApi());
    }

    @Test
    void exampleScenarioSupportsSlashBaseUrlStyle() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        new TemplateBuilder(configurer)
                .parentPackage("com.example.codegen.mysql")
                .parentModule("system");
        new ControllerBuilder(configurer)
                .baseUrl("admin")
                .baseUrlStyle(BaseUrlStyle.SLASH_CASE);

        Map<String, Object> data = configurer.renderMap(configurer.createRenderContext(sampleTableInfo()));

        assertEquals("/admin/system/sys/user", data.get("requestBaseUrl"));
    }

    private static String testOutputDir(String scenario) {
        return Paths.get(
                System.getProperty("user.dir"),
                "target",
                "test-generated",
                scenario,
                "src",
                "main",
                "java"
        ).toString();
    }

    private static TableInfo sampleTableInfo() {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("SysUser");
        return tableInfo;
    }
}
