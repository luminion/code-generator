package io.github.luminion.generator.config;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface Switchable<C extends Switchable<C>> {

    GlobalConfig<C> switchGlobal();

    DatasourceConfig<C> switchDatasource();

    ControllerConfig<C> switchController();

    ServiceConfig<C> switchService();

    MapperConfig<C> switchMapper();

    ModelConfig<C> switchModel();
    
    C switchCustom();

}
