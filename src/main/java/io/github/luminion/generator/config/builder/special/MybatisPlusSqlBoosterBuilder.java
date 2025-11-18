package io.github.luminion.generator.config.builder.special;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.RuntimeEnv;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MybatisPlusSqlBoosterBuilder extends AbstractSpecialBuilder<MybatisPlusSqlBoosterBuilder>{
    public MybatisPlusSqlBoosterBuilder(Configurer configurer) {
        super(configurer);
        configurer.getGlobalConfig().setRuntimeEnv(RuntimeEnv.MYBATIS_PLUS);
    }

    @Override
    public MybatisPlusSqlBoosterBuilder entityActiveRecord(boolean enable) {
        return super.entityActiveRecord(enable);
    }

    @Override
    public MybatisPlusSqlBoosterBuilder entityTableFieldAnnotation(boolean enable) {
        return super.entityTableFieldAnnotation(enable);
    }
}
