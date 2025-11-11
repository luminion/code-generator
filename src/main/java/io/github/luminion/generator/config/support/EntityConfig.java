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
package io.github.luminion.generator.config.support;

import io.github.luminion.generator.config.common.INameConvert;
import io.github.luminion.generator.config.enums.IdType;
import io.github.luminion.generator.config.po.TableInfo;
import io.github.luminion.generator.config.rules.IColumnType;
import io.github.luminion.generator.config.rules.NamingStrategy;
import io.github.luminion.generator.config.fill.IFill;
import io.github.luminion.generator.config.fill.ITemplate;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.ReflectUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
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
public class EntityConfig implements ITemplate {

    /**
     * 自定义继承的Entity类全称，带包名
     */
    protected String superClass;

    /**
     * 乐观锁字段名称(数据库字段)
     *
     * @since 3.5.0
     */
    protected String versionColumnName;

    /**
     * 乐观锁属性名称(实体字段)
     *
     * @since 3.5.0
     */
    protected String versionPropertyName;

    /**
     * 逻辑删除字段名称(数据库字段)
     *
     * @since 3.5.0
     */
    protected String logicDeleteColumnName;

    /**
     * 逻辑删除属性名称(实体字段)
     *
     * @since 3.5.0
     */
    protected String logicDeletePropertyName;

    /**
     * 自定义基础的Entity类，公共字段
     */
    protected final Set<String> superEntityColumns = new HashSet<>();

    /**
     * 自定义忽略字段
     * <a href="https://github.com/baomidou/generator/issues/46">...</a>
     */
    protected final Set<String> ignoreColumns = new HashSet<>();

    /**
     * 表填充字段
     */
    protected final List<IFill> tableFillList = new ArrayList<>();



    /**
     * 实体是否生成 serialVersionUID
     */
    protected boolean serialVersionUID = true;

    /**
     * 是否启用 {@link java.io.Serial} (需JAVA 14) 注解
     *
     * @since 3.5.11
     */
    protected boolean serialAnnotation;

    /**
     * 【实体】是否生成字段常量（默认 false）<br>
     * -----------------------------------<br>
     * public static final String ID = "test_id";
     */
    protected boolean columnConstant;



    /**
     * 是否生成实体时，生成字段注解（默认 false）
     */
    protected boolean tableFieldAnnotationEnable;

    /**
     * 开启 ActiveRecord 模式（默认 false）
     *
     * @since 3.5.0
     */
    protected boolean activeRecord;

    /**
     * <p>
     * 父类 Class 反射属性转换为公共字段
     * </p>
     *
     * @param clazz 实体父类 Class
     */
    public void convertSuperEntityColumns(Class<?> clazz) {
        Map<String, Field> fieldMap = ReflectUtils.fieldMap(clazz);
        // todo 待完善 原逻辑
//        List<Field> fields = TableInfoHelper.getAllFields(clazz);
//        this.superEntityColumns.addAll(fieldMap.values().stream().map(field -> {
//            TableId tableId = field.getAnnotation(Class.forName());
//            if (tableId != null && StringUtils.isNotBlank(tableId.value())) {
//                return tableId.value();
//            }
//            TableField tableField = field.getAnnotation(TableField.class);
//            if (tableField != null && StringUtils.isNotBlank(tableField.value())) {
//                return tableField.value();
//            }
//            if (null == columnNaming || columnNaming == NamingStrategy.no_change) {
//                return field.getName();
//            }
//            return StringUtils.camelToUnderline(field.getName());
//        }).collect(Collectors.toSet()));
    }

    /**
     * 匹配父类字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     * @since 3.5.0
     */
    public boolean matchSuperEntityColumns(String fieldName) {
        // 公共字段判断忽略大小写【 部分数据库大小写不敏感 】
        return superEntityColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    /**
     * 匹配忽略字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     * @since 3.5.0
     */
    public boolean matchIgnoreColumns(String fieldName) {
        return ignoreColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = ITemplate.super.renderData(tableInfo);
        data.put("idType", idType == null ? null : idType.toString());
        data.put("logicDeleteFieldName", this.logicDeleteColumnName);
        data.put("versionFieldName", this.versionColumnName);
        data.put("activeRecord", this.activeRecord);
        data.put("entitySerialVersionUID", this.serialVersionUID);
        data.put("entitySerialAnnotation", this.serialAnnotation);
        data.put("entityColumnConstant", this.columnConstant);
        data.put("entityBooleanColumnRemoveIsPrefix", this.booleanColumnRemoveIsPrefix);
        data.put("superEntityClass", ClassUtils.getSimpleName(this.superClass));
        GlobalConfig globalConfig = tableInfo.getConfigurer().getGlobalConfig();
        ModelConfig modelConfig = tableInfo.getConfigurer().getModelConfig();

        boolean excelImport = globalConfig.isGenerateImport() && modelConfig.isQueryDTOExtendsEntity();
        boolean excelExport = globalConfig.isGenerateExport() && modelConfig.isQueryVOExtendsEntity();
        data.put("excelOnEntity", excelImport || excelExport);

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
     *
     * @since 3.5.0
     */
    public Set<String> entityImportPackages(TableInfo tableInfo) {
        GlobalConfig globalConfig = tableInfo.getConfigurer().getGlobalConfig();
        ModelConfig modelConfig = tableInfo.getConfigurer().getModelConfig();
        TreeSet<String> importPackages = new TreeSet<>();
        if (StringUtils.isNotBlank(this.superClass)) {
            importPackages.add(this.superClass);
        } else {
            if (this.activeRecord) {
                // 无父类开启 AR 模式
                importPackages.add("com.baomidou.mybatisplus.extension.activerecord.Model");
            }
        }
        if (this.serialVersionUID || this.activeRecord) {
            importPackages.add(Serializable.class.getCanonicalName());
            if (this.serialAnnotation) {
                importPackages.add("java.io.Serial");
            }
        }
        if (tableInfo.isConvert()) {
            importPackages.add("com.baomidou.mybatisplus.annotation.TableName");
        }
        if (null != this.idType && tableInfo.isHavePrimaryKey()) {
            // 指定需要 IdType 场景
            importPackages.add("com.baomidou.mybatisplus.annotation.IdType");
            importPackages.add("com.baomidou.mybatisplus.annotation.TableId");
        }
        tableInfo.getFields().forEach(field -> {
            IColumnType columnType = field.getColumnType();
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
            if (globalConfig.isChainModel()) {
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
        if (globalConfig.isGenerateExport() && modelConfig.isQueryVOExtendsEntity()) {
            String excelIgnoreUnannotated = globalConfig.resolveExcelClassCanonicalName("annotation.ExcelIgnoreUnannotated");
            String excelProperty = globalConfig.resolveExcelClassCanonicalName("annotation.ExcelProperty");
            importPackages.add(excelIgnoreUnannotated);
            importPackages.add(excelProperty);
        }
        return importPackages;
    }

}