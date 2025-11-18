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
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.ENTITY.getKey(),
            "%s",
            "entity",
            "/templates/model/entity.java",
            ".java"
    );

    /**
     * 自定义继承的Entity类全称，带包名
     */
    protected String superClass;

    /**
     * 开启 ActiveRecord 模式
     */
    protected boolean activeRecord;
    /**
     * 是否生成实体时，生成字段注解
     */
    protected boolean tableFieldAnnotation;

    @Override
    public void init() {
        if (this.superClass != null && this.activeRecord) {
            log.warn("继承和父类和activeRecord同时开启,activeRecord将失效!!!");
        }
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        Set<String> importPackages = new TreeSet<>();
        data.put("activeRecord", this.activeRecord);

        if (this.tableFieldAnnotation) {
            data.put("tableFieldAnnotation", this.tableFieldAnnotation);
            importPackages.add("com.baomidou.mybatisplus.annotation.TableField");
        }

        GlobalConfig globalConfig = tableInfo.getConfigurer().getGlobalConfig();

        if (StringUtils.isNotBlank(this.superClass)) {
            data.put("entitySuperClass", ClassUtils.getSimpleName(this.superClass));
            importPackages.add(this.superClass);
        } else {
            // 无父类开启 AR 模式
            importPackages.add("com.baomidou.mybatisplus.extension.activerecord.Model");
        }
        if (tableInfo.isConvert()) {
            importPackages.add("com.baomidou.mybatisplus.annotation.TableName");
        }
        tableInfo.getFields().forEach(field -> {
            JavaFieldInfo columnType = field.getJavaType();
            if (null != columnType && null != columnType.getPkg()) {
                importPackages.add(columnType.getPkg());
            }
            if (field.isKeyFlag()) {
                // 主键
                if (field.isConvert() || field.isKeyIdentityFlag()) {
                    importPackages.add("com.baomidou.mybatisplus.annotation.TableId");
                }
            } else if (field.isConvert() || this.tableFieldAnnotation) {
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
        if (globalConfig.isLombok()) {
            if (this.superClass != null || this.activeRecord) {
                importPackages.add("lombok.EqualsAndHashCode");
            }
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelSerializableImportPackages());
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelLombokImportPackages());

        // 导入包
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("entityJavaPkg", javaPackages);
        data.put("entityFramePkg", frameworkPackages);

        // 注解
        TreeSet<String> annotations = new TreeSet<>();
        String comment = Optional.ofNullable(tableInfo.getComment()).orElse("");
        String tableName = tableInfo.getName();
        String schemaName = Optional.ofNullable(tableInfo.getSchemaName()).orElse("");
        switch (globalConfig.getDocType()) {
            case SPRING_DOC:
                annotations.add(String.format("@Schema(description = \"%s\")", comment));
                break;
            case SWAGGER:
                annotations.add(String.format("@ApiModel(description = \"%s\")", comment));
                break;
        }
        if (globalConfig.isLombok()) {
            annotations.add("@Data");
            if (globalConfig.isChainModel()) {
                annotations.add("@Accessors(chain = true)");
            }
            if (this.superClass != null || this.activeRecord) {
                annotations.add("@EqualsAndHashCode(callSuper = true)");
            }
        }
        if (tableInfo.isConvert()) {
            annotations.add(String.format("@TableName(\"%s%s\")", schemaName, tableName));
        }

        data.put("entityAnnotations", annotations);

        return data;
    }

}