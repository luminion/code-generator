package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.Resolver;
import io.github.luminion.generator.config.core.GlobalConfig;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class EntityExcelImportDTOConfig implements TemplateRender {
    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.ENTITY_EXCEL_IMPORT_DTO.getKey(),
            "%sExcelImportDTO",
            "dto.excel",
            "/templates/model/entityExcelImportDTO.java",
            ".java"
    );
    /**
     * 导入的包
     */
    private Set<String> importPackages = new TreeSet<>();


    @Override
    public List<TemplateFile> renderTemplateFiles() {
        return Collections.singletonList(templateFile);
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        Resolver resolver = tableInfo.getResolver();
        Configurer<?> configurer = resolver.getConfigurer();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        
        // 关闭功能
        if (!globalConfig.isGenerateImport()){
            this.getTemplateFile().setGenerate(false);
        }

        // excel包
        String excelIgnoreUnannotated = globalConfig.getExcelApi().getPackagePrefix() + "annotation.ExcelIgnoreUnannotated";
        String excelProperty = globalConfig.getExcelApi().getPackagePrefix() + "annotation.ExcelProperty";
        importPackages.add(excelIgnoreUnannotated);

        // 属性过滤
        importPackages.add(excelProperty);
        Set<String> editExcludeColumns = tableInfo.getConfigurer().getStrategyConfig().getEditExcludeColumns();
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
//        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelLombokImportPackages());
        importPackages.remove("lombok.experimental.Accessors");


        // 导入包
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("excelImportDTOJavaPkg", javaPackages);
        data.put("excelImportDTOFramePkg", frameworkPackages);
        return data;
    }

}
