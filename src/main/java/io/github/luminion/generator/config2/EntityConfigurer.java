package io.github.luminion.generator.config2;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface EntityConfigurer<C extends Switcher<C>> extends Switcher<C> {

    /**
     * 逻辑删除字段
     *
     * @param logicDeleteColumnName 逻辑删除列名称
     * @return {@link EntityConfigurer }
     * @since 1.0.0
     */
    EntityConfigurer<C> logicDeleteColumnName(String logicDeleteColumnName);


    /**
     * 禁用下划线转驼峰
     *
     * @return {@link EntityConfigurer }
     * @since 1.0.0
     */
    EntityConfigurer<C>  disableUnderlineToCamel();
    
    
    
    
}
