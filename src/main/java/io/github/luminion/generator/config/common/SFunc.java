package io.github.luminion.generator.config.common;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface SFunc<T, R> extends Function<T, R>, Serializable {
}
