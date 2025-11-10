package io.github.luminion.generator.config2;

/**
 * @author luminion
 * @since 1.0.0
 */
public class BaseSwitcher<C extends Switcher<C>> implements Switcher<C>{
    protected ControllerConfigurer<C> controllerConfigurer;
    protected ServiceConfigurer<C> serviceConfigurer;
    protected MapperConfigurer<C> mapperConfigurer;
    protected EntityConfigurer<C> entityConfigurer;
    protected DtoConfigurer<C> dtoConfigurer;
    protected GlobalConfigurer<C> globalConfigurer;
    protected C special;


    @Override
    public ControllerConfigurer<C> switchController() {
        return controllerConfigurer;
    }

    @Override
    public ServiceConfigurer<C> switchService() {
        return serviceConfigurer;
    }

    @Override
    public MapperConfigurer<C> switchMapper() {
        return mapperConfigurer;
    }

    @Override
    public EntityConfigurer<C> switchEntity() {
        return entityConfigurer;
    }

    @Override
    public DtoConfigurer<C> switchDto() {
        return dtoConfigurer;
    }

    @Override
    public GlobalConfigurer<C> switchGlobal() {
        return globalConfigurer;
    }

    @Override
    public C switchSpecial() {
        return special;
    }
}
