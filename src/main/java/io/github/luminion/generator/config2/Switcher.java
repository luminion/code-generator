package io.github.luminion.generator.config2;


/**
 * 扩展
 * @author luminion
 * @since 1.0.0
 */
public interface Switcher<C extends Switcher<C>> {
    
    ControllerConfigurer<C> switchController();
    
    ServiceConfigurer<C> switchService();
    
    MapperConfigurer<C> switchMapper();

    EntityConfigurer<C> switchEntity();

    DtoConfigurer<C> switchDto();
    
    GlobalConfigurer<C> switchGlobal();
    
    C switchSpecial();
}
