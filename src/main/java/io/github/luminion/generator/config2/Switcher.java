package io.github.luminion.generator.config2;

import io.github.luminion.generator.config.support.DataSourceConfig;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface Switcher {
    
    ControllerConfig switchController();
    
    ServiceConfig switchService();
    
    MapperConfig switchMapper();

    EntityConfig switchEntity();

    DtoConfig switchDto();
    
    GlobalConfig switchGlobal();
    
}
