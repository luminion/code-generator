package io.github.luminion.generator.config2.base;

import io.github.luminion.generator.config2.EntityConfigurer;
import io.github.luminion.generator.config2.Switcher;
import io.github.luminion.generator.config2.swither.BasicSwitcher;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class EntityConfigBase<C extends Switcher<C>> extends AbstractConfigurer<C> implements EntityConfigurer<C> {

    public EntityConfigBase(BasicSwitcher<C> basicSwitcher) {
        super(basicSwitcher);
        basicSwitcher.setEntityConfigurer(this);
    }
}
