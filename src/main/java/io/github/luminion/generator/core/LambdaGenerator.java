package io.github.luminion.generator.core;


import io.github.luminion.generator.config.Switcher;

/**
 * @author luminion
 */
public interface LambdaGenerator<C extends Switcher<C>> extends Switcher<C> {
    
    
    
    void execute(String[] tables);

}
