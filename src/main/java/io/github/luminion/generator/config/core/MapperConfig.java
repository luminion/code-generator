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
public class MapperConfig implements TemplateRender {
    private final Configurer configurer;

    /**
     * 自定义继承的Mapper类全称，带包名
     */
    protected String mapperSuperClass;

    /**
     * 自定义Mapper类上的注解，带包名
     */
    protected String mapperAnnotationClass = "org.apache.ibatis.annotations.Mapper";

    /**
     * 是否开启mapperBaseColumnList
     *
     */
    @RenderField
    protected boolean mapperBaseColumnList;

    /**
     * 是否开启BaseResultMap
     *
     */
    @RenderField
    protected boolean mapperBaseResultMap;

    /**
     * 缓存类
     */
    @RenderField
    protected String mapperCacheClass;

    /**
     * 排序字段map
     * 字段名 -> 是否倒序
     */
    protected Map<String, Boolean> xmlOrderColumnMap = new LinkedHashMap<>();


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        data.put("mapperSuperClass", ClassUtils.getSimpleName(mapperSuperClass));
        data.put("mapperAnnotationClass", "@" + ClassUtils.getSimpleName(mapperAnnotationClass));

        // 默认排序字段sql
        List<TableField> fields = tableInfo.getFields();
        List<String> existColumnNames = fields.stream().map(TableField::getColumnName).collect(Collectors.toList());
        if (xmlOrderColumnMap != null && !xmlOrderColumnMap.isEmpty()) {
            xmlOrderColumnMap.entrySet().stream()
                    .filter(e -> existColumnNames.contains(e.getKey()))
                    .map(e -> String.format("a.%s%s", e.getKey(), e.getValue() ? " DESC" : ""))
                    .reduce((e1, e2) -> e1 + ", " + e2)
                    .ifPresent(e -> data.put("orderColumnSql", e));
        }

        // 导包
        data.putAll(resolveMapperImports(tableInfo));
        return data;
    }

    private Map<String, Object> resolveMapperImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        if (mapperSuperClass != null) {
            importPackages.add(mapperSuperClass);
        }
        if (mapperAnnotationClass != null) {
            importPackages.add(mapperAnnotationClass);
        }

        // 运行环境
        if (RuntimeEnv.MP_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
            importPackages.add(RuntimeClass.SQL_BOOSTER_MP_MAPPER.getClassName());
            TemplateClassFile queryResult = templateFileMap.get(TemplateFileEnum.QUERY_RESULT.getKey());
            importPackages.add(queryResult.getClassCanonicalName());
        }
        if (RuntimeEnv.MYBATIS_PLUS.equals(globalConfig.getRuntimeEnv())) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_BASE_MAPPER.getClassName());
            // 查询相关
            if (globalConfig.isGenerateQueryById() || globalConfig.isGenerateQueryList() || globalConfig.isGenerateQueryPage() || globalConfig.isGenerateExcelExport()) {
                TemplateClassFile queryParam = templateFileMap.get(TemplateFileEnum.QUERY_PARAM.getKey());
                TemplateClassFile queryResult = templateFileMap.get(TemplateFileEnum.QUERY_RESULT.getKey());
                importPackages.add(queryParam.getClassCanonicalName());
                importPackages.add(queryResult.getClassCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getClassName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
            }
        }

        // 导包数据
        HashMap<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("mapperFramePkg", frameworkPackages);
        data.put("mapperJavaPkg", javaPackages);
        return data;
    }

}
