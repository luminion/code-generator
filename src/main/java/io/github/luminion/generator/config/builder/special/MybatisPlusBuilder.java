package io.github.luminion.generator.config.builder.special;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.IdType;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.fill.IFill;
import lombok.NonNull;

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

    @Override
    public MybatisPlusBuilder strategyIdType(@NonNull IdType idType) {
        return super.strategyIdType(idType);
    }

    @Override
    public MybatisPlusBuilder strategyLogicDeleteColumnName(@NonNull String logicDeleteColumnName) {
        return super.strategyLogicDeleteColumnName(logicDeleteColumnName);
    }

    @Override
    public MybatisPlusBuilder strategyTableFills(IFill... tableFills) {
        return super.strategyTableFills(tableFills);
    }

    @Override
    public MybatisPlusBuilder strategyVersionColumnName(@NonNull String versionColumnName) {
        return super.strategyVersionColumnName(versionColumnName);
    }
    
}
