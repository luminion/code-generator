package io.github.luminion.generator.config.model.builder.special;

import io.github.luminion.generator.config.Configurer;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MybatisPlusBuilder extends AbstractSpecialBuilder<MybatisPlusBuilder> {


    public MybatisPlusBuilder(Configurer configurer) {
        super(configurer);
    }

    @Override
    public MybatisPlusBuilder enactiveRecord(boolean enable) {
        return super.enactiveRecord(enable);
    }

    @Override
    public MybatisPlusBuilder tableFieldAnnotation(boolean enable) {
        return super.tableFieldAnnotation(enable);
    }
}
