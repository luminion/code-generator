package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.EntityQueryDTOConfig;

/**
 * @author luminion
 * @since 1.0.0
 */
public class EntityQueryDTOBuilder extends AbstractModelBuilder<EntityQueryDTOConfig,EntityQueryDTOBuilder> {

    public EntityQueryDTOBuilder(EntityQueryDTOConfig render) {
        super(render);
    }

    /**
     * 是否继承实体类
     *
     * @param enable 是否启用
     * @return this
     */
    public EntityQueryDTOBuilder extendsEntity(boolean enable) {
        this.config.setExtendsEntity(enable);
        return this;
    }

}
