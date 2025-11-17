package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.core.GlobalConfig;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
@Slf4j
public class EntityExcelExportDTOConfig implements TemplateRender {
    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.ENTITY_EXCEL_EXPORT_DTO.getKey(),
            "%sExcelExportDTO",
            "dto.excel",
            "/templates/base/entityExcelExportDTO.java",
            ".java"
    );

    /**
     * 自定义继承的Entity类全称，带包名
     */
    protected String superClass;



    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        GlobalConfig globalConfig = tableInfo.getConfigurer().getGlobalConfig();
        Set<String> importPackages = new TreeSet<>();

        if (superClass != null) {
            data.put("excelExportDTOSuperClass", ClassUtils.getSimpleName(this.superClass));
            importPackages.add(this.superClass);
        }
        if (globalConfig.isSerializableUID()) {
            importPackages.add("java.io.Serializable");
            if (globalConfig.isSerializableAnnotation()) {
                importPackages.add("java.io.Serial");
            }
        }

        if (globalConfig.isLombok()) {
            if (globalConfig.isChainModel()) {
                log.info("excel导出, 自动忽略chainModel, (在Excel框架中, 链式setter存在无法赋值的情况)");
            }
            if (this.superClass != null) {
                importPackages.add("lombok.EqualsAndHashCode");
            }
            if (superClass != null) {
                importPackages.add("lombok.EqualsAndHashCode");
            }
            importPackages.add("lombok.Data");
        }

        switch (globalConfig.getDocType()) {
            case SPRING_DOC:
                importPackages.add("io.swagger.v3.oas.annotations.media.Schema");
                break;
            case SWAGGER:
                importPackages.add("io.swagger.annotations.ApiModel");
                importPackages.add("io.swagger.annotations.ApiModelProperty");
                break;
        }
        String excelIgnoreUnannotated = globalConfig.getExcelApi().packagePrefix + "annotation.ExcelIgnoreUnannotated";
        String excelProperty = globalConfig.getExcelApi().packagePrefix + "annotation.ExcelProperty";
        importPackages.add(excelIgnoreUnannotated);
        importPackages.add(excelProperty);
        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            JavaFieldInfo columnType = field.getJavaType();
            if (null != columnType && null != columnType.getPkg()) {
                importPackages.add(columnType.getPkg());
            }
        }

        // 导入包
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("excelExportDTOJavaPkg", javaPackages);
        data.put("excelExportDTOFramePkg", frameworkPackages);
        return data;
    }
}
