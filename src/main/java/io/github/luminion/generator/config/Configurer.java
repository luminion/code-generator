package io.github.luminion.generator.config;

import io.github.luminion.generator.common.ExtraFieldStrategy;
import io.github.luminion.generator.common.support.JdbcTableInfoProvider;
import io.github.luminion.generator.config.v2.*;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TableSuffixField;
import io.github.luminion.generator.po.TemplateFile;
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
    private final StrategyConfig strategyConfig;

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
        this.strategyConfig = new StrategyConfig(this);

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
        List<TemplateFile> templateFiles = getTemplateConfig().getTemplateFiles();

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
    
        // 渲染模板数据
        result.putAll(this.globalConfig.renderData(tableInfo));
        result.putAll(this.strategyConfig.renderData(tableInfo));
//        result.putAll(this.templateConfig.renderData(tableInfo));
        
        result.putAll(this.controllerConfig.renderData(tableInfo));
        result.putAll(this.serviceConfig.renderData(tableInfo));
        result.putAll(this.mapperConfig.renderData(tableInfo));
        
        result.putAll(this.commandConfig.renderData(tableInfo));
        result.putAll(this.queryConfig.renderData(tableInfo));
        result.putAll(this.excelConfig.renderData(tableInfo));
        

        // 表信息
        result.put("table", tableInfo);
        // 类名
        result.putAll(this.getOutputClassSimpleNameMap(tableInfo));
        
        // 类包
        result.put("package", this.getOutputClassPackageInfoMap());
        // 类全名
        result.put("class", this.getOutputClassNameMap(tableInfo));
        // 类是否生成
        result.put("generate", this.getOutputClassGenerateMap());
        
        
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
