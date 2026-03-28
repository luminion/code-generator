package io.github.luminion.generator.config;

import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.naming.ExtraFieldStrategy;
import io.github.luminion.generator.datasource.support.JdbcTableInfoProvider;
import io.github.luminion.generator.metadata.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
@Getter
public class Configurer {
    private final DataSourceConfig dataSourceConfig;

    private final GlobalConfig globalConfig;
    private final TemplateConfig templateConfig;

    private final ControllerConfig controllerConfig;
    private final ServiceConfig serviceConfig;
    private final MapperConfig mapperConfig;
    private final EntityConfig entityConfig;

    private final CommandConfig commandConfig;
    private final QueryConfig queryConfig;
    private final ExcelConfig excelConfig;


    public Configurer(String url, String username, String password) {
        this.dataSourceConfig = new DataSourceConfig(url, username, password);

        this.globalConfig = new GlobalConfig(this);
        this.templateConfig = new TemplateConfig(this);

        this.controllerConfig = new ControllerConfig(this);
        this.serviceConfig = new ServiceConfig(this);
        this.mapperConfig = new MapperConfig(this);
        this.entityConfig = new EntityConfig(this);

        this.commandConfig = new CommandConfig(this);
        this.queryConfig = new QueryConfig(this);
        this.excelConfig = new ExcelConfig(this);
    }


    public List<TableInfo> queryTableInfos() {
        try {
            JdbcTableInfoProvider defaultQuery = new JdbcTableInfoProvider(this.dataSourceConfig);
            List<TableInfo> tableInfos = defaultQuery.queryTables();
            if (tableInfos.isEmpty()) {
                log.warn("No matching tables found");
            }
            return tableInfos;
        } catch (Exception exception) {
            throw new RuntimeException("创建IDatabaseQuery实例出现错误:", exception);
        }
    }


    public Map<String, Object> renderMap(TableInfo tableInfo) {
        HashMap<String, Object> result = new HashMap<>();

        // 处理后缀
        Set<String> existPropertyNames = tableInfo.getFields().stream()
                .map(e -> e.getPropertyName())
                .collect(Collectors.toSet());
        List<TableSuffixField> extraFields = tableInfo.getExtraFields();
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            for (Map.Entry<String, String> entry : this.queryConfig.getExtraFieldSuffixMap().entrySet()) {
                String suffix = entry.getKey();
                String sqlOperator = entry.getValue();
                ExtraFieldStrategy extraFieldStrategy = this.queryConfig.getExtraFieldStrategy();

                if (extraFieldStrategy.generateExtraField(sqlOperator, field)) {
                    String suffixPropertyName = field.getPropertyName() + suffix;
                    if (existPropertyNames.contains(suffixPropertyName)) {
                        continue;
                    }
                    existPropertyNames.add(suffixPropertyName);
                    TableSuffixField extraField = new TableSuffixField();
                    extraField.setSqlOperator(sqlOperator);
                    extraField.setPropertyType(field.getPropertyType());
                    extraField.setPropertyName(field.getPropertyName() + suffix);
                    extraField.setCapitalName(field.getCapitalName() + suffix);
                    extraField.setColumnName(field.getColumnName());
                    extraField.setComment(field.getComment());
                    extraFields.add(extraField.refactor());
                }
            }
        }

        // 逻辑处理
        if (!globalConfig.isGenerateCreate()) {
            templateConfig.getCreateParam().setGenerate(false);
        }
        if (!globalConfig.isGenerateUpdate()) {
            templateConfig.getUpdateParam().setGenerate(false);
        }
        boolean generateQuery = globalConfig.isGenerateQueryById()
                || globalConfig.isGenerateQueryList()
                || globalConfig.isGenerateQueryPage()
                || globalConfig.isGenerateExcelExport();
        if (!generateQuery) {
            templateConfig.getQueryParam().setGenerate(false);
            if (!RuntimeEnv.MP_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
                templateConfig.getQueryResult().setGenerate(false);
            }
        }
        if (!globalConfig.isGenerateExcelImport()) {
            templateConfig.getExcelImportParam().setGenerate(false);
        }
        if (!globalConfig.isGenerateExcelExport()) {
            templateConfig.getExcelExportParam().setGenerate(false);
        }


        // 渲染模板数据
        result.putAll(this.globalConfig.renderData(tableInfo));

        result.putAll(this.controllerConfig.renderData(tableInfo));
        result.putAll(this.serviceConfig.renderData(tableInfo));
        result.putAll(this.mapperConfig.renderData(tableInfo));
        result.putAll(this.entityConfig.renderData(tableInfo));

        result.putAll(this.commandConfig.renderData(tableInfo));
        result.putAll(this.queryConfig.renderData(tableInfo));
        result.putAll(this.excelConfig.renderData(tableInfo));


        // 表信息
        result.put("table", tableInfo);
        // 模板信息
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        result.putAll(templateFileMap);


        // 自定义扩展数据
        Map<String, Object> customRenderData = this.globalConfig.getCustomRenderData();
        result.putAll(customRenderData);
        BiConsumer<TableInfo, Map<String, Object>> beforeGenerate = this.globalConfig.getCustomRenderLogic();
        if (beforeGenerate != null) {
            beforeGenerate.accept(tableInfo, result);
        }

        return result;
    }


}
