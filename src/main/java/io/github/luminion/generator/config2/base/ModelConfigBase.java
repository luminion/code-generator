package io.github.luminion.generator.config2.base;

import io.github.luminion.generator.config2.ModelConfigurer;
import io.github.luminion.generator.config2.Switcher;
import io.github.luminion.generator.config2.swither.BasicSwitcher;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class ModelConfigBase<C extends Switcher<C>> extends AbstractConfigurer<C> implements ModelConfigurer<C> {

    public ModelConfigBase(BasicSwitcher<C> basicSwitcher) {
        super(basicSwitcher);
        basicSwitcher.setModelConfigurer(this);
    }
}
