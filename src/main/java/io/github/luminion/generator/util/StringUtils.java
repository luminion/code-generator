package io.github.luminion.generator.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

import java.util.regex.Pattern;

/**
 * @author luminion
 * @since 1.0.0
 */
public class StringUtils {

    /**
     * 是否为大写命名
     */
    private static final Pattern CAPITAL_MODE = Pattern.compile("^[0-9A-Z/_]+$");

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
    

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
    
    /**
     * 删除字符前缀之后,首字母小写,之后字符大小写的不变
     * <p>StringUtils.removePrefixAfterPrefixToLower( "isUser", 2 )     = user</p>
     * <p>StringUtils.removePrefixAfterPrefixToLower( "isUserInfo", 2 ) = userInfo</p>
     *
     * @param rawString 需要处理的字符串
     * @param index     删除多少个字符(从左至右)
     * @return ignore
     */
    public static String removePrefixAfterPrefixToLower(String rawString, int index) {
        return prefixToLower(rawString.substring(index), 1);
    }

    /**
     * 前n个首字母小写,之后字符大小写的不变
     *
     * @param rawString 需要处理的字符串
     * @param index     多少个字符(从左至右)
     * @return ignore
     */
    public static String prefixToLower(String rawString, int index) {
        StringBuilder field = new StringBuilder();
        field.append(rawString.substring(0, index).toLowerCase());
        field.append(rawString.substring(index));
        return field.toString();
    }

    
    /**
     * 是否为大写命名
     *
     * @param word 待判断字符串
     * @return ignore
     */
    public static boolean isCapitalMode(String word) {
        return null != word && CAPITAL_MODE.matcher(word).matches();
    }

    /**
     * 是否为驼峰下划线混合命名
     *
     * @param word 待判断字符串
     * @return ignore
     */
    public static boolean isMixedMode(String word) {
        return matches(".*[A-Z]+.*", word) && matches(".*[/_]+.*", word);
    }

    /**
     * 正则表达式匹配
     *
     * @param regex 正则表达式字符串
     * @param input 要匹配的字符串
     * @return 如果 input 符合 regex 正则表达式格式, 返回true, 否则返回 false;
     */
    public static boolean matches(String regex, String input) {
        if (null == regex || null == input) {
            return false;
        }
        return Pattern.matches(regex, input);
    }

    /**
     * 首字母转换小写
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String firstToLowerCase(String param) {
        if (isBlank(param)) {
            return "";
        }
        return param.substring(0, 1).toLowerCase() + param.substring(1);
    }
    
    
}
