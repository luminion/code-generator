package io.github.luminion.generator.config;

import lombok.Data;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ConfigSwitcher<C extends Switchable<C>>  {
    protected GlobalConfig<C> globalConfig;
    protected DatasourceConfig<C> datasourceConfig;
    protected ControllerConfig<C> controllerConfig;
    protected ServiceConfig<C> serviceConfig;
    protected MapperConfig<C> mapperConfig;
    protected ModelConfig<C> modelConfig;
    protected C customConfig;
}
