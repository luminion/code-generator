package io.github.luminion.generator.config.core;

import io.github.luminion.generator.common.RenderField;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.IdStrategy;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateClassFile;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class EntityConfig implements TemplateRender {
    private final Configurer configurer;
    
    /**
     * 实体父类
     */
    private String entitySuperClass;
    /**
     * 指定生成的主键的ID类型
     */
    @RenderField
    protected IdStrategy idType = IdStrategy.ASSIGN_UUID;

    /**
     * 开启 ActiveRecord 模式
     */
    @RenderField
    protected boolean activeRecord;
    /**
     * 是否生成实体时，生成字段注解
     */
    @RenderField
    protected boolean tableFieldAnnotation;
    

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);

        data.put("entitySuperClass", ClassUtils.getSimpleName(entitySuperClass));


        // 导包
        data.putAll(resolveEntityImports(tableInfo));
        
        return data;
    }

    private Map<String, Object> resolveEntityImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();

        // 实体父类
        if (entitySuperClass!=null){
            importPackages.add(entitySuperClass);
        }

        Set<String> entityImportPackages = new TreeSet<>();
        if (tableFieldAnnotation) {
            entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getClassName());
        }
   
        if (tableInfo.isConvert()) {
            entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_NAME.getClassName());
        }
        if (activeRecord) {
            entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_ACTIVE_RECORD_MODEL.getClassName());
            entityImportPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
        }
        tableInfo.getFields().forEach(field -> {
            String propertyPkg = field.getPropertyPkg();
            if (null != propertyPkg) {
                entityImportPackages.add(propertyPkg);
            }

            if (field.isKeyFlag()) {
                // 主键
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_ID.getClassName());
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_ID_TYPE.getClassName());
            } else if (field.isConvert() || tableFieldAnnotation) {
                // 普通字段
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getClassName());
            }
            if (null != field.getFill()) {
                // 填充字段
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getClassName());
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_FIELD_FILL.getClassName());
            }
            if (field.isVersionField()) {
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_VERSION.getClassName());
            }
            if (field.isLogicDeleteField()) {
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_LOGIC.getClassName());
            }
        });

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
        data.put("entityFramePkg", frameworkPackages);
        data.put("entityJavaPkg", javaPackages);
        return data;
    }
    
    
}
