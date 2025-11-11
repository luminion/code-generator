package io.github.luminion.generator.util;

/**
 * @author luminion
 * @since 1.0.0
 */
public class StringUtils {


    public static boolean isBlank(CharSequence cs) {
        if (cs != null) {
            int length = cs.length();
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
