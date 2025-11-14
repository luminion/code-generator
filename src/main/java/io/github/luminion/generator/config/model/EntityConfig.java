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
package io.github.luminion.generator.config.model;

import io.github.luminion.generator.config.core.GlobalConfig;
import io.github.luminion.generator.config.core.StrategyConfig;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实体属性配置
 *
 * @author nieqiurong 2020/10/11.
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
@Data
public class EntityConfig implements TemplateRender {

    /**
     * 自定义继承的Entity类全称，带包名
     */
    protected String superClass;

    /**
     * 实体是否生成 serialVersionUID
     */
    protected boolean serialUID = true;

    /**
     * 是否启用 {@link java.io.Serial} (需JAVA 14) 注解
     *
     */
    protected boolean serialAnnotation;

    /**
     * 开启 ActiveRecord 模式（默认 false）
     */
    protected boolean activeRecord;
    
    /**
     * 是否使用mybatis plus
     */
    protected boolean mybatisPlus;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        data.put("activeRecord", this.activeRecord);
        data.put("entitySerialVersionUID", this.serialUID);
        data.put("entitySerialAnnotation", this.serialAnnotation);
        data.put("superEntityClass", ClassUtils.getSimpleName(this.superClass));
        // 导入包
        Set<String> importPackages = this.entityImportPackages(tableInfo);
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("entityImportPackages4Java", javaPackages);
        data.put("entityImportPackages4Framework", frameworkPackages);
        return data;
    }

    /**
     * 导包处理
     */
    public Set<String> entityImportPackages(TableInfo tableInfo) {
        GlobalConfig globalConfig = tableInfo.getConfigurer().getGlobalConfig();
        StrategyConfig strategyConfig = tableInfo.getConfigurer().getStrategyConfig();
        TreeSet<String> importPackages = new TreeSet<>();
        if (StringUtils.isNotBlank(this.superClass)) {
            importPackages.add(this.superClass);
        } else {
            if (this.activeRecord) {
                // 无父类开启 AR 模式
                importPackages.add("com.baomidou.mybatisplus.extension.activerecord.Model");
            }
        }
        if (this.serialUID || this.activeRecord) {
            importPackages.add(Serializable.class.getCanonicalName());
            if (this.serialAnnotation) {
                importPackages.add("java.io.Serial");
            }
        }
        if (tableInfo.isConvert()) {
            importPackages.add("com.baomidou.mybatisplus.annotation.TableName");
        }
        if (null != strategyConfig.getIdType() && tableInfo.isHavePrimaryKey()) {
            // 指定需要 IdType 场景
            importPackages.add("com.baomidou.mybatisplus.annotation.IdType");
            importPackages.add("com.baomidou.mybatisplus.annotation.TableId");
        }
        tableInfo.getFields().forEach(field -> {
            JavaFieldInfo columnType = field.getJavaType();
            if (null != columnType && null != columnType.getPkg()) {
                importPackages.add(columnType.getPkg());
            }
            if (field.isKeyFlag()) {
                // 主键
                if (field.isConvert() || field.isKeyIdentityFlag()) {
                    importPackages.add("com.baomidou.mybatisplus.annotation.TableField");
                }
                // 自增
                if (field.isKeyIdentityFlag()) {
                    importPackages.add("com.baomidou.mybatisplus.annotation.IdType");
                }
            } else if (field.isConvert()) {
                // 普通字段
                importPackages.add("com.baomidou.mybatisplus.annotation.TableField");
            }
            if (null != field.getFill()) {
                // 填充字段
                importPackages.add("com.baomidou.mybatisplus.annotation.TableField");
                importPackages.add("com.baomidou.mybatisplus.annotation.FieldFill");
            }
            if (field.isVersionField()) {
                importPackages.add("com.baomidou.mybatisplus.annotation.Version");
            }
            if (field.isLogicDeleteField()) {
                importPackages.add("com.baomidou.mybatisplus.annotation.TableLogic");
            }
        });
        if (globalConfig.isSpringdoc()) {
            importPackages.add("io.swagger.v3.oas.annotations.media.Schema");
        }
        if (globalConfig.isLombok()) {
            if (globalConfig.isLombokChainModel()) {
                importPackages.add("lombok.experimental.Accessors");
            }
            if (this.superClass != null) {
                importPackages.add("lombok.EqualsAndHashCode");
            }
            if (superClass!=null || activeRecord){
                importPackages.add("lombok.EqualsAndHashCode");
            }
            importPackages.add("lombok.Data");
        }
        if (globalConfig.isSwagger()) {
            importPackages.add("io.swagger.annotations.ApiModel");
            importPackages.add("io.swagger.annotations.ApiModelProperty");
        }
        return importPackages;
    }

}