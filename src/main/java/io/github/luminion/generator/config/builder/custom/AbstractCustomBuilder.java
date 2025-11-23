package io.github.luminion.generator.config.builder.custom;

import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public abstract class AbstractCustomBuilder<B extends AbstractCustomBuilder<B>> {
    @SuppressWarnings("unchecked")
    protected B returnThis() {
        return (B) this;
    }

}
