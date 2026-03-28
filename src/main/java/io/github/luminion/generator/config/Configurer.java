package io.github.luminion.generator.config;

import io.github.luminion.generator.datasource.support.JdbcTableInfoProvider;
import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.internal.render.TemplateGenerationPlanner;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

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

    private final TemplateGenerationPlanner templateGenerationPlanner;

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

        this.templateGenerationPlanner = new TemplateGenerationPlanner(this);
    }

    public List<TableInfo> queryTableInfos() {
        try {
            JdbcTableInfoProvider tableInfoProvider = new JdbcTableInfoProvider(this.dataSourceConfig);
            List<TableInfo> tableInfos = tableInfoProvider.queryTables();
            if (tableInfos.isEmpty()) {
                log.warn("No matching tables found");
            }
            return tableInfos;
        } catch (Exception exception) {
            throw new RuntimeException("创建IDatabaseQuery实例出现错误:", exception);
        }
    }

    public RenderContext createRenderContext(TableInfo tableInfo) {
        return templateGenerationPlanner.plan(tableInfo);
    }

    public Map<String, TemplateClassFile> resolveTemplateFiles(TableInfo tableInfo) {
        return createRenderContext(tableInfo).getTemplateFiles();
    }

    public Map<String, Object> renderMap(TableInfo tableInfo) {
        return renderMap(createRenderContext(tableInfo));
    }

    public Map<String, Object> renderMap(RenderContext context) {
        HashMap<String, Object> result = collectRenderData(context);
        result.put("table", context.getTableInfo());
        result.putAll(context.getTemplateFiles());
        applyCustomRenderData(context.getTableInfo(), result);
        return result;
    }

    public Map<String, Object> renderMap(TableInfo tableInfo, Map<String, TemplateClassFile> templateFileMap) {
        return renderMap(new RenderContext(tableInfo, templateFileMap));
    }

    private HashMap<String, Object> collectRenderData(RenderContext context) {
        HashMap<String, Object> result = new HashMap<>();
        result.putAll(this.globalConfig.renderData(context));
        result.putAll(this.controllerConfig.renderData(context));
        result.putAll(this.serviceConfig.renderData(context));
        result.putAll(this.mapperConfig.renderData(context));
        result.putAll(this.entityConfig.renderData(context));
        result.putAll(this.commandConfig.renderData(context));
        result.putAll(this.queryConfig.renderData(context));
        result.putAll(this.excelConfig.renderData(context));
        return result;
    }

    private void applyCustomRenderData(TableInfo tableInfo, Map<String, Object> result) {
        Map<String, Object> customRenderData = this.globalConfig.getCustomRenderData();
        result.putAll(customRenderData);
        BiConsumer<TableInfo, Map<String, Object>> beforeGenerate = this.globalConfig.getCustomRenderLogic();
        if (beforeGenerate != null) {
            beforeGenerate.accept(tableInfo, result);
        }
    }
}