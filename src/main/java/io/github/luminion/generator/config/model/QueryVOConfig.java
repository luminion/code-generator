package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.TemplateModelRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.ConfigResolver;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeClass;
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
public class QueryVOConfig implements TemplateModelRender {

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.QUERY_VO.getKey(),
            "%sVO",
            "model.vo",
            "/templates/model/queryVO.java",
            ".java"
    );

    /**
     * 是否继承实体类
     */
    protected boolean extendsEntity = false;

    @Override
    public TemplateFile renderTemplateFile() {
        return templateFile;
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        Set<String> importPackages = new TreeSet<>();

        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        // 关闭功能
        if (!globalConfig.isGenerateSelectByXml() && !RuntimeEnv.isSqlBooster(globalConfig.getRuntimeEnv())) {
            this.renderTemplateFile().setGenerate(false);
        }

        if (extendsEntity) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
            if (globalConfig.isLombok()) {
                importPackages.add(RuntimeClass.LOMBOK_EQUALS_AND_HASH_CODE.getClassName());
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
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));

        data.put("queryVOFramePkg", frameworkPackages);
        data.put("queryVOJavaPkg", javaPackages);


        return data;
    }
}
