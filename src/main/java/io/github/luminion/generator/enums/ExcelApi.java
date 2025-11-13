package io.github.luminion.generator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum ExcelApi {
    
    
    EASY_EXCEL("javax"),


    FAST_EXCEL("jakarta"),

    ;
    @Getter
    public final String packagePrefix;



}
