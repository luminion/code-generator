package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.ExcelApi;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
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
     * excel导入方法名
     */
    @RenderField
    private String excelImportMethodName = "excelImport";
    
    /**
     * excel导入模板方法名
     */
    @RenderField
    private String excelImportTemplateMethodName = "excelTemplate";
    
    /**
     * excel导出方法名
     */
    @RenderField
    private String excelExportMethodName = "excelExport";


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
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();


        String excelIgnoreUnannotated = excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_IGNORE_UNANNOTATED.getCanonicalName();
        String excelProperty = excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_PROPERTY.getCanonicalName();
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
            if (commandConfig.getCommandExcludeColumns().contains(field.getColumnName())) {
                continue;
            }
            String propertyPkg = field.getPropertyPkg();
            Optional.ofNullable(propertyPkg).ifPresent(importPackages::add);
        }
        

        // 全局包
//        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());
        
        // excel框架不支持链式setter, 会导致无法写入值
        importPackages.remove(RuntimeClass.LOMBOK_ACCESSORS.getCanonicalName());

        // 导包数据
        HashMap<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("excelImportParamFramePkg", frameworkPackages);
        data.put("excelImportParamJavaPkg", javaPackages);
        return data;
    }
    

    private Map<String,Object> resolveExcelExportDtoImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        String excelIgnoreUnannotated = excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_IGNORE_UNANNOTATED.getCanonicalName();
        String excelProperty = excelApi.getPackagePrefix() + RuntimeClass.PREFIX_EXCEL_EXCEL_PROPERTY.getCanonicalName();
        importPackages.add(excelIgnoreUnannotated);
        importPackages.add(excelProperty);

        // 属性过滤
        for (TableField field : tableInfo.getFields()) {
            String propertyPkg = field.getPropertyPkg();
            Optional.ofNullable(propertyPkg).ifPresent(importPackages::add);
        }
        

        // 全局包
//        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());

//        // excel框架不支持链式setter, 会导致无法写入值
//        importPackages.remove(RuntimeClass.LOMBOK_ACCESSORS.getClassName());

        // 导包数据
        HashMap<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("excelExportParamFramePkg", frameworkPackages);
        data.put("excelExportParamJavaPkg", javaPackages);
        return data;
    }


}
