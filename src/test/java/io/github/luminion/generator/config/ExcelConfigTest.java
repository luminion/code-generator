package io.github.luminion.generator.config;

import io.github.luminion.generator.builder.ExcelBuilder;
import io.github.luminion.generator.engine.VelocityTemplateEngine;
import io.github.luminion.generator.enums.ExcelExportMode;
import io.github.luminion.generator.enums.ExcelImportMode;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.util.InitializeUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcelConfigTest {

    @Test
    void excelBuilderExposesBatchImportAndPagedExportModes() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisPlus(configurer);

        new ExcelBuilder(configurer)
                .importMode(ExcelImportMode.BATCH)
                .excelImportBatchSize(512)
                .exportMode(ExcelExportMode.PAGED)
                .excelExportPageSize(4096);

        Map<String, Object> data = configurer.renderMap(configurer.createRenderContext(sampleTableInfo()));

        assertEquals(ExcelImportMode.BATCH, configurer.getExcelConfig().getExcelImportMode());
        assertEquals(512, configurer.getExcelConfig().getExcelImportBatchSize());
        assertEquals(ExcelExportMode.PAGED, configurer.getExcelConfig().getExcelExportMode());
        assertEquals(4096, configurer.getExcelConfig().getExcelExportPageSize());
        assertEquals(true, data.get("excelImportBatchMode"));
        assertEquals(true, data.get("excelExportPagedMode"));

        @SuppressWarnings("unchecked")
        Set<String> serviceImplFramePkg = (Set<String>) data.get("serviceImplFramePkg");
        assertTrue(serviceImplFramePkg.contains("com.alibaba.excel.context.AnalysisContext"));
        assertTrue(serviceImplFramePkg.contains("com.alibaba.excel.event.AnalysisEventListener"));
        assertTrue(serviceImplFramePkg.contains("com.alibaba.excel.ExcelWriter"));
        assertTrue(serviceImplFramePkg.contains("com.alibaba.excel.write.metadata.WriteSheet"));
    }

    @Test
    void batchAndPagedSizesMustBePositive() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        ExcelBuilder builder = new ExcelBuilder(configurer);

        IllegalArgumentException importException = assertThrows(IllegalArgumentException.class,
                () -> builder.excelImportBatchSize(0));
        IllegalArgumentException exportException = assertThrows(IllegalArgumentException.class,
                () -> builder.excelExportPageSize(-1));

        assertEquals("excel import batch size must be greater than 0", importException.getMessage());
        assertEquals("excel export page size must be greater than 0", exportException.getMessage());
    }

    @Test
    void serviceImplTemplateKeepsSimpleExcelBranchesByDefault() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisPlus(configurer);

        String rendered = renderServiceImpl(configurer, sampleTableInfo());

        assertTrue(rendered.contains(".head(SysUserExcelImportDTO.class).sheet().doReadSync();"));
        assertTrue(rendered.contains(".doWrite(records);"));
        assertFalse(rendered.contains("AnalysisEventListener<SysUserExcelImportDTO>"));
        assertFalse(rendered.contains("queryExcelExportPage(param, current"));
    }

    @Test
    void serviceImplTemplateImportsBeanUtilsWithoutExcelImport() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisPlus(configurer);
        configurer.getGlobalConfig().setGenerateExcelImport(false);
        configurer.getGlobalConfig().setGenerateExcelExport(false);

        String rendered = renderServiceImpl(configurer, sampleTableInfo());

        assertTrue(rendered.contains("import org.springframework.beans.BeanUtils;"));
        assertTrue(rendered.contains("BeanUtils.copyProperties(param, entity);"));
    }

    @Test
    void serviceImplTemplateRendersBatchImportAndPagedExportBranches() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisPlus(configurer);

        new ExcelBuilder(configurer)
                .importMode(ExcelImportMode.BATCH)
                .excelImportBatchSize(256)
                .exportMode(ExcelExportMode.PAGED)
                .excelExportPageSize(1024);

        String rendered = renderServiceImpl(configurer, sampleTableInfo());

        assertTrue(rendered.contains("new AnalysisEventListener<SysUserExcelImportDTO>()"));
        assertTrue(rendered.contains("flushExcelImportBatch(entityList)"));
        assertTrue(rendered.contains("private int flushExcelImportBatch(List<SysUser> entityList)"));
        assertTrue(rendered.contains("ExcelWriter excelWriter = EasyExcel.write(os, SysUserExcelExportDTO.class).build();"));
        assertTrue(rendered.contains("WriteSheet writeSheet = EasyExcel.writerSheet()"));
        assertTrue(rendered.contains("queryExcelExportPage(param, current, 1024L)"));
        assertTrue(rendered.contains("private List<SysUserVO> queryExcelExportPage(SysUserQueryDTO param, long current, long size)"));
        assertTrue(rendered.contains("Page<SysUserVO> page = new Page<>(current, size);"));
        assertFalse(rendered.contains(".doReadSync();"));
        assertFalse(rendered.contains(".doWrite(records);"));
    }

    private static String renderServiceImpl(Configurer configurer, TableInfo tableInfo) throws Exception {
        VelocityTemplateEngine engine = new VelocityTemplateEngine(configurer);
        Map<String, Object> renderData = configurer.renderMap(configurer.createRenderContext(tableInfo));
        return engine.writer(renderData, "serviceImpl", readResource("templates/serviceImpl.java.vm"));
    }

    private static String readResource(String path) throws IOException {
        try (InputStream inputStream = ExcelConfigTest.class.getClassLoader().getResourceAsStream(path)) {
            assertNotNull(inputStream, "Missing resource: " + path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toString(StandardCharsets.UTF_8.name());
        }
    }

    private static TableInfo sampleTableInfo() {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("SysUser");
        tableInfo.setComment("系统用户");

        TableField idField = new TableField();
        idField.setRawColumnName("id");
        idField.setColumnName("id");
        idField.setPropertyName("id");
        idField.setPropertyType("Long");
        idField.setJavaTypeCanonicalName("java.lang.Long");
        idField.setComment("主键");
        idField.setPrimaryKey(true);
        idField.setMetaInfo(metaInfo(false));

        TableField nameField = new TableField();
        nameField.setRawColumnName("username");
        nameField.setColumnName("username");
        nameField.setPropertyName("username");
        nameField.setPropertyType("String");
        nameField.setJavaTypeCanonicalName("java.lang.String");
        nameField.setComment("用户名");
        nameField.setMetaInfo(metaInfo(false));

        tableInfo.setPrimaryKeyField(idField);
        tableInfo.setHasPrimaryKey(true);
        tableInfo.getFields().add(idField);
        tableInfo.getFields().add(nameField);
        return tableInfo;
    }

    private static TableField.MetaInfo metaInfo(boolean nullable) {
        TableField.MetaInfo metaInfo = new TableField.MetaInfo();
        metaInfo.setNullable(nullable);
        return metaInfo;
    }
}
