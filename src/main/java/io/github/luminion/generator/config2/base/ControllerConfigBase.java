package io.github.luminion.generator.config2.base;

import io.github.luminion.generator.config2.ControllerConfigurer;
import io.github.luminion.generator.config2.Switcher;
import io.github.luminion.generator.config2.swither.BasicSwitcher;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class ControllerConfigBase<C extends Switcher<C>> extends AbstractConfigurer<C> implements ControllerConfigurer<C> {

    public ControllerConfigBase(BasicSwitcher<C> basicSwitcher) {
        super(basicSwitcher);
        basicSwitcher.setControllerConfigurer(this);
    }
    
    
}
