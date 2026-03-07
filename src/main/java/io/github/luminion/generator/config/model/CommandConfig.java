package io.github.luminion.generator.config.model;

import io.github.luminion.generator.render.api.MultiTemplateModelRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.ConfigResolver;
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
 * 命令功能配置
 * <p>
 * 整合创建DTO和更新DTO的命令配置，对应CQRS模式的Command概念
 *
 * @author luminion
 * @since 1.0.0
 */
@Data
public class CommandConfig implements MultiTemplateModelRender {

    /**
     * 创建模板文件
     */
    private TemplateFile createTemplateFile = new TemplateFile(
            TemplateFileEnum.CREATE_DTO.getKey(),
            "%sCreateDTO",
            "model.dto",
            "/templates/model/createDTO.java",
            ".java"
    );

    /**
     * 更新模板文件
     */
    private TemplateFile updateTemplateFile = new TemplateFile(
            TemplateFileEnum.UPDATE_DTO.getKey(),
            "%sUpdateDTO",
            "model.dto",
            "/templates/model/updateDTO.java",
            ".java"
    );

    @Override
    public void init() {
    }

    @Override
    public void renderDataPreProcess(TableInfo tableInfo) {
        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        // 关闭功能
        if (!globalConfig.isGenerateCreate()) {
            createTemplateFile.setGenerate(false);
        }
        if (!globalConfig.isGenerateUpdate()) {
            updateTemplateFile.setGenerate(false);
        }
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        Set<String> importPackages = new TreeSet<>();

        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        // 创建配置渲染数据
        renderCreateData(tableInfo, data, importPackages, configCollector, globalConfig);

        // 更新配置渲染数据
        renderUpdateData(tableInfo, data, importPackages, configCollector, globalConfig);

        return data;
    }

    private void renderCreateData(TableInfo tableInfo, Map<String, Object> data,
                                  Set<String> importPackages, ConfigCollector<?> configCollector,
                                  GlobalConfig globalConfig) {
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
    }

    private void renderUpdateData(TableInfo tableInfo, Map<String, Object> data,
                                  Set<String> importPackages, ConfigCollector<?> configCollector,
                                  GlobalConfig globalConfig) {
        String size = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_SIZE.getClassName();
        String notBlank = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_BLANK.getClassName();
        String notNull = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_NULL.getClassName();

        // 属性过滤
        Set<String> editExcludeColumns = configCollector.getStrategyConfig().getEditExcludeColumns();
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            if (field.getFill() != null &&
                    ("INSERT".equals(field.getFill()) || "INSERT_UPDATE".equals(field.getFill()) || "UPDATE".equals(field.getFill()))
            ) {
                continue;
            }
            if (editExcludeColumns.contains(field.getColumnName())) {
                continue;
            }
            Optional.ofNullable(field.getJavaType().getPkg()).ifPresent(importPackages::add);

            boolean notnullFlag = field.isKeyFlag() || field.isVersionField();
            boolean isString = "String".equals(field.getPropertyType());

            if (globalConfig.isValidated()) {
                if (field.isKeyFlag()) {
                    importPackages.add(notNull);
                }
                if (notnullFlag) {
                    if (isString) {
                        importPackages.add(notBlank);
                        importPackages.add(notNull);
                                       } else {
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

        data.put("updateDTOFramePkg", frameworkPackages);
        data.put("updateDTOJavaPkg", javaPackages);
    }

    @Override
    public void renderDataPostProcess(TableInfo tableInfo, Map<String, Object> renderData) {
    }

    @Override
    public List<TemplateFile> renderTemplateFiles() {
        List<TemplateFile> files = new ArrayList<>();
        files.add(createTemplateFile);
        files.add(updateTemplateFile);
        return files;
    }
}
