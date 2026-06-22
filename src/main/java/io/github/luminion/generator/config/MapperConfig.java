package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.enums.TemplateEnum;
import io.github.luminion.generator.internal.render.ImportPackageSupport;
import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class MapperConfig implements TemplateRender {
    private final Configurer configurer;

    protected String mapperSuperClass;
    protected String mapperAnnotationClass;

    @RenderField
    protected boolean mapperBaseColumnList;

    @RenderField
    protected boolean mapperBaseResultMap;

    @RenderField
    protected String mapperCacheClass;

    protected Map<String, Boolean> xmlOrderColumnMap = new LinkedHashMap<>();

    @Override
    public Map<String, Object> renderData(RenderContext context) {
        TableInfo tableInfo = context.getTableInfo();
        Map<String, Object> data = TemplateRender.super.renderData(context);
        data.put("mapperSuperClass", ClassUtils.getSimpleName(mapperSuperClass));
        data.put("mapperAnnotationClass", ClassUtils.getSimpleName(mapperAnnotationClass));

        List<TableField> fields = tableInfo.getFields();
        List<String> existColumnNames = fields.stream().map(TableField::getColumnName).collect(Collectors.toList());
        if (xmlOrderColumnMap != null && !xmlOrderColumnMap.isEmpty()) {
            xmlOrderColumnMap.entrySet().stream()
                    .filter(entry -> existColumnNames.contains(entry.getKey()))
                    .map(entry -> String.format("a.%s%s", entry.getKey(), entry.getValue() ? " DESC" : ""))
                    .reduce((left, right) -> left + ", " + right)
                    .ifPresent(sql -> data.put("orderColumnSql", sql));
        }
        data.putAll(resolveMapperImports(context));
        return data;
    }

    private Map<String, Object> resolveMapperImports(RenderContext context) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Map<String, TemplateClassFile> templateFileMap = context.getTemplateFiles();
        Set<String> importPackages = new TreeSet<>();

        ImportPackageSupport.addIfPresent(importPackages, mapperSuperClass);
        ImportPackageSupport.addIfPresent(importPackages, mapperAnnotationClass);
        importPackages.add(templateFileMap.get(TemplateEnum.ENTITY.getKey()).getFullyQualifiedClassName());

        if (RuntimeEnv.MP_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
            importPackages.add(RuntimeClass.SQL_BOOSTER_MP_MAPPER.getCanonicalName());
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
        }
        if (RuntimeEnv.MYBATIS_PLUS.equals(globalConfig.getRuntimeEnv())) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_BASE_MAPPER.getCanonicalName());
            if (globalConfig.isGenerateQueryById() || globalConfig.isGenerateQueryList() || globalConfig.isGenerateQueryPage() || globalConfig.isGenerateExcelExport()) {
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
                importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getCanonicalName());
                importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            }
        }

        return ImportPackageSupport.splitImportPackages(importPackages, "mapperFramePkg", "mapperJavaPkg");
    }
}