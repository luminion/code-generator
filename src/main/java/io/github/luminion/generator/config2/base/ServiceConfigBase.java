package io.github.luminion.generator.config2.base;

import io.github.luminion.generator.config2.ServiceConfigurer;
import io.github.luminion.generator.config2.Switcher;
import io.github.luminion.generator.config2.swither.BasicSwitcher;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class ServiceConfigBase<C extends Switcher<C>> extends AbstractConfigurer<C> implements ServiceConfigurer<C> {

    public ServiceConfigBase(BasicSwitcher<C> basicSwitcher) {
        super(basicSwitcher);
        basicSwitcher.setServiceConfigurer(this);
    }
}
