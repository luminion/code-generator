package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.core.GlobalConfig;
import io.github.luminion.generator.enums.FieldFill;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class EntityUpdateDTOConfig implements TemplateRender{

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.ENTITY_UPDATE_DTO.getKey(),
            "%sUpdateDTO",
            "dto.command",
            "/templates/model/entityUpdateDTO.java",
            ".java"
    );

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        GlobalConfig globalConfig = tableInfo.getConfigurer().getGlobalConfig();
        Set<String> importPackages = new TreeSet<>();

        // 关闭功能
        if (!globalConfig.isGenerateUpdate()){
            this.getTemplateFile().setGenerate(false);
        }


        String size = globalConfig.getJavaEEApi().getPackagePrefix() + "validation.constraints.Size";
        String notBlank = globalConfig.getJavaEEApi().getPackagePrefix() + "validation.constraints.NotBlank";
        String notNull = globalConfig.getJavaEEApi().getPackagePrefix() + "validation.constraints.NotNull";

        // 属性过滤
        Set<String> editExcludeColumns = tableInfo.getConfigurer().getStrategyConfig().getEditExcludeColumns();
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            if (FieldFill.INSERT.name().equals(field.getFill()) || FieldFill.INSERT_UPDATE.name().equals(field.getFill()) || FieldFill.UPDATE.name().equals(field.getFill())) {
                continue;
            }
            if (field.isKeyFlag()){
                importPackages.add(notNull);
            }
            if (editExcludeColumns.contains(field.getColumnName())) {
                continue;
            }
            Optional.ofNullable(field.getJavaType().getPkg()).ifPresent(importPackages::add);
            boolean notnullFlag = field.isKeyFlag() || field.isVersionField();
            boolean isString = "String".equals(field.getPropertyType());
            if (notnullFlag) {
                if (isString) {
                    importPackages.add(notBlank);
                } else {
                    importPackages.add(notNull);
                }
            }
            if (isString) {
                importPackages.add(size);
            }
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelSerializableImportPackages());
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelLombokImportPackages());


        // 导入包
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("updateDTOJavaPkg", javaPackages);
        data.put("updateDTOFramePkg", frameworkPackages);
        return data;
    }
    
}
