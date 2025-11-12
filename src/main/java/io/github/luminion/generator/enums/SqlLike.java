package io.github.luminion.generator.enums;

/**
 * @author luminion
 * @since 1.0.0
 */
public enum SqlLike {
    /**
     * %值
     */
    LEFT,
    /**
     * 值%
     */
    RIGHT,
    /**
     * %值%
     */
    DEFAULT
    ;

    public static String concatLike(Object str, SqlLike type) {
        switch (type) {
            case LEFT:
                return "%" + str;
            case RIGHT:
                return str + "%";
            default:
                return "%" + str + "%";
        }
    }
}