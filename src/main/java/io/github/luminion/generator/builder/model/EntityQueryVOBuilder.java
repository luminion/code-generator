package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.EntityQueryVOConfig;

/**
 * @author luminion
 * @since 1.0.0
 */
public class EntityQueryVOBuilder extends AbstractModelBuilder<EntityQueryVOConfig, EntityQueryVOBuilder> {

    public EntityQueryVOBuilder(EntityQueryVOConfig render) {
        super(render);
    }

    /**
     * 是否继承实体类
     *
     * @param enable 是否启用
     * @return this
     */
    public EntityQueryVOBuilder extendsEntity(boolean enable) {
        this.config.setExtendsEntity(enable);
        return this;
    }
}
