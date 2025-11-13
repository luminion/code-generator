package io.github.luminion.generator.common;

import java.util.function.Function;

/**
 * 名称转换器
 *
 * @author luminion
 * @since 1.0.0
 */
@FunctionalInterface
public interface NameConverter extends Function<String, String> {


    /**
     * 名称转化
     *
     * @param source 原名称
     * @return 转化后的名称
     * @since 1.0.0
     */
    @Override
    String apply(String source);
}
