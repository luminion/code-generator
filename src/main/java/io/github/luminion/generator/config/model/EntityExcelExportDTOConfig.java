package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.core.GlobalConfig;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
@Slf4j
public class EntityExcelExportDTOConfig implements TemplateRender {
    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.ENTITY_EXCEL_EXPORT_DTO.getKey(),
            "%sExcelExportDTO",
            "dto.excel",
            "/templates/base/entityExcelExportDTO.java",
            ".java"
    );

    @Override
    public void init() {
        TemplateRender.super.init();
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        GlobalConfig globalConfig = tableInfo.getConfigurer().getGlobalConfig();
        Set<String> importPackages = new TreeSet<>();
        
        // excel包
        String excelIgnoreUnannotated = globalConfig.getExcelApi().getPackagePrefix() + "annotation.ExcelIgnoreUnannotated";
        String excelProperty = globalConfig.getExcelApi().getPackagePrefix() + "annotation.ExcelProperty";
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
        importPackages.remove("lombok.experimental.Accessors");

        // 导入包
        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        data.put("excelExportDTOFramePkg", frameworkPackages);
        data.put("excelExportDTOJavaPkg", javaPackages);
        return data;
    }
}
