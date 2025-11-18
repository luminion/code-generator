package io.github.luminion.generator.core.suppport;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.builder.special.MybatisPlusSqlBoosterBuilder;
import io.github.luminion.generator.core.AbstractGenerator;
import io.github.luminion.generator.core.LambdaGenerator;

import java.util.function.Consumer;

/**
 * @author luminion
 * @since 1.0.0
 */
public class SqlBoosterGenerator extends AbstractGenerator<MybatisPlusSqlBoosterBuilder> {

    public SqlBoosterGenerator(Configurer configurer) {
        super(configurer);
    }

    @Override
    public LambdaGenerator<MybatisPlusSqlBoosterBuilder> special(Consumer<MybatisPlusSqlBoosterBuilder> consumer) {
        MybatisPlusSqlBoosterBuilder mybatisPlusSqlBoosterBuilder = new MybatisPlusSqlBoosterBuilder(this.configurer);
        consumer.accept(mybatisPlusSqlBoosterBuilder);
        return this;
    }
}
