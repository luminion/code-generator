package io.github.luminion.generator.config.core;

import io.github.luminion.generator.common.RenderField;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateClassFile;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ServiceConfig implements TemplateRender {
    private final Configurer configurer;

    /**
     * 自定义继承的Service类全称，带包名
     */
    @RenderField
    protected String serviceSuperClass;

    /**
     * 自定义继承的ServiceImpl类全称，带包名
     */
    @RenderField
    protected String serviceImplSuperClass;

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);

        if (serviceSuperClass != null) {
            data.put("serviceSuperClass", ClassUtils.getSimpleName(serviceSuperClass));
        }
        if (serviceImplSuperClass != null) {
            data.put("serviceImplSuperClass", ClassUtils.getSimpleName(serviceImplSuperClass));
        }

        // 导包数据
        data.putAll(resolveServiceImports(tableInfo));
        data.putAll(resolveServiceImplImports(tableInfo));
        return data;
    }


    private Map<String, Object> resolveServiceImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        if (serviceSuperClass != null) {
            importPackages.add(serviceSuperClass);
        }
        
        // 运行环境
        if (RuntimeEnv.MP_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
            TemplateClassFile queryResult = templateFileMap.get(TemplateFileEnum.QUERY_RESULT.getKey());
            importPackages.add(queryResult.getClassCanonicalName());
            importPackages.add(RuntimeClass.SQL_BOOSTER_MP_SERVICE.getClassName());
        }
        if (RuntimeEnv.MYBATIS_PLUS.equals(globalConfig.getRuntimeEnv())) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_BASE_MAPPER.getClassName());
            // 根据ID查询
            if (globalConfig.isGenerateQueryById() && idField != null){
                TemplateClassFile queryResult = templateFileMap.get(TemplateFileEnum.QUERY_RESULT.getKey());
                importPackages.add(queryResult.getClassCanonicalName());
                if (idFieldPropertyPkg != null) {
                    importPackages.add(idFieldPropertyPkg);
                }
            }
            // 列表查询
            if (globalConfig.isGenerateQueryList()){
                TemplateClassFile queryResult = templateFileMap.get(TemplateFileEnum.QUERY_RESULT.getKey());
                importPackages.add(queryResult.getClassCanonicalName());
                TemplateClassFile queryParam = templateFileMap.get(TemplateFileEnum.QUERY_PARAM.getKey());
                importPackages.add(queryParam.getClassCanonicalName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
            }
            // 分页查询
            if (globalConfig.isGenerateQueryPage()){
                TemplateClassFile queryResult = templateFileMap.get(TemplateFileEnum.QUERY_RESULT.getKey());
                importPackages.add(queryResult.getClassCanonicalName());
                TemplateClassFile queryParam = templateFileMap.get(TemplateFileEnum.QUERY_PARAM.getKey());
                importPackages.add(queryParam.getClassCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getClassName());
            }
        }
        // 创建
        if (globalConfig.isGenerateCreate()){
            TemplateClassFile createParam = templateFileMap.get(TemplateFileEnum.CREATE_PARAM.getKey());
            importPackages.add(createParam.getClassCanonicalName());
            if (idFieldPropertyPkg != null){
                importPackages.add(idFieldPropertyPkg);
            }
        }
        // 更新
        if (globalConfig.isGenerateUpdate() && idField != null){
            TemplateClassFile updateParam = templateFileMap.get(TemplateFileEnum.UPDATE_PARAM.getKey());
            importPackages.add(updateParam.getClassCanonicalName());
            if (idFieldPropertyPkg != null){
                importPackages.add(idFieldPropertyPkg);
            }
        }
        // 删除
        if (globalConfig.isGenerateDelete() && idField != null){
            if (idFieldPropertyPkg != null){
                importPackages.add(idFieldPropertyPkg);
            }
        }
        // excel导入
        if (globalConfig.isGenerateExcelImport()){
            importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getClassName());
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
        }
        // excel导出
        if (globalConfig.isGenerateExcelExport()){
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
        }


        // 导包数据
        HashMap<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("serviceFramePkg", frameworkPackages);
        data.put("serviceJavaPkg", javaPackages);
        return data;
    }


    private Map<String, Object> resolveServiceImplImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        if (serviceImplSuperClass != null) {
            importPackages.add(serviceImplSuperClass);
        }

        // 运行环境
        if (RuntimeEnv.MP_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
            TemplateClassFile queryResult = templateFileMap.get(TemplateFileEnum.QUERY_RESULT.getKey());
            importPackages.add(queryResult.getClassCanonicalName());
            importPackages.add(RuntimeClass.SQL_BOOSTER_MP_SERVICE.getClassName());
        }
        if (RuntimeEnv.MYBATIS_PLUS.equals(globalConfig.getRuntimeEnv())) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_BASE_MAPPER.getClassName());
            // 根据ID查询
            if (globalConfig.isGenerateQueryById() && idField != null){
                TemplateClassFile queryResult = templateFileMap.get(TemplateFileEnum.QUERY_RESULT.getKey());
                importPackages.add(queryResult.getClassCanonicalName());
                TemplateClassFile queryParam = templateFileMap.get(TemplateFileEnum.QUERY_PARAM.getKey());
                importPackages.add(queryParam.getClassCanonicalName());
                if (idFieldPropertyPkg != null) {
                    importPackages.add(idFieldPropertyPkg);
                }
            }
            // 列表查询
            if (globalConfig.isGenerateQueryList()){
                TemplateClassFile queryResult = templateFileMap.get(TemplateFileEnum.QUERY_RESULT.getKey());
                importPackages.add(queryResult.getClassCanonicalName());
                TemplateClassFile queryParam = templateFileMap.get(TemplateFileEnum.QUERY_PARAM.getKey());
                importPackages.add(queryParam.getClassCanonicalName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
            }
            // 分页查询
            if (globalConfig.isGenerateQueryPage()){
                TemplateClassFile queryResult = templateFileMap.get(TemplateFileEnum.QUERY_RESULT.getKey());
                importPackages.add(queryResult.getClassCanonicalName());
                TemplateClassFile queryParam = templateFileMap.get(TemplateFileEnum.QUERY_PARAM.getKey());
                importPackages.add(queryParam.getClassCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getClassName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_PAGE.getClassName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
            }
        }
        // 创建
        if (globalConfig.isGenerateCreate()){
            TemplateClassFile createParam = templateFileMap.get(TemplateFileEnum.CREATE_PARAM.getKey());
            importPackages.add(createParam.getClassCanonicalName());
            if (idFieldPropertyPkg != null){
                importPackages.add(idFieldPropertyPkg);
            }
        }
        // 更新
        if (globalConfig.isGenerateUpdate() && idField != null){
            TemplateClassFile updateParam = templateFileMap.get(TemplateFileEnum.UPDATE_PARAM.getKey());
            importPackages.add(updateParam.getClassCanonicalName());
            if (idFieldPropertyPkg != null){
                importPackages.add(idFieldPropertyPkg);
            }
        }
        // 删除
        if (globalConfig.isGenerateDelete() && idField != null){
            if (idFieldPropertyPkg != null){
                importPackages.add(idFieldPropertyPkg);
            }
        }

        String excelMain = excelConfig.getExcelApi().getMainEntrance();
        String excelPackagePrefix = excelConfig.getExcelApi().getPackagePrefix();
        String excelClass = excelPackagePrefix + excelMain;
        String longestMatchColumnWidthStyleStrategyClass = excelPackagePrefix + RuntimeClass.PREFIX_EXCEL_LONGEST_MATCH_COLUMN_WIDTH_STYLE_STRATEGY.getClassName();
        
        // excel导入
        if (globalConfig.isGenerateExcelImport()){
            TemplateClassFile excelImportParam = templateFileMap.get(TemplateFileEnum.EXCEL_IMPORT_PARAM.getKey());
            importPackages.add(excelImportParam.getClassCanonicalName());
            importPackages.add(excelClass);
            importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getClassName());
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
            importPackages.add(RuntimeClass.JAVA_UTIL_COLLECTIONS.getClassName());
            importPackages.add(RuntimeClass.JAVA_STREAM_COLLECTORS.getClassName());
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
            importPackages.add(RuntimeClass.SPRING_BOOT_BEAN_UTILS.getClassName());

        }
        // excel导出
        if (globalConfig.isGenerateExcelExport()){
            TemplateClassFile excelExportParam = templateFileMap.get(TemplateFileEnum.EXCEL_EXPORT_PARAM.getKey());
            importPackages.add(excelExportParam.getClassCanonicalName());
            importPackages.add(excelClass);
            importPackages.add(longestMatchColumnWidthStyleStrategyClass);
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
        }


        // 导包数据
        HashMap<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("serviceImplFramePkg", frameworkPackages);
        data.put("serviceImplJavaPkg", javaPackages);
        return data;
    }
}
