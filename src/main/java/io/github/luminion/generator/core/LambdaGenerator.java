package io.github.luminion.generator.core;


import io.github.luminion.generator.config.Switchable;

/**
 * @author luminion
 */
public interface LambdaGenerator<C extends Switchable<C>> extends Switchable<C> {
    
    
    
    void execute(String[] tables);

}
