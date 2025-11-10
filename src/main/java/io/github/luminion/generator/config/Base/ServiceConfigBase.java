package io.github.luminion.generator.config.Base;

import io.github.luminion.generator.config.ConfigSwitcher;
import io.github.luminion.generator.config.Switchable;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class ServiceConfigBase<C extends Switchable<C>> extends AbstractSwitchableConfig<C> {

    public ServiceConfigBase(ConfigSwitcher<C> configSwitcher) {
        super(configSwitcher);
    }
    
    
    
}
