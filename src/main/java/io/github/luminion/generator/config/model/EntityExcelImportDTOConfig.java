package io.github.luminion.generator.config.model;

import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Data;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class EntityExcelImportDTOConfig {
    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.ENTITY_EXCEL_IMPORT_DTO.getKey(),
            "%sExcelImportDTO",
            "dto.excel",
            "/templates/base/entityExcelImportDTO.java",
            ".java"
    );
    
}
