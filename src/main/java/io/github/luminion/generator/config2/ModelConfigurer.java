package io.github.luminion.generator.config2;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface ModelConfigurer<C extends Switcher<C>> extends Switcher<C> {

    /**
     * 逻辑删除字段
     *
     * @param logicDeleteColumnName 逻辑删除列名称
     * @return {@link ModelConfigurer }
     * @since 1.0.0
     */
    ModelConfigurer<C> entityLogicDeleteColumnName(String logicDeleteColumnName);

    /**
     * 禁用下划线转驼峰
     *
     * @return {@link ModelConfigurer }
     * @since 1.0.0
     */
    ModelConfigurer<C> disableUnderlineToCamel();

    /**
     * 禁用生成serialVersionUID
     *
     * @return {@link ModelConfigurer }
     * @since 1.0.0
     */
    ModelConfigurer<C> disableSerialVersionUID();
    
    
    
    
}
