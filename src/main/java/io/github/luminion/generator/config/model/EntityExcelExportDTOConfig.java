package io.github.luminion.generator.config.model;

import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TemplateFile;

/**
 * @author luminion
 * @since 1.0.0
 */
public class EntityExcelExportDTOConfig {
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
}
