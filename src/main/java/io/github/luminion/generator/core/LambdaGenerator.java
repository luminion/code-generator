package io.github.luminion.generator.core;

import io.github.luminion.generator.config.builder.core.GlobalBuilder;
import io.github.luminion.generator.config.builder.core.InjectionBuilder;
import io.github.luminion.generator.config.builder.core.StrategyBuilder;
import io.github.luminion.generator.config.builder.model.*;
import io.github.luminion.generator.config.builder.special.AbstractSpecialBuilder;

import java.util.function.Consumer;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface LambdaGenerator<C extends AbstractSpecialBuilder<C>> {

    LambdaGenerator<C> global(Consumer<GlobalBuilder> consumer);

    LambdaGenerator<C> strategy(Consumer<StrategyBuilder> consumer);

    LambdaGenerator<C> injection(Consumer<InjectionBuilder> consumer);

    LambdaGenerator<C> controller(Consumer<ControllerBuilder> consumer);

    LambdaGenerator<C> service(Consumer<ServiceBuilder> consumer);

    LambdaGenerator<C> serviceImpl(Consumer<ServiceImplBuilder> consumer);

    LambdaGenerator<C> mapper(Consumer<MapperBuilder> consumer);

    LambdaGenerator<C> mapperXml(Consumer<MapperXmlBuilder> consumer);

    LambdaGenerator<C> entity(Consumer<EntityBuilder> consumer);

    LambdaGenerator<C> queryDTO(Consumer<EntityQueryDTOBuilder> consumer);

    LambdaGenerator<C> queryVO(Consumer<EntityQueryVOBuilder> consumer);

    LambdaGenerator<C> insertDTO(Consumer<EntityInsertDTOBuilder> consumer);

    LambdaGenerator<C> updateDTO(Consumer<EntityUpdateDTOBuilder> consumer);

    LambdaGenerator<C> excelExportDTO(Consumer<EntityExcelExportDTOBuilder> consumer);

    LambdaGenerator<C> excelImportDTO(Consumer<EntityExcelImportDTOBuilder> consumer);

    LambdaGenerator<C> special(Consumer<C> consumer);

    void execute(String... tables);

}
