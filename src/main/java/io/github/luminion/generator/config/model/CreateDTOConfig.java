package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.Resolver;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeClass;
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
public class CreateDTOConfig implements TemplateRender {

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.CREATE_DTO.getKey(),
            "%sCreateDTO",
            "model.dto",
            "/templates/model/createDTO.java",
            ".java"
    );

    @Override
    public TemplateFile renderTemplateFile() {
        return templateFile;
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        Set<String> importPackages = new TreeSet<>();

        Resolver resolver = tableInfo.getResolver();
        ConfigCollector<?> configCollector = resolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        // 关闭功能
        if (!globalConfig.isGenerateCreate()) {
            this.renderTemplateFile().setGenerate(false);
        }

        String size = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_SIZE.getClassName();
        String notBlank = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_BLANK.getClassName();
        String notNull = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_NULL.getClassName();

        // 属性过滤
        Set<String> editExcludeColumns = configCollector.getStrategyConfig().getEditExcludeColumns();
        for (TableField field : tableInfo.getFields()) {
            if (field.isKeyFlag()) {
                continue;
            }
            if (field.isVersionField()) {
                continue;
            }
            if (field.isLogicDeleteField()) {
                continue;
            }
            if (field.getFill() != null &&
                    ("INSERT".equals(field.getFill()) || "INSERT_UPDATE".equals(field.getFill()))
            ) {
                continue;
            }
            if (editExcludeColumns.contains(field.getColumnName())) {
                continue;
            }
            Optional.ofNullable(field.getJavaType().getPkg()).ifPresent(importPackages::add);
            TableField.MetaInfo metaInfo = field.getMetaInfo();
            boolean isString = "String".equals(field.getPropertyType());
            //boolean notnullFlag = !metaInfo.isNullable() && metaInfo.getDefaultValue() == null;
            boolean notnullFlag = !metaInfo.isNullable();

            if (globalConfig.isValidated()) {
                if (notnullFlag) {
                    if (isString) {
                        importPackages.add(notBlank);
                    } else {
                        importPackages.add(notNull);
                    }
                }
                if (isString && globalConfig.isValidated()) {
                    importPackages.add(size);
                }
            }
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelSerializableImportPackages());
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelLombokImportPackages());


        // 导入包
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("createDTOFramePkg", frameworkPackages);
        data.put("createDTOJavaPkg", javaPackages);
        return data;
    }
}
