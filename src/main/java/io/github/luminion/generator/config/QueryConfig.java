package io.github.luminion.generator.config;

import io.github.luminion.generator.naming.ExtraFieldStrategy;
import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.naming.DefaultExtraFieldStrategy;
import io.github.luminion.generator.enums.RuntimeClass;
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
public class QueryConfig implements TemplateRender {
    private final Configurer configurer;

    /**
     * id查询方法名
     */
    @RenderField
    private String queryByIdMethodName = "voById";

    /**
     * 列表查询方法名
     */
    @RenderField
    private String queryListMethodName = "voList";

    /**
     * 分页查询方法名
     */
    @RenderField
    private String queryPageMethodName = "voPage";

    /**
     * 额外字段后缀
     */
    @RenderField
    private Map<String, String> extraFieldSuffixMap = new LinkedHashMap<>();

    /**
     * 额外字段策略
     */
    @RenderField
    private ExtraFieldStrategy extraFieldStrategy = new DefaultExtraFieldStrategy();

    /**
     * 当前页码参数名
     */
    @RenderField
    private String pageParamName = "current";

    /**
     * 每页条数参数名
     */
    @RenderField
    private String sizeParamName = "size";

    /**
     * 查询参数-分页字段
     */
    private boolean queryParamPageFields = true;

    /**
     * 查询dto父类全限定名
     */
    private String queryParamSuperClass;

    /**
     * 查询vo父类全限定名
     */
    private String queryResultSuperClass;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);

        if (queryParamSuperClass != null) {
            data.put("queryParamSuperClass", ClassUtils.getSimpleName(queryParamSuperClass));
            data.put("queryParamSuperClassCanonicalName", queryParamSuperClass);
        }

        if (queryResultSuperClass != null) {
            data.put("queryResultSuperClass", ClassUtils.getSimpleName(queryResultSuperClass));
            data.put("queryResultSuperClassCanonicalName", queryResultSuperClass);
        }

        String pageCapitalName = pageParamName.substring(0, 1).toUpperCase() + pageParamName.substring(1);
        String sizeCapitalName = sizeParamName.substring(0, 1).toUpperCase() + sizeParamName.substring(1);
        data.put("pageCapitalName", pageCapitalName);
        data.put("sizeCapitalName", sizeCapitalName);

        // 导包
        data.putAll(resolveQueryParamImports(tableInfo));
        data.putAll(resolveQueryResultImports(tableInfo));

        return data;
    }

    private Map<String, Object> resolveQueryParamImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        // 额外in查询
        importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());

        // 表字段
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            Optional.ofNullable(field.getPropertyPkg()).ifPresent(importPackages::add);
        }

        // 父类
        if (queryParamSuperClass != null) {
            importPackages.add(queryParamSuperClass);
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());

        // 导包数据
        HashMap<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("queryParamFramePkg", frameworkPackages);
        data.put("queryParamJavaPkg", javaPackages);
        return data;
    }

    private Map<String, Object> resolveQueryResultImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            Optional.ofNullable(field.getPropertyPkg()).ifPresent(importPackages::add);
        }

        // 父类
        if (queryResultSuperClass != null) {
            importPackages.add(queryResultSuperClass);
        }
        // 全局包
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());

        // 导包数据
        HashMap<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("queryResultFramePkg", frameworkPackages);
        data.put("queryResultJavaPkg", javaPackages);
        return data;
    }
}
