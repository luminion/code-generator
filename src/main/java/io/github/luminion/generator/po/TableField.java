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
package io.github.luminion.generator.po;

import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.config.base.StrategyConfig;
import io.github.luminion.generator.enums.JdbcType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 表字段信息
 *
 * @author YangHu
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
@Data
public class TableField {

    /**
     * 数据库字段名称(原始名)
     */
    private String name;
    /**
     * 是否数据库关键字
     */
    private boolean keyWords;

    /**
     * 数据库字段（关键字含转义符号）
     */
    private String columnName;
    /**
     * 字段元数据信息
     */
    private MetaInfo metaInfo;

    /**
     * java属性名称
     */
    private String propertyName;

    /**
     * java属性类型
     */
    private String propertyType;

    /**
     * java数据库字段类型
     */
    private JavaFieldInfo JavaType;

    /**
     * 字段注释
     */
    private String comment;
    

    // 特殊配置
    /**
     * 是否做注解转换
     */
    private boolean convert;
    /**
     * 是否主键
     */
    private boolean keyFlag;
    /**
     * 主键是否为自增类型
     */
    private boolean keyIdentityFlag;
    /**
     * 是否版本字段
     */
    private boolean versionField;
    /**
     * 是否逻辑删除字段
     *
     */
    private boolean logicDeleteField;
    /**
     * 填充
     */
    private String fill;
    /**
     * 自定义查询字段列表
     */
    private Map<String, Object> customMap;


    /**
     * 按 JavaBean 规则来生成 get 和 set 方法后面的属性名称
     * 需要处理一下特殊情况：
     * <p>
     * 1、如果只有一位，转换为大写形式
     * 2、如果多于 1 位，只有在第二位是小写的情况下，才会把第一位转为小写
     * <p>
     * 我们并不建议在数据库对应的对象中使用基本类型，因此这里不会考虑基本类型的情况
     */
    public String getCapitalName() {
        if (propertyName.length() == 1) {
            return propertyName.toUpperCase();
        }
        if (Character.isLowerCase(propertyName.charAt(1))) {
            return Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        }
        return propertyName;
    }

    /**
     * 元数据信息
     *
     * @author nieqiurong 2021/2/8
     * @since 3.5.0
     */
    @Data
    public static class MetaInfo {

        /**
         * 表名称
         */
        private String tableName;

        /**
         * 字段名称
         */
        private String columnName;

        /**
         * 字段长度
         */
        private int length;

        /**
         * 是否非空
         */
        private boolean nullable;

        /**
         * 字段注释
         */
        private String remarks;

        /**
         * 字段默认值
         */
        private String defaultValue;

        /**
         * 字段精度
         */
        private int scale;

        /**
         * JDBC类型
         */
        private JdbcType jdbcType;

        /**
         * 类型名称(可用做额外判断处理,例如在pg下,json,uuid,jsonb,ts query这种都认为是OHTER 1111)
         */
        private String typeName;
    }
}
