package io.github.luminion.generator.config2.swither;

import io.github.luminion.generator.config2.*;
import lombok.Data;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class BasicSwitcher<C extends Switcher<C>> implements Switcher<C> {
    protected ControllerConfigurer<C> controllerConfigurer;
    protected ServiceConfigurer<C> serviceConfigurer;
    protected MapperConfigurer<C> mapperConfigurer;
    protected ModelConfigurer<C> modelConfigurer;
    protected DtoConfigurer<C> dtoConfigurer;
    protected GlobalConfigurer<C> globalConfigurer;
    protected C specialConfigurer;

    @Override
    public ControllerConfigurer<C> switchController() {
        return this.getControllerConfigurer();
    }

    @Override
    public ServiceConfigurer<C> switchService() {
        return this.getServiceConfigurer();
    }

    @Override
    public MapperConfigurer<C> switchMapper() {
        return this.getMapperConfigurer();
    }

    @Override
    public ModelConfigurer<C> switchEntity() {
        return this.getModelConfigurer();
    }

    @Override
    public DtoConfigurer<C> switchDto() {
        return this.getDtoConfigurer();
    }

    @Override
    public GlobalConfigurer<C> switchGlobal() {
        return this.getGlobalConfigurer();
    }

    @Override
    public C switchSpecial() {
        return this.getSpecialConfigurer();
    }

}
