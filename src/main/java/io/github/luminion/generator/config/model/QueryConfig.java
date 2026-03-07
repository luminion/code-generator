package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.MultiTemplateModelRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.ConfigResolver;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询功能配置
 * <p>
 * 整合查询参数(QueryDTO)和查询结果(QueryVO)的配置
 *
 * @author luminion
 * @since 1.0.0
 */
@Data
public class QueryConfig implements MultiTemplateModelRender {

    /**
     * 查询参数模板文件
     */
    private TemplateFile queryParamTemplateFile = new TemplateFile(
            TemplateFileEnum.QUERY_DTO.getKey(),
            "%sQueryDTO",
            "model.dto",
            "/templates/model/queryDTO.java",
            ".java"
    );

    /**
     * 查询结果模板文件
     */
    private TemplateFile queryResultTemplateFile = new TemplateFile(
            TemplateFileEnum.QUERY_VO.getKey(),
            "%sVO",
            "model.vo",
            "/templates/model/queryVO.java",
            ".java"
    );

    /**
     * 查询参数是否继承实体类
     */
    private boolean queryParamExtendsEntity = false;

    /**
     * 查询结果是否继承实体类
     */
    private boolean queryResultExtendsEntity = false;

    @Override
    public void init() {
    }

    @Override
    public void renderDataPreProcess(TableInfo tableInfo) {
        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        // 关闭功能
        if (!globalConfig.isGenerateSelectByXml()) {
            queryParamTemplateFile.setGenerate(false);
            queryResultTemplateFile.setGenerate(false);
        }
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        Set<String> importPackages = new TreeSet<>();

        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        // 查询参数渲染数据
        renderQueryParamData(tableInfo, data, importPackages, configResolver, globalConfig);

        // 查询结果渲染数据
        renderQueryResultData(tableInfo, data, importPackages, configResolver, globalConfig);

        return data;
    }

    private void renderQueryParamData(TableInfo tableInfo, Map<String, Object> data,
                                      Set<String> importPackages, ConfigResolver configResolver,
                                      GlobalConfig globalConfig) {
        importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());

        if (queryParamExtendsEntity) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
            if (globalConfig.isLombok()) {
                importPackages.add(RuntimeClass.LOMBOK_EQUALS_AND_HASH_CODE.getClassName());
            }
            data.put("queryDTOExtendsEntity", queryParamExtendsEntity);
        }
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            Optional.ofNullable(field.getJavaType().getPkg()).ifPresent(importPackages::add);
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelSerializableImportPackages());
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelLombokImportPackages());

        // 导入包
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("queryDTOFramePkg", frameworkPackages);
        data.put("queryDTOJavaPkg", javaPackages);
    }

    private void renderQueryResultData(TableInfo tableInfo, Map<String, Object> data,
                                       Set<String> importPackages, ConfigResolver configResolver,
                                       GlobalConfig globalConfig) {
        if (queryResultExtendsEntity) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
            if (globalConfig.isLombok()) {
                importPackages.add(RuntimeClass.LOMBOK_EQUALS_AND_HASH_CODE.getClassName());
            }
            data.put("queryVOExtendsEntity", queryResultExtendsEntity);
        } else {
            for (TableField field : tableInfo.getFields()) {
                if (field.isLogicDeleteField()) {
                    continue;
                }
                Optional.ofNullable(field.getJavaType().getPkg()).ifPresent(importPackages::add);
            }
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelSerializableImportPackages());
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelLombokImportPackages());

        // 导入包
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));

        data.put("queryVOFramePkg", frameworkPackages);
        data.put("queryVOJavaPkg", javaPackages);
    }

    @Override
    public void renderDataPostProcess(TableInfo tableInfo, Map<String, Object> renderData) {
    }

    @Override
    public List<TemplateFile> renderTemplateFiles() {
        List<TemplateFile> files = new ArrayList<>();
        files.add(queryParamTemplateFile);
        files.add(queryResultTemplateFile);
        return files;
    }
}
