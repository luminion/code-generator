package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.TemplateModelRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.ConfigResolver;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
@Slf4j
public class ExportDTOConfig implements TemplateModelRender {
    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.EXPORT_DTO.getKey(),
            "%sExportDTO",
            "model.excel",
            "/templates/model/exportDTO.java",
            ".java"
    );

    @Override
    public TemplateFile renderTemplateFile() {
        return templateFile;
    }
    
    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        Set<String> importPackages = new TreeSet<>();

        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        // 关闭功能
        if (!globalConfig.isGenerateExport()) {
            this.renderTemplateFile().setGenerate(false);
        }

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
//        importPackages.addAll(globalConfig.getModelDocImportPackages());
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
        return data;
    }
}
