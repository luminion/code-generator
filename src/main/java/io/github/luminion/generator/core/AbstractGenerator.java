package io.github.luminion.generator.core;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.builder.core.GlobalBuilder;
import io.github.luminion.generator.config.builder.core.StrategyBuilder;
import io.github.luminion.generator.config.builder.model.*;
import io.github.luminion.generator.engine.VelocityTemplateEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author luminion
 */
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractGenerator<C extends TemplateRender,B> implements LambdaGenerator<B> {
    protected final Configurer<C> configurer;

    @Override
    public LambdaGenerator<B> global(Consumer<GlobalBuilder> consumer) {
        consumer.accept(new GlobalBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> strategy(Consumer<StrategyBuilder> consumer) {
        consumer.accept(new StrategyBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> controller(Consumer<ControllerBuilder> consumer) {
        consumer.accept(new ControllerBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> service(Consumer<ServiceBuilder> consumer) {
        consumer.accept(new ServiceBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> serviceImpl(Consumer<ServiceImplBuilder> consumer) {
        consumer.accept(new ServiceImplBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> mapper(Consumer<MapperBuilder> consumer) {
        consumer.accept(new MapperBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> mapperXml(Consumer<MapperXmlBuilder> consumer) {
        consumer.accept(new MapperXmlBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> entity(Consumer<EntityBuilder> consumer) {
        consumer.accept(new EntityBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> queryDTO(Consumer<EntityQueryDTOBuilder> consumer) {
        consumer.accept(new EntityQueryDTOBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> queryVO(Consumer<EntityQueryVOBuilder> consumer) {
        consumer.accept(new EntityQueryVOBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> createDTO(Consumer<EntityCreateDTOBuilder> consumer) {
        consumer.accept(new EntityCreateDTOBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> updateDTO(Consumer<EntityUpdateDTOBuilder> consumer) {
        consumer.accept(new EntityUpdateDTOBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> excelExportDTO(Consumer<EntityExcelExportDTOBuilder> consumer) {
        consumer.accept(new EntityExcelExportDTOBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<B> excelImportDTO(Consumer<EntityExcelImportDTOBuilder> consumer) {
        consumer.accept(new EntityExcelImportDTOBuilder(this.configurer));
        return this;
    }

    @Override
    public void execute(String... tableNames) {
        if (tableNames.length > 0) {
            configurer.getStrategyConfig().getInclude().addAll(Arrays.asList(tableNames));
        }
        log.debug("========================== ready to generate...==========================");
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine(this.configurer);
        // 模板引擎初始化执行文件输出
        templateEngine.batchOutput().open();
        log.debug("========================== generate success！！！==========================");
        String banner =
                "  _________                                        \n" +
                        " /   _____/__ __   ____  ____  ____   ______ ______\n" +
                        " \\_____  \\|  |  \\_/ ___\\/ ___\\/ __ \\ /  ___//  ___/\n" +
                        " /        \\  |  /\\  \\__\\  \\__\\  ___/ \\___ \\ \\___ \\ \n" +
                        "/_______  /____/  \\___  >___  >___  >____  >____  >\n" +
                        "        \\/            \\/    \\/    \\/     \\/     \\/ "+
                        "\n(ﾉ>ω<)ﾉ  Code generation complete! Let's coding ~\n";
        System.out.println(banner);
        System.out.println("check files in following folder:");
        String path = configurer.getGlobalConfig().getOutputDir();
        System.out.println(new File(path).getAbsolutePath());
    }
}
