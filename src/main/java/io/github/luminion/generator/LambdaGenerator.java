package io.github.luminion.generator;

import io.github.luminion.generator.builder.CommandBuilder;
import io.github.luminion.generator.builder.ControllerBuilder;
import io.github.luminion.generator.builder.DataSourceBuilder;
import io.github.luminion.generator.builder.EntityBuilder;
import io.github.luminion.generator.builder.ExcelBuilder;
import io.github.luminion.generator.builder.GlobalBuilder;
import io.github.luminion.generator.builder.MapperBuilder;
import io.github.luminion.generator.builder.QueryBuilder;
import io.github.luminion.generator.builder.ServiceBuilder;
import io.github.luminion.generator.builder.TemplateBuilder;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.engine.VelocityTemplateEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class LambdaGenerator {
    private final Configurer configurer;

    public LambdaGenerator global(Consumer<GlobalBuilder> global) {
        global.accept(new GlobalBuilder(configurer));
        return this;
    }

    public LambdaGenerator dataSource(Consumer<DataSourceBuilder> dataSource) {
        dataSource.accept(new DataSourceBuilder(configurer));
        return this;
    }

    public LambdaGenerator template(Consumer<TemplateBuilder> template) {
        template.accept(new TemplateBuilder(configurer));
        return this;
    }

    public LambdaGenerator controller(Consumer<ControllerBuilder> controller) {
        controller.accept(new ControllerBuilder(configurer));
        return this;
    }

    public LambdaGenerator service(Consumer<ServiceBuilder> service) {
        service.accept(new ServiceBuilder(configurer));
        return this;
    }

    public LambdaGenerator mapper(Consumer<MapperBuilder> mapper) {
        mapper.accept(new MapperBuilder(configurer));
        return this;
    }

    public LambdaGenerator entity(Consumer<EntityBuilder> entity) {
        entity.accept(new EntityBuilder(configurer));
        return this;
    }

    public LambdaGenerator command(Consumer<CommandBuilder> command) {
        command.accept(new CommandBuilder(configurer));
        return this;
    }

    public LambdaGenerator query(Consumer<QueryBuilder> query) {
        query.accept(new QueryBuilder(configurer));
        return this;
    }

    public LambdaGenerator excel(Consumer<ExcelBuilder> excel) {
        excel.accept(new ExcelBuilder(configurer));
        return this;
    }

    public void execute(String... tableNames) {
        if (tableNames.length > 0) {
            configurer.getDataSourceConfig().getIncludeTables().addAll(Arrays.asList(tableNames));
            log.info("Restrict generation to tables: {}", Arrays.toString(tableNames));
        } else {
            log.info("No explicit table filter provided. All matched tables will be generated.");
        }
        File outputDir = new File(configurer.getTemplateConfig().getOutputDir());
        log.info("Start code generation. Output directory: {}", outputDir.getAbsolutePath());
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine(configurer);
        templateEngine.batchOutput().open();
        log.info("Code generation completed. Output directory: {}", outputDir.getAbsolutePath());
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
        System.out.println(outputDir.getAbsolutePath());
    }
}