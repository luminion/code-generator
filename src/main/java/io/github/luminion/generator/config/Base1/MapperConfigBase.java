package io.github.luminion.generator.config.Base1;

import io.github.luminion.generator.config.ConfigSwitcher;
import io.github.luminion.generator.config.Switchable;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class MapperConfigBase<C extends Switchable<C>> extends AbstractSwitchableConfig<C> {

    public MapperConfigBase(ConfigSwitcher<C> configSwitcher) {
        super(configSwitcher);
    }
    
    
    
}
