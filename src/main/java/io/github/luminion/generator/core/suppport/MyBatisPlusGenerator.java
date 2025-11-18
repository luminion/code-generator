package io.github.luminion.generator.core.suppport;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.builder.special.MybatisPlusBuilder;
import io.github.luminion.generator.core.AbstractGenerator;
import io.github.luminion.generator.core.LambdaGenerator;

import java.util.function.Consumer;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MyBatisPlusGenerator extends AbstractGenerator<MybatisPlusBuilder> {
    public MyBatisPlusGenerator(Configurer configurer) {
        super(configurer);
    }

    @Override
    public LambdaGenerator<MybatisPlusBuilder> special(Consumer<MybatisPlusBuilder> consumer) {
        MybatisPlusBuilder mybatisPlusBuilder = new MybatisPlusBuilder(this.configurer);
        consumer.accept(mybatisPlusBuilder);
        return this;
    }
}
