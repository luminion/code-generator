package io.github.luminion.generator.config.base;

import io.github.luminion.generator.config.ConfigSwitcher;
import io.github.luminion.generator.config.Switchable;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class ModelConfigBase<C extends Switchable<C>> extends AbstractSwitchableConfig<C> {

    public ModelConfigBase(ConfigSwitcher<C> configSwitcher) {
        super(configSwitcher);
    }
    
    
    
}
