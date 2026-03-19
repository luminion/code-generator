package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.RenderField;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.ExcelApi;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateClassFile;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ExcelConfig implements TemplateRender {
    private final Configurer configurer;

    /**
     * excel api
     */
    private ExcelApi excelApi = ExcelApi.EASY_EXCEL;

    /**
     * 生成导入方法及配套类(需允许新增)
     */
    @RenderField
    private boolean enableExcelImport = true;

    /**
     * 生成导出方法(需允许查询)
     */
    @RenderField
    private boolean enableExcelExport = true;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        
        data.put("excelApiPackagePrefix", excelApi.getPackagePrefix());
        data.put("excelApiClass", excelApi.getMainEntrance());

        // 导包
        data.putAll(resolveExcelImportDtoImports(tableInfo));
        data.putAll(resolveExcelExportDtoImports(tableInfo));
        
        return data;
    }

    private Map<String, Object> resolveExcelImportDtoImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField primaryKeyField = tableInfo.getPrimaryKeyField();
        String primaryKeyFieldPropertyPkg = primaryKeyField != null ? primaryKeyField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();


        String excelIgnoreUnannotated = excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_IGNORE_UNANNOTATED.getClassName();
        String excelProperty = excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_PROPERTY.getClassName();
        importPackages.add(excelIgnoreUnannotated);
        importPackages.add(excelProperty);


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
            if (commandConfig.getEditExcludeColumns().contains(field.getColumnName())) {
                continue;
            }
            String propertyPkg = field.getPropertyPkg();
            Optional.ofNullable(propertyPkg).ifPresent(importPackages::add);
        }
        

        // 全局包
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());
        
        // excel框架不支持链式setter, 会导致无法写入值
        importPackages.remove(RuntimeClass.LOMBOK_ACCESSORS.getClassName());

        // 导包数据
        HashMap<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("excelImportDtoFramePkg", frameworkPackages);
        data.put("excelImportDtoJavaPkg", javaPackages);
        return data;
    }
    

    private Map<String,Object> resolveExcelExportDtoImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField primaryKeyField = tableInfo.getPrimaryKeyField();
        String primaryKeyFieldPropertyPkg = primaryKeyField != null ? primaryKeyField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        String excelIgnoreUnannotated = excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_IGNORE_UNANNOTATED.getClassName();
        String excelProperty = excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_PROPERTY.getClassName();
        importPackages.add(excelIgnoreUnannotated);
        importPackages.add(excelProperty);

        // 属性过滤
        for (TableField field : tableInfo.getFields()) {
            String propertyPkg = field.getPropertyPkg();
            Optional.ofNullable(propertyPkg).ifPresent(importPackages::add);
        }
        

        // 全局包
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());

        // excel框架不支持链式setter, 会导致无法写入值
        importPackages.remove(RuntimeClass.LOMBOK_ACCESSORS.getClassName());

        // 导包数据
        HashMap<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("excelExportDtoFramePkg", frameworkPackages);
        data.put("excelExportDtoJavaPkg", javaPackages);
        return data;
    }


}
