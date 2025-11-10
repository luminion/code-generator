package io.github.luminion.generator.config2.base;

import io.github.luminion.generator.config2.DtoConfigurer;
import io.github.luminion.generator.config2.Switcher;
import io.github.luminion.generator.config2.swither.BasicSwitcher;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class DtoConfigBase<C extends Switcher<C>> extends AbstractConfigurer<C> implements DtoConfigurer<C> {

    public DtoConfigBase(BasicSwitcher<C> basicSwitcher) {
        super(basicSwitcher);
        basicSwitcher.setDtoConfigurer(this);
    }
}
