package io.github.luminion.generator.core;

import io.github.luminion.generator.config.core.builder.GlobalBuilder;

import java.util.function.Consumer;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface LambdaGenerator<C> {

    LambdaGenerator<C> global(Consumer<GlobalBuilder> consumer);
    
//    LambdaGenerator<C> strategy(Consumer<StrategyBuilder> consumer);
//    
//    LambdaGenerator<C> inject(Consumer<InjectBuilder> consumer);
//    
//
//    LambdaGenerator<C> controller(Consumer<ControllerBuilder> consumer);
//
//    LambdaGenerator<C> service(Consumer<ServiceBuilder> consumer);
//
//    LambdaGenerator<C> mapper(Consumer<MapperBuilder> consumer);
//
//    LambdaGenerator<C> model(Consumer<ModelBuilder> consumer);
//
//    LambdaGenerator<C> special(Consumer<C> consumer);
//    
    void execute(String... tables);

}
