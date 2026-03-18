package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.ExtraFieldStrategy;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.common.support.DefaultExtraFieldStrategy;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.RuntimeClass;
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
public class QueryConfig implements TemplateRender {
    private final Configurer configurer;

    /**
     * 生成id查询方法
     */
    private boolean enableSelectVoById = true;
    /**
     * 批量查询相关方法及配套类
     */
    private boolean enableSelectVoList = false;

    /**
     * 批量查询分页相关方法及配套类
     */
    private boolean enableSelectVoPage = true;

    /**
     * 额外字段后缀
     */
    private final Map<String, String> extraFieldSuffixMap = new LinkedHashMap<>();

    /**
     * 额外字段策略
     */
    private ExtraFieldStrategy extraFieldStrategy = new DefaultExtraFieldStrategy();
    /**
     * 当前页码参数名
     */
    private String pageName = "current";

    /**
     * 每页条数参数名
     */
    private String sizeName = "size";

    /**
     * 查询dto父类全限定名
     */
    private String queryDtoSuperClass;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("generateVoById", this.enableSelectVoById);
        data.put("generateVoList", this.enableSelectVoList);
        data.put("generateVoPage", this.enableSelectVoPage);
        data.put("extraFieldSuffixMap", extraFieldSuffixMap);
        data.put("extraFieldStrategy", extraFieldStrategy);
        data.put("pageName", pageName);
        data.put("sizeName", sizeName);
        if (queryDtoSuperClass != null) {
            data.put("queryDtoSuperClass", ClassUtils.getSimpleName(queryDtoSuperClass));
            data.put("queryDtoSuperClassCanonicalName", queryDtoSuperClass);
            // 假设用户没改配置，值是 "pageNum" 和 "pageSize"
            String pageGetter = "get"
                    + pageName.substring(0, 1).toUpperCase()
                    + pageName.substring(1)
                    + "()";
            String sizeGetter = "get"
                    + sizeName.substring(0, 1).toUpperCase()
                    + sizeName.substring(1)
                    + "()";
            data.put("pageGetter", pageGetter);
            data.put("sizeGetter", sizeGetter);
        }

        // 导包
        data.putAll(this.resolveQueryDtoImports(tableInfo));
        data.putAll(this.resolveQueryVoImports(tableInfo));

        return data;
    }

    private Map<String, Object> resolveQueryDtoImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField primaryKeyField = tableInfo.getPrimaryKeyField();
        String primaryKeyFieldPropertyPkg = primaryKeyField != null ? primaryKeyField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        // 额外in查询
        importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());

        // 表字段
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            Optional.ofNullable(field.getPropertyPkg()).ifPresent(importPackages::add);
        }

        // 父类
        if (queryDtoSuperClass != null) {
            importPackages.add(queryDtoSuperClass);
            if (globalConfig.isLombok()) {
                importPackages.add(RuntimeClass.LOMBOK_EQUALS_AND_HASH_CODE.getClassName());
            }
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
        data.put("queryDtoFramePkg", frameworkPackages);
        data.put("queryDtoJavaPkg", javaPackages);
        return data;
    }

    private Map<String, Object> resolveQueryVoImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField primaryKeyField = tableInfo.getPrimaryKeyField();
        String primaryKeyFieldPropertyPkg = primaryKeyField != null ? primaryKeyField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            Optional.ofNullable(field.getPropertyPkg()).ifPresent(importPackages::add);
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
        data.put("queryVoFramePkg", frameworkPackages);
        data.put("queryVoJavaPkg", javaPackages);
        return data;
    }
}
