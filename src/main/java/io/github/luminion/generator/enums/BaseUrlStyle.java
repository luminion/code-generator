package io.github.luminion.generator.enums;

import io.github.luminion.generator.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * Controller baseUrl 中实体路径部分的命名风格
 *
 * @author luminion
 * @since 1.0.0
 */
@AllArgsConstructor
public enum BaseUrlStyle {
    /**
     * 驼峰：sysUser
     */
    CAMEL_CASE(value -> value),
    /**
     * 中划线：sys-user
     */
    HYPHEN_CASE(StringUtils::camelToHyphen),
    /**
     * 下划线：sys_user
     */
    UNDERLINE_CASE(StringUtils::camelToUnderline),
    /**
     * 斜杠分割：sys/user
     */
    SLASH_CASE(value -> StringUtils.camelToHyphen(value).replace('-', '/')),
    ;

    @Getter
    private final Function<String, String> function;

    public String convert(String value) {
        return function.apply(value);
    }
}
