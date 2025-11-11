package io.github.luminion.generator.config.Base1;

import io.github.luminion.generator.config.*;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class AbstractSwitchableConfig<C extends Switchable<C>> implements Switchable<C> {
    protected final ConfigSwitcher<C> configSwitcher;
    
    public AbstractSwitchableConfig(ConfigSwitcher<C> configSwitcher) {
        super();
        this.configSwitcher = configSwitcher;
    }

    @Override
    public GlobalConfig<C> switchGlobal() {
        return configSwitcher.getGlobalConfig();
    }

    @Override
    public DatasourceConfig<C> switchDatasource() {
        return configSwitcher.getDatasourceConfig();
    }

    @Override
    public ControllerConfig<C> switchController() {
        return configSwitcher.getControllerConfig();
    }

    @Override
    public ServiceConfig<C> switchService() {
        return configSwitcher.getServiceConfig();
    }

    @Override
    public MapperConfig<C> switchMapper() {
        return configSwitcher.getMapperConfig();
    }

    @Override
    public ModelConfig<C> switchModel() {
        return configSwitcher.getModelConfig();
    }

    @Override
    public C switchCustom() {
        return configSwitcher.getCustomConfig();
    }
}
