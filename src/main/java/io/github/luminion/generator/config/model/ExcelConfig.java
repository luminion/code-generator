package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.MultiTemplateModelRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.ConfigResolver;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel功能配置
 * <p>
 * 整合导入(ImportDTO)和导出(ExportDTO)的配置
 *
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ExcelConfig implements MultiTemplateModelRender {

    /**
     * 导入模板文件
     */
    private TemplateFile importTemplateFile = new TemplateFile(
            TemplateFileEnum.IMPORT_DTO.getKey(),
            "%sImportDTO",
            "model.excel",
            "/templates/model/importDTO.java",
            ".java"
    );

    /**
     * 导出模板文件
     */
    private TemplateFile exportTemplateFile = new TemplateFile(
            TemplateFileEnum.EXPORT_DTO.getKey(),
            "%sExportDTO",
            "model.excel",
            "/templates/model/exportDTO.java",
            ".java"
    );

    @Override
    public void init() {
    }

    @Override
    public void renderDataPreProcess(TableInfo tableInfo) {
        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        // 关闭功能
        if (!globalConfig.isGenerateImport()) {
            importTemplateFile.setGenerate(false);
        }
        if (!globalConfig.isGenerateExport()) {
            exportTemplateFile.setGenerate(false);
        }
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        Set<String> importPackages = new TreeSet<>();

        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        // 导入配置渲染数据
        renderImportData(tableInfo, data, importPackages, configCollector, globalConfig);

        // 导出配置渲染数据
        renderExportData(tableInfo, data, importPackages, configCollector, globalConfig);

        return data;
    }

    private void renderImportData(TableInfo tableInfo, Map<String, Object> data,
                                 Set<String> importPackages, ConfigCollector<?> configCollector,
                                 GlobalConfig globalConfig) {
        // excel包
        String excelIgnoreUnannotated = globalConfig.getExcelApi().getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_IGNORE_UNANNOTATED.getClassName();
        String excelProperty = globalConfig.getExcelApi().getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_PROPERTY.getClassName();
        importPackages.add(excelIgnoreUnannotated);

        // 属性过滤
        importPackages.add(excelProperty);
        Set<String> editExcludeColumns = configCollector.getStrategyConfig().getEditExcludeColumns();
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            if (field.isKeyFlag()) {
                continue;
            }
            if (field.isVersionField()) {
                continue;
            }
            if (field.getFill() != null &&
                    ("INSERT".equals(field.getFill()) || "INSERT_UPDATE".equals(field.getFill()))
            ) {
                continue;
            }
            if (editExcludeColumns.contains(field.getColumnName())) {
                continue;
            }
            JavaFieldInfo columnType = field.getJavaType();
            if (null != columnType && null != columnType.getPkg()) {
                importPackages.add(columnType.getPkg());
            }
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelSerializableImportPackages());
        importPackages.addAll(globalConfig.getModelLombokImportPackages());
        importPackages.remove(RuntimeClass.LOMBOK_ACCESSORS.getClassName());

        // 导入包
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("importDTOFramePkg", frameworkPackages);
        data.put("importDTOJavaPkg", javaPackages);
    }

    private void renderExportData(TableInfo tableInfo, Map<String, Object> data,
                                 Set<String> importPackages, ConfigCollector<?> configCollector,
                                 GlobalConfig globalConfig) {
        // excel包
        String excelIgnoreUnannotated = globalConfig.getExcelApi().getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_IGNORE_UNANNOTATED.getClassName();
        String excelProperty = globalConfig.getExcelApi().getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_PROPERTY.getClassName();
        importPackages.add(excelIgnoreUnannotated);

        // 属性过滤
        importPackages.add(excelProperty);
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            JavaFieldInfo columnType = field.getJavaType();
            if (null != columnType && null != columnType.getPkg()) {
                importPackages.add(columnType.getPkg());
            }
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelSerializableImportPackages());
        importPackages.addAll(globalConfig.getModelLombokImportPackages());
        importPackages.remove(RuntimeClass.LOMBOK_ACCESSORS.getClassName());

        // 导入包
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("exportDTOFramePkg", frameworkPackages);
        data.put("exportDTOJavaPkg", javaPackages);
    }

    @Override
    public void renderDataPostProcess(TableInfo tableInfo, Map<String, Object> renderData) {
    }

    @Override
    public List<TemplateFile> renderTemplateFiles() {
        List<TemplateFile> files = new ArrayList<>();
        files.add(importTemplateFile);
        files.add(exportTemplateFile);
        return files;
    }
}
