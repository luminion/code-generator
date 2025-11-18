package io.github.luminion.generator.config.model.builder.special;

import io.github.luminion.generator.config.Configurer;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public abstract class AbstractSpecialBuilder<C extends AbstractSpecialBuilder<C>> {
    protected final Configurer configurer;
    @SuppressWarnings("unchecked")
    protected C returnThis() {
        return (C) this;
    }
    /**
     * 是否生成实体时，生成字段注解
     *
     * @param enable 是否启用
     * @return this
     */
    protected C entityTableFieldAnnotation(boolean enable) {
        this.configurer.getEntityConfig().setTableFieldAnnotation(enable);
        return returnThis();
    }

    /**
     * 开启 ActiveRecord 模式
     *
     * @param enable 是否启用
     * @return this
     */
    protected C entityActiveRecord(boolean enable) {
        this.configurer.getEntityConfig().setActiveRecord(enable);
        return returnThis();
    }
    
    
    
}
