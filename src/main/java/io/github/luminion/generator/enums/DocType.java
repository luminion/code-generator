package io.github.luminion.generator.enums;

/**
 * @author luminion
 * @since 1.0.0
 */
public enum DocType {
    /**
     * java doc
     */
    JAVA_DOC,
    /**
     * markdown格式的javadoc
     */
    @Deprecated
    JAVA_MARKDOWN,

    /**
     * swagger v2
     */
    SWAGGER,
    /**
     * spring doc (swagger v3)
     */
    SPRING_DOC,


    
    ;

}
