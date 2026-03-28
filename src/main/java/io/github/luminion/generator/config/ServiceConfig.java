package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.enums.TemplateEnum;
import io.github.luminion.generator.metadata.InvokeInfo;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
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

        TemplateClassFile entity = templateFileMap.get(TemplateEnum.ENTITY.getKey());
        importPackages.add(entity.getClassCanonicalName());


        // 运行环境
        if (RuntimeEnv.MP_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
            TemplateClassFile queryResult = templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey());
            importPackages.add(queryResult.getClassCanonicalName());
            importPackages.add(RuntimeClass.SQL_BOOSTER_MP_SERVICE.getCanonicalName());
        }
        if (RuntimeEnv.MYBATIS_PLUS.equals(globalConfig.getRuntimeEnv())) {
            // 根据ID查询
            if (globalConfig.isGenerateQueryById() && idField != null) {
                TemplateClassFile queryResult = templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey());
                importPackages.add(queryResult.getClassCanonicalName());
                if (idFieldPropertyPkg != null) {
                    importPackages.add(idFieldPropertyPkg);
                }
            }
            // 列表查询
            if (globalConfig.isGenerateQueryList()) {
                TemplateClassFile queryResult = templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey());
                importPackages.add(queryResult.getClassCanonicalName());
                TemplateClassFile queryParam = templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey());
                importPackages.add(queryParam.getClassCanonicalName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            }
            // 分页查询
            if (globalConfig.isGenerateQueryPage()) {
                TemplateClassFile queryResult = templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey());
                importPackages.add(queryResult.getClassCanonicalName());
                TemplateClassFile queryParam = templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey());
                importPackages.add(queryParam.getClassCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getCanonicalName());
                InvokeInfo pageType = configurer.getControllerConfig().getPageType();
                String pageClassCanonicalName = pageType.getClassCanonicalName();
                if (pageClassCanonicalName !=null && !pageClassCanonicalName.isEmpty()){
                    importPackages.add(pageClassCanonicalName);
                }
            }
        }
        // 创建
        if (globalConfig.isGenerateCreate()) {
            TemplateClassFile createParam = templateFileMap.get(TemplateEnum.CREATE_PARAM.getKey());
            importPackages.add(createParam.getClassCanonicalName());
            if (idFieldPropertyPkg != null) {
                importPackages.add(idFieldPropertyPkg);
            }
        }
        // 更新
        if (globalConfig.isGenerateUpdate() && idField != null) {
            TemplateClassFile updateParam = templateFileMap.get(TemplateEnum.UPDATE_PARAM.getKey());
            importPackages.add(updateParam.getClassCanonicalName());
            if (idFieldPropertyPkg != null) {
                importPackages.add(idFieldPropertyPkg);
            }
        }
        // 删除
        if (globalConfig.isGenerateDelete() && idField != null) {
            if (idFieldPropertyPkg != null) {
                importPackages.add(idFieldPropertyPkg);
            }
        }
        // excel导入
        if (globalConfig.isGenerateExcelImport()) {
            importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getCanonicalName());
        }
        // excel导出
        if (globalConfig.isGenerateExcelExport()) {
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getCanonicalName());
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getClassCanonicalName());
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

        importPackages.add(templateFileMap.get(TemplateEnum.ENTITY.getKey()).getClassCanonicalName());

        importPackages.add(templateFileMap.get(TemplateEnum.MAPPER.getKey()).getClassCanonicalName());

        TemplateClassFile service = templateFileMap.get(TemplateEnum.SERVICE.getKey());
        if (service.isGenerate()) {
            importPackages.add(service.getClassCanonicalName());
        }
        importPackages.add(RuntimeClass.SPRING_BOOT_SERVICE.getCanonicalName());

        // 运行环境
        if (RuntimeEnv.MP_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getClassCanonicalName());
        }
        if (RuntimeEnv.MYBATIS_PLUS.equals(globalConfig.getRuntimeEnv())) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_BASE_MAPPER.getCanonicalName());
            // 根据ID查询
            if (globalConfig.isGenerateQueryById() && idField != null) {
                TemplateClassFile queryResult = templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey());
                importPackages.add(queryResult.getClassCanonicalName());
                TemplateClassFile queryParam = templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey());
                importPackages.add(queryParam.getClassCanonicalName());
                if (idFieldPropertyPkg != null) {
                    importPackages.add(idFieldPropertyPkg);
                }
            }
            // 列表查询
            if (globalConfig.isGenerateQueryList()) {
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getClassCanonicalName());
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getClassCanonicalName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            }
            // 分页查询
            if (globalConfig.isGenerateQueryPage()) {
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getClassCanonicalName());
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getClassCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_PAGE.getCanonicalName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
                InvokeInfo pageType = configurer.getControllerConfig().getPageType();
                String pageClassCanonicalName = pageType.getClassCanonicalName();
                if (pageClassCanonicalName !=null && !pageClassCanonicalName.isEmpty()){
                    importPackages.add(pageClassCanonicalName);
                }
            }
        }
        // 创建
        if (globalConfig.isGenerateCreate()) {
            importPackages.add(templateFileMap.get(TemplateEnum.CREATE_PARAM.getKey()).getClassCanonicalName());
            if (idFieldPropertyPkg != null) {
                importPackages.add(idFieldPropertyPkg);
            }
        }
        // 更新
        if (globalConfig.isGenerateUpdate() && idField != null) {
            importPackages.add(templateFileMap.get(TemplateEnum.UPDATE_PARAM.getKey()).getClassCanonicalName());
            if (idFieldPropertyPkg != null) {
                importPackages.add(idFieldPropertyPkg);
            }
        }
        // 删除
        if (globalConfig.isGenerateDelete() && idField != null) {
            if (idFieldPropertyPkg != null) {
                importPackages.add(idFieldPropertyPkg);
            }
        }

        String excelMain = excelConfig.getExcelApi().getMainEntrance();
        String excelPackagePrefix = excelConfig.getExcelApi().getPackagePrefix();
        String excelClass = excelPackagePrefix + excelMain;
        String longestMatchColumnWidthStyleStrategyClass = excelPackagePrefix + RuntimeClass.PREFIX_EXCEL_LONGEST_MATCH_COLUMN_WIDTH_STYLE_STRATEGY.getCanonicalName();

        // excel导入
        if (globalConfig.isGenerateExcelImport()) {
            importPackages.add(templateFileMap.get(TemplateEnum.EXCEL_IMPORT_PARAM.getKey()).getClassCanonicalName());
            importPackages.add(excelClass);
            importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_UTIL_COLLECTIONS.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_STREAM_COLLECTORS.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            importPackages.add(RuntimeClass.SPRING_BOOT_BEAN_UTILS.getCanonicalName());

        }
        // excel导出
        if (globalConfig.isGenerateExcelExport()) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getClassCanonicalName());
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getClassCanonicalName());
            importPackages.add(templateFileMap.get(TemplateEnum.EXCEL_EXPORT_PARAM.getKey()).getClassCanonicalName());
            importPackages.add(excelClass);
            importPackages.add(longestMatchColumnWidthStyleStrategyClass);
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getCanonicalName());
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
