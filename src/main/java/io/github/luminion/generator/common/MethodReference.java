package io.github.luminion.generator.common;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化获取信息的函数
 *
 * @author luminion
 * @since 1.0.0
 */
public interface MethodReference<T, R> extends Function<T, R>, Serializable {
}
