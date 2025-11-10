package io.github.luminion.generator.config2.base;

import io.github.luminion.generator.config2.*;
import io.github.luminion.generator.config2.swither.BasicSwitcher;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class AbstractConfigurer<C extends Switcher<C>> implements Switcher<C> {
    private final BasicSwitcher<C> basicSwitcher;

    public AbstractConfigurer(BasicSwitcher<C> basicSwitcher) {
        this.basicSwitcher = basicSwitcher;
    }

    @Override
    public ControllerConfigurer<C> switchController() {
        return basicSwitcher.getControllerConfigurer();
    }

    @Override
    public ServiceConfigurer<C> switchService() {
        return basicSwitcher.getServiceConfigurer();
    }

    @Override
    public MapperConfigurer<C> switchMapper() {
        return basicSwitcher.getMapperConfigurer();
    }

    @Override
    public ModelConfigurer<C> switchEntity() {
        return basicSwitcher.getModelConfigurer();
    }

    @Override
    public DtoConfigurer<C> switchDto() {
        return basicSwitcher.getDtoConfigurer();
    }

    @Override
    public GlobalConfigurer<C> switchGlobal() {
        return basicSwitcher.getGlobalConfigurer();
    }

    @Override
    public C switchSpecial() {
        return basicSwitcher.getSpecialConfigurer();
    }
    
}
