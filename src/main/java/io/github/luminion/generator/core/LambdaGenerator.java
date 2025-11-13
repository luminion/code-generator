package io.github.luminion.generator.core;

import io.github.luminion.generator.config.builder.*;

import java.util.function.Consumer;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface LambdaGenerator<C> {

    LambdaGenerator<C> controller(Consumer<ControllerBuilder> consumer);

    LambdaGenerator<C> service(Consumer<ServiceBuilder> consumer);

    LambdaGenerator<C> mapper(Consumer<MapperBuilder> consumer);

    LambdaGenerator<C> model(Consumer<ModelBuilder> consumer);

    LambdaGenerator<C> strategy(Consumer<StrategyBuilder> consumer);

    LambdaGenerator<C> global(Consumer<GlobalBuilder> consumer);

    LambdaGenerator<C> output(Consumer<OutputBuilder> consumer);

    LambdaGenerator<C> special(Consumer<C> consumer);
    
    void execute(String... tables);

}
