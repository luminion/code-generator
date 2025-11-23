package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.Resolver;
import io.github.luminion.generator.config.core.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeEnv;
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
public class EntityQueryVOConfig implements TemplateRender {

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.ENTITY_QUERY_VO.getKey(),
            "%sQueryVO",
            "vo",
            "/templates/model/entityQueryVO.java",
            ".java"
    );
    /**
     * 导入的包
     */
    private Set<String> importPackages = new TreeSet<>();

    /**
     * 是否继承实体类
     */
    protected boolean extendsEntity = true;

    @Override
    public List<TemplateFile> renderTemplateFiles() {
        return Collections.singletonList(templateFile);
    }


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        Configurer<?> configurer = tableInfo.getConfigurer();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Resolver resolver = configurer.getResolver();

        // 关闭功能
        if (!globalConfig.isGenerateInsert() && !RuntimeEnv.isSqlBooster(globalConfig.getRuntimeEnv())){
            this.getTemplateFile().setGenerate(false);
        }
    
        if (extendsEntity) {
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
            if (globalConfig.isLombok()){
                importPackages.add("lombok.EqualsAndHashCode");
            }
            data.put("queryVOExtendsEntity", extendsEntity);
        } else {
            for (TableField field : tableInfo.getFields()) {
                if (field.isLogicDeleteField()) {
                    continue;
                }
                Optional.ofNullable(field.getJavaType().getPkg()).ifPresent(importPackages::add);
            }
        }

        // 全局包
        importPackages.addAll(globalConfig.getModelSerializableImportPackages());
        importPackages.addAll(globalConfig.getModelDocImportPackages());
        importPackages.addAll(globalConfig.getModelLombokImportPackages());


        // 导入包
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("queryVOJavaPkg", javaPackages);
        data.put("queryVOFramePkg", frameworkPackages);
        
        
        return data;
    }
}
