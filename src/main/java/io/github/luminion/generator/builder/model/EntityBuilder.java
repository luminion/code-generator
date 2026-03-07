package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.EntityConfig;
import lombok.NonNull;

/**
 * 实体类配置构建器
 * <p>
 * 用于配置实体类的生成选项：
 * <ul>
 *   <li>继承父类</li>
 * </ul>
 * 继承自AbstractModelBuilder，提供通用配置方法
 *
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
