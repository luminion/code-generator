package io.github.luminion.generator;

import io.github.luminion.generator.builder.*;
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
        }
        log.debug("========================== ready to generate...==========================");
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine(configurer);
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
        String path = configurer.getTemplateConfig().getOutputDir();
        System.out.println(new File(path).getAbsolutePath());
    }

}
