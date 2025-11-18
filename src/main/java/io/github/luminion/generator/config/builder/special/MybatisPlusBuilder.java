package io.github.luminion.generator.config.builder.special;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.RuntimeEnv;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MybatisPlusBuilder extends AbstractSpecialBuilder<MybatisPlusBuilder> {


    public MybatisPlusBuilder(Configurer configurer) {
        super(configurer);
        configurer.getGlobalConfig().setRuntimeEnv(RuntimeEnv.MYBATIS_PLUS);
    }

    @Override
    public MybatisPlusBuilder entityActiveRecord(boolean enable) {
        return super.entityActiveRecord(enable);
    }

    @Override
    public MybatisPlusBuilder entityTableFieldAnnotation(boolean enable) {
        return super.entityTableFieldAnnotation(enable);
    }
}
