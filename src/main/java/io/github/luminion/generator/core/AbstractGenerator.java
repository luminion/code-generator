package io.github.luminion.generator.core;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.builder.core.GlobalBuilder;
import io.github.luminion.generator.config.builder.core.InjectionBuilder;
import io.github.luminion.generator.config.builder.core.StrategyBuilder;
import io.github.luminion.generator.config.builder.model.*;
import io.github.luminion.generator.config.builder.special.AbstractSpecialBuilder;
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
public abstract class AbstractGenerator<C extends AbstractSpecialBuilder<C>> implements LambdaGenerator<C> {
    protected final Configurer configurer;

    @Override
    public LambdaGenerator<C> global(Consumer<GlobalBuilder> consumer) {
        consumer.accept(new GlobalBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> strategy(Consumer<StrategyBuilder> consumer) {
        consumer.accept(new StrategyBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> injection(Consumer<InjectionBuilder> consumer) {
        consumer.accept(new InjectionBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> controller(Consumer<ControllerBuilder> consumer) {
        consumer.accept(new ControllerBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> service(Consumer<ServiceBuilder> consumer) {
        consumer.accept(new ServiceBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> serviceImpl(Consumer<ServiceImplBuilder> consumer) {
        consumer.accept(new ServiceImplBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> mapper(Consumer<MapperBuilder> consumer) {
        consumer.accept(new MapperBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> mapperXml(Consumer<MapperXmlBuilder> consumer) {
        consumer.accept(new MapperXmlBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> entity(Consumer<EntityBuilder> consumer) {
        consumer.accept(new EntityBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> queryDTO(Consumer<EntityQueryDTOBuilder> consumer) {
        consumer.accept(new EntityQueryDTOBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> queryVO(Consumer<EntityQueryVOBuilder> consumer) {
        consumer.accept(new EntityQueryVOBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> insertDTO(Consumer<EntityInsertDTOBuilder> consumer) {
        consumer.accept(new EntityInsertDTOBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> updateDTO(Consumer<EntityUpdateDTOBuilder> consumer) {
        consumer.accept(new EntityUpdateDTOBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> excelExportDTO(Consumer<EntityExcelExportDTOBuilder> consumer) {
        consumer.accept(new EntityExcelExportDTOBuilder(this.configurer));
        return this;
    }

    @Override
    public LambdaGenerator<C> excelImportDTO(Consumer<EntityExcelImportDTOBuilder> consumer) {
        consumer.accept(new EntityExcelImportDTOBuilder(this.configurer));
        return this;
    }

    @Override
    public void execute(String... tableNames) {
        if (tableNames.length > 0) {
            configurer.getStrategyConfig().getInclude().addAll(Arrays.asList(tableNames));
        }
        log.debug("==========================准备生成文件...==========================");
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine(this.configurer);
        // 模板引擎初始化执行文件输出
        templateEngine.batchOutput().open();
        log.debug("==========================文件生成完成！！！==========================");
        String banner =
                "  _  _                                      ___             _  _      _                               ___             _  _  \n" +
                        " | || |   __ _    __ __   __ _      o O O  /   \\     o O O | \\| |    (_)     __      ___      o O O  |   \\   __ _    | || | \n" +
                        " | __ |  / _` |   \\ V /  / _` |    o       | - |    o      | .` |    | |    / _|    / -_)    o       | |) | / _` |    \\_, | \n" +
                        " |_||_|  \\__,_|   _\\_/_  \\__,_|   TS__[O]  |_|_|   TS__[O] |_|\\_|   _|_|_   \\__|_   \\___|   TS__[O]  |___/  \\__,_|   _|__/  \n" +
                        "_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| {======|_|\"\"\"\"\"| {======|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| {======|_|\"\"\"\"\"|_|\"\"\"\"\"|_| \"\"\"\"| \n" +
                        "\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'./o--000'\"`-0-0-'./o--000'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'./o--000'\"`-0-0-'\"`-0-0-'\"`-0-0-' ";
        System.out.println(banner);
        System.out.println("execute success! check files in following folder:");
        String path = configurer.getGlobalConfig().getOutputDir();
        System.out.println(new File(path).getAbsolutePath());
    }
}
