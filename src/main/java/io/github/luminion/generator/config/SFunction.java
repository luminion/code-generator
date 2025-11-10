package io.github.luminion.generator.config;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 
 * @author luminion
 * @since 1.0.0
 */
public interface SFunction<T,R> extends Serializable, Function<T,R> {
}
