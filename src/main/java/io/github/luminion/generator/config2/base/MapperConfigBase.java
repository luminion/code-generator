package io.github.luminion.generator.config2.base;

import io.github.luminion.generator.config2.MapperConfigurer;
import io.github.luminion.generator.config2.Switcher;
import io.github.luminion.generator.config2.swither.BasicSwitcher;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class MapperConfigBase<C extends Switcher<C>> extends AbstractConfigurer<C> implements MapperConfigurer<C> {

    public MapperConfigBase(BasicSwitcher<C> basicSwitcher) {
        super(basicSwitcher);
        basicSwitcher.setMapperConfigurer(this);
    }
}
