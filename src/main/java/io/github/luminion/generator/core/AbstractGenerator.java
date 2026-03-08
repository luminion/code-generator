package io.github.luminion.generator.core;

import io.github.luminion.generator.builder.base.GlobalBuilder;
import io.github.luminion.generator.builder.base.InjectionBuilder;
import io.github.luminion.generator.builder.base.StrategyBuilder;
import io.github.luminion.generator.builder.model.*;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.engine.VelocityTemplateEngine;
import io.github.luminion.generator.util.InitializeUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.function.Function;

/**
 * @author luminion
 */
@Slf4j
public abstract class AbstractGenerator<C extends TemplateRender, B> implements LambdaGenerator<B> {
    protected final ConfigCollector<C> configCollector;

    public AbstractGenerator(ConfigCollector<C> configCollector) {
        this.configCollector = configCollector;
        InitializeUtils.initializeExtraFieldSuffix(configCollector);
        InitializeUtils.initializeMapperSortColumn(configCollector);
        InitializeUtils.initializeDtoExcludeColumn(configCollector);
        InitializeUtils.initJdbcTypeConverter(configCollector);
    }

    @Override
    public LambdaGenerator<B> global(Function<GlobalBuilder, GlobalBuilder> func) {
        func.apply(new GlobalBuilder(this.configCollector.getGlobalConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> injection(Function<InjectionBuilder, InjectionBuilder> func) {
        func.apply(new InjectionBuilder(this.configCollector.getInjectionConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> strategy(Function<StrategyBuilder, StrategyBuilder> func) {
        func.apply(new StrategyBuilder(this.configCollector.getStrategyConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> controller(Function<ControllerBuilder, ControllerBuilder> func) {
        func.apply(new ControllerBuilder(this.configCollector.getControllerConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> service(Function<ServiceBuilder, ServiceBuilder> func) {
        func.apply(new ServiceBuilder(this.configCollector.getServiceConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> serviceImpl(Function<ServiceImplBuilder, ServiceImplBuilder> func) {
        func.apply(new ServiceImplBuilder(this.configCollector.getServiceImplConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> mapper(Function<MapperBuilder, MapperBuilder> func) {
        func.apply(new MapperBuilder(this.configCollector.getMapperConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> mapperXml(Function<MapperXmlBuilder, MapperXmlBuilder> func) {
        func.apply(new MapperXmlBuilder(this.configCollector.getMapperXmlConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> entity(Function<EntityBuilder, EntityBuilder> func) {
        func.apply(new EntityBuilder(this.configCollector.getEntityConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> queryDTO(Function<QueryDTOBuilder, QueryDTOBuilder> func) {
        func.apply(new QueryDTOBuilder(this.configCollector.getQueryDTOConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> queryVO(Function<QueryVOBuilder, QueryVOBuilder> func) {
        func.apply(new QueryVOBuilder(this.configCollector.getQueryVOConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> createDTO(Function<CreateDTOBuilder, CreateDTOBuilder> func) {
        func.apply(new CreateDTOBuilder(this.configCollector.getCreateDTOConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> updateDTO(Function<UpdateDTOBuilder, UpdateDTOBuilder> func) {
        func.apply(new UpdateDTOBuilder(this.configCollector.getUpdateDTOConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> exportDTO(Function<ExportDTOBuilder, ExportDTOBuilder> func) {
        func.apply(new ExportDTOBuilder(this.configCollector.getExportDTOConfig()));
        return this;
    }

    @Override
    public LambdaGenerator<B> importDTO(Function<ImportDTOBuilder, ImportDTOBuilder> func) {
        func.apply(new ImportDTOBuilder(this.configCollector.getImportDTOConfig()));
        return this;
    }

    @Override
    public void execute(String... tableNames) {
        if (tableNames.length > 0) {
            configCollector.getStrategyConfig().getInclude().addAll(Arrays.asList(tableNames));
        }
        log.debug("========================== ready to generate...==========================");
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine(this.configCollector);
        // 模板引擎初始化执行文件输出
        templateEngine.batchOutput().open();
        log.debug("========================== generate success！！！==========================");
        String banner =
                "  _________                                        \n" +
                        " /   _____/__ __   ____  ____  ____   ______ ______\n" +
                        " \\_____  \\|  |  \\_/ ___\\/ ___\\/ __ \\ /  ___//  ___/\n" +
                        " /        \\  |  /\\  \\__\\  \\__\\  ___/ \\___ \\ \\___ \\ \n" +
                        "/_______  /____/  \\___  >___  >___  >____  >____  >\n" +
                        "        \\/            \\/    \\/    \\/     \\/     \\/ " +
                        "\n(ﾉ>ω<)ﾉ  Code generation complete! Let's start coding ~\n";
        System.out.println(banner);
        System.out.println("generated file output dir:");
        String path = configCollector.getGlobalConfig().getOutputDir();
        System.out.println(new File(path).getAbsolutePath());
    }
}
