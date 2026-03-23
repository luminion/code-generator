package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.IdStrategy;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
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
    protected IdStrategy idType = IdStrategy.AUTO;

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

        if (tableFieldAnnotation) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getCanonicalName());
        }
   
        if (tableInfo.isConvert()) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_NAME.getCanonicalName());
        }
        if (activeRecord) {
            importPackages.add(RuntimeClass.MYBATIS_PLUS_ACTIVE_RECORD_MODEL.getCanonicalName());
            importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getCanonicalName());
        }
        tableInfo.getFields().forEach(field -> {
            String propertyPkg = field.getPropertyPkg();
            if (null != propertyPkg) {
                importPackages.add(propertyPkg);
            }

            if (field.isKeyFlag()) {
                // 主键
                importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_ID.getCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_ID_TYPE.getCanonicalName());
            } else if (field.isConvert() || tableFieldAnnotation) {
                // 普通字段
                importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getCanonicalName());
            }
            if (null != field.getFill()) {
                // 填充字段
                importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getCanonicalName());
                importPackages.add(RuntimeClass.MYBATIS_PLUS_FIELD_FILL.getCanonicalName());
            }
            if (field.isVersionField()) {
                importPackages.add(RuntimeClass.MYBATIS_PLUS_VERSION.getCanonicalName());
            }
            if (field.isLogicDeleteField()) {
                importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_LOGIC.getCanonicalName());
            }
        });
        
        if(tableInfo.isConvert()){
            importPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_NAME.getCanonicalName());
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
        data.put("entityFramePkg", frameworkPackages);
        data.put("entityJavaPkg", javaPackages);
        return data;
    }
    
    
}
