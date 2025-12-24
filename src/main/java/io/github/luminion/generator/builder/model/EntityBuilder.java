package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.EntityConfig;
import lombok.NonNull;

/**
 * @author luminion
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class EntityBuilder extends AbstractModelBuilder<EntityConfig, EntityBuilder> {

    public EntityBuilder(EntityConfig render) {
        super(render);
    }

    /**
     * 自定义继承的Entity类全称，带包名
     *
     * @param superClass 父类
     * @return this
     */
    public EntityBuilder superClass(@NonNull String superClass) {
        this.config.setSuperClass(superClass);
        return this;
    }
}
