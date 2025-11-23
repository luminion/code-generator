package io.github.luminion.generator.core;

import io.github.luminion.generator.config.builder.core.GlobalBuilder;
import io.github.luminion.generator.config.builder.core.StrategyBuilder;
import io.github.luminion.generator.config.builder.model.*;

import java.util.function.Consumer;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface LambdaGenerator<B> {
    
    LambdaGenerator<B> global(Consumer<GlobalBuilder> consumer);

    LambdaGenerator<B> strategy(Consumer<StrategyBuilder> consumer);

    LambdaGenerator<B> controller(Consumer<ControllerBuilder> consumer);

    LambdaGenerator<B> service(Consumer<ServiceBuilder> consumer);

    LambdaGenerator<B> serviceImpl(Consumer<ServiceImplBuilder> consumer);

    LambdaGenerator<B> mapper(Consumer<MapperBuilder> consumer);

    LambdaGenerator<B> mapperXml(Consumer<MapperXmlBuilder> consumer);

    LambdaGenerator<B> entity(Consumer<EntityBuilder> consumer);

    LambdaGenerator<B> queryDTO(Consumer<EntityQueryDTOBuilder> consumer);

    LambdaGenerator<B> queryVO(Consumer<EntityQueryVOBuilder> consumer);

    LambdaGenerator<B> insertDTO(Consumer<EntityInsertDTOBuilder> consumer);

    LambdaGenerator<B> updateDTO(Consumer<EntityUpdateDTOBuilder> consumer);

    LambdaGenerator<B> excelExportDTO(Consumer<EntityExcelExportDTOBuilder> consumer);

    LambdaGenerator<B> excelImportDTO(Consumer<EntityExcelImportDTOBuilder> consumer);

    LambdaGenerator<B> custom(Consumer<B> consumer);

    void execute(String... tables);

}
