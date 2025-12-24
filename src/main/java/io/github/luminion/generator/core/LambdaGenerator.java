package io.github.luminion.generator.core;

import io.github.luminion.generator.builder.core.GlobalBuilder;
import io.github.luminion.generator.builder.core.StrategyBuilder;
import io.github.luminion.generator.builder.model.*;

import java.util.function.Function;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface LambdaGenerator<B> {

    LambdaGenerator<B> global(Function<GlobalBuilder, GlobalBuilder> func);

    LambdaGenerator<B> strategy(Function<StrategyBuilder, StrategyBuilder> func);

    LambdaGenerator<B> controller(Function<ControllerBuilder, ControllerBuilder> func);

    LambdaGenerator<B> service(Function<ServiceBuilder, ServiceBuilder> func);

    LambdaGenerator<B> serviceImpl(Function<ServiceImplBuilder, ServiceImplBuilder> func);

    LambdaGenerator<B> mapper(Function<MapperBuilder, MapperBuilder> func);

    LambdaGenerator<B> mapperXml(Function<MapperXmlBuilder, MapperXmlBuilder> func);

    LambdaGenerator<B> entity(Function<EntityBuilder, EntityBuilder> func);

    LambdaGenerator<B> queryDTO(Function<QueryDTOBuilder, QueryDTOBuilder> func);

    LambdaGenerator<B> queryVO(Function<QueryVOBuilder, QueryVOBuilder> func);

    LambdaGenerator<B> createDTO(Function<CreateDTOBuilder, CreateDTOBuilder> func);

    LambdaGenerator<B> updateDTO(Function<UpdateDTOBuilder, UpdateDTOBuilder> func);

    LambdaGenerator<B> exportDTO(Function<ExportDTOBuilder, ExportDTOBuilder> func);

    LambdaGenerator<B> importDTO(Function<ImportDTOBuilder, ImportDTOBuilder> func);

    LambdaGenerator<B> custom(Function<B, B> func);

    void execute(String... tables);

}
