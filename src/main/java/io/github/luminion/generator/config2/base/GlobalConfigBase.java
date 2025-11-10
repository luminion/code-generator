package io.github.luminion.generator.config2.base;

import io.github.luminion.generator.config2.GlobalConfigurer;
import io.github.luminion.generator.config2.Switcher;
import io.github.luminion.generator.config2.swither.BasicSwitcher;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class GlobalConfigBase<C extends Switcher<C>> extends AbstractConfigurer<C> implements GlobalConfigurer<C> {

    public GlobalConfigBase(BasicSwitcher<C> basicSwitcher) {
        super(basicSwitcher);
        basicSwitcher.setGlobalConfigurer(this);
    }
}
