/*
 * Copyright (c) 2011-2025, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.luminion.generator.enums;

import io.github.luminion.generator.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

/**
 * 名称转化策略
 *
 * @author YangHu, tangguo
 * @author luminion
 * @since 1.0.0
 */
@AllArgsConstructor
public enum NamingStrategy {
    /**
     * 不做任何改变，原样输出
     */
    NO_CHANGE(e -> e),
    /**
     * 下划线转驼峰命名
     */
    UNDERLINE_TO_CAMEL_CASE(NamingStrategy::underlineToCamel),
    /**
     * 下划线转驼峰命名(首字母大写)
     */
    UNDERLINE_TO_PASCAL_CASE(e -> NamingStrategy.capitalFirst(NamingStrategy.underlineToCamel(e))),
    ;
    @Getter
    private final Function<String, String> function;

    /**
     * 下划线转驼峰
     *
     * @param name 待转内容
     */
    public static String underlineToCamel(String name) {
        // 快速检查
        if (StringUtils.isBlank(name)) {
            // 没必要转换
            return "";
        }
        String tempName = name;
        // 大写数字下划线组成转为小写 , 允许混合模式转为小写
        if (StringUtils.isCapitalMode(name) || StringUtils.isMixedMode(name)) {
            tempName = name.toLowerCase();
        }
        StringBuilder result = new StringBuilder();
        // 用下划线将原始字符串分割
        String[] camels = tempName.split("_");
        // 跳过原始字符串中开头、结尾的下换线或双重下划线
        // 处理真正的驼峰片段
        Arrays.stream(camels).filter(camel -> !StringUtils.isBlank(camel)).forEach(camel -> {
            if (result.length() == 0) {
                // 第一个驼峰片段，首字母都小写
                result.append(StringUtils.firstToLowerCase(camel));
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(capitalFirst(camel));
            }
        });
        return result.toString();
    }

    /**
     * 实体首字母大写
     *
     * @param name 待转换的字符串
     * @return 转换后的字符串
     */
    public static String capitalFirst(String name) {
        if (StringUtils.isNotBlank(name)) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return "";
    }


    /**
     * 去掉指定的前缀
     *
     * @param name   表名
     * @param prefix 前缀
     * @return 转换后的字符串
     */
    public static String removePrefix(String name, Set<String> prefix) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        // 判断是否有匹配的前缀，然后截取前缀
        return prefix.stream().filter(pf -> name.toLowerCase().startsWith(pf.toLowerCase()))
                .findFirst().map(pf -> name.substring(pf.length())).orElse(name);
    }

    /**
     * 去掉指定的后缀
     *
     * @param name   表名
     * @param suffix 后缀
     * @return 转换后的字符串
     */
    public static String removeSuffix(String name, Set<String> suffix) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        // 判断是否有匹配的后缀，然后截取后缀
        return suffix.stream().filter(sf -> name.toLowerCase().endsWith(sf.toLowerCase()))
                .findFirst().map(sf -> name.substring(0, name.length() - sf.length())).orElse(name);
    }


    /**
     * 去除前缀和后缀, 并转化
     *
     * @param converter 转化器
     * @param name      姓名
     * @param prefix    前缀
     * @param suffix    后缀
     * @return {@link String }
     */
    public static String doConvertName(String name, Set<String> prefix, Set<String> suffix, Function<String, String> converter) {
        String propertyName = name;
        // 删除前缀
        if (!prefix.isEmpty()) {
            propertyName = removePrefix(propertyName, prefix);
        }
        // 删除后缀
        if (!suffix.isEmpty()) {
            propertyName = removeSuffix(propertyName, suffix);
        }
        if (StringUtils.isBlank(propertyName)) {
            throw new IllegalArgumentException(String.format("%s 的名称转换结果为空，请检查是否配置问题", name));
        }
        return converter.apply(propertyName);
    }
}
