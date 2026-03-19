package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.RenderField;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateClassFile;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class CommandConfig implements TemplateRender {
    private final Configurer configurer;

    /**
     * 生成参数校验相关注解
     */
    @RenderField
    private boolean enableValid;
    /**
     * 生成新增方法及配套类
     */
    @RenderField
    private boolean enableCreateByDto = true;

    /**
     * 生成更新方法及配套类
     */
    @RenderField
    private boolean enableUpdateByDto = true;

    /**
     * 生成删除方法及配套类
     */
    @RenderField
    private boolean enableDeleteById = true;

    /**
     * 新增和修改需要需要排除的字段
     */
    @RenderField
    private final Set<String> editExcludeColumns = new HashSet<>();

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);

        // 导包
        data.putAll(resolveCreateDtoImports(tableInfo));
        data.putAll(resolveUpdateDtoImports(tableInfo));

        return data;
    }


    private Map<String, Object> resolveCreateDtoImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;
        Set<String> importPackages = new TreeSet<>();

        String size = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_SIZE.getClassName();
        String notBlank = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_BLANK.getClassName();
        String notNull = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_NULL.getClassName();

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
            Optional.ofNullable(field.getPropertyPkg()).ifPresent(importPackages::add);
            TableField.MetaInfo metaInfo = field.getMetaInfo();
            boolean isString = "String".equals(field.getPropertyType());
            //boolean notnullFlag = !metaInfo.isNullable() && metaInfo.getDefaultValue() == null;
            boolean notnullFlag = !metaInfo.isNullable();
            if (enableValid) {
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
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());


        // 导包数据
        Map<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("createDtoFramePkg", frameworkPackages);
        data.put("createDtoJavaPkg", javaPackages);
        return data;
    }


    private Map<String, Object> resolveUpdateDtoImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        String size = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_SIZE.getClassName();
        String notBlank = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_BLANK.getClassName();
        String notNull = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_VALIDATION_NOT_NULL.getClassName();

        // 属性过滤
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            boolean isInsertFill = "INSERT".equals(field.getFill()) || "INSERT_UPDATE".equals(field.getFill());
            boolean isUpdateFill = "UPDATE".equals(field.getFill());
            boolean isFill = isInsertFill || isUpdateFill;
            if (field.getFill() != null && isFill) {
                continue;
            }
            if (editExcludeColumns.contains(field.getColumnName())) {
                continue;
            }
            Optional.ofNullable(field.getPropertyPkg()).ifPresent(importPackages::add);

            boolean notnullFlag = field.isKeyFlag() || field.isVersionField();
            boolean isString = "String".equals(field.getPropertyType());

            if (enableValid) {
                if (field.isKeyFlag()) {
                    importPackages.add(notNull);
                }
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
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelImportPackages());

        // 导包数据
        HashMap<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("updateDtoFramePkg", frameworkPackages);
        data.put("updateDtoJavaPkg", javaPackages);
        return data;
    }

}
