package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.Resolver;
import io.github.luminion.generator.config.core.GlobalConfig;
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
public class EntityQueryDTOConfig implements TemplateRender {

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.ENTITY_QUERY_DTO.getKey(),
            "%sQueryDTO",
            "dto.query",
            "/templates/mybatisplus/entityQueryDTO.java",
            ".java"
    );
    
    /**
     * 是否继承实体类
     */
    protected boolean extendsEntity = true;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        Configurer configurer = tableInfo.getConfigurer();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Resolver resolver = configurer.getResolver();
        TreeSet<String> importPackages = new TreeSet<>();
      
        importPackages.add(List.class.getCanonicalName());
        
        if (extendsEntity) {
            String entityClassName = resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_DTO, tableInfo);
            String entityClassSimpleName = resolver.getClassSimpleName(TemplateFileEnum.ENTITY, tableInfo);
            importPackages.add(entityClassName);
            if (globalConfig.isLombok()){
                importPackages.add("lombok.EqualsAndHashCode");
            }
            data.put("queryDTOExtendsEntity", extendsEntity);
        }
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            Optional.ofNullable(field.getJavaType().getPkg()).ifPresent(importPackages::add);
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelSerializableImportPackages());
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelLombokImportPackages());

        // 导入包
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("queryDTOJavaPkg", javaPackages);
        data.put("queryDTOFramePkg", frameworkPackages);
        
        
        return data;
    }
}
