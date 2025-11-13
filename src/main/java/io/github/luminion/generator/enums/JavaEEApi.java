package io.github.luminion.generator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum JavaEEApi {

    /**
     * 使用javax.
     */
    JAVAX("javax"),

    /**
     * springboot3.0+默认
     */
    JAKARTA("jakarta"),
    
    ;
    @Getter
    public final String packagePrefix; 

    
}
