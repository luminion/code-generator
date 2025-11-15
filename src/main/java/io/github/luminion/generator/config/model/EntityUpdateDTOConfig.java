package io.github.luminion.generator.config.model;

import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Data;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class EntityUpdateDTOConfig {

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.ENTITY_INSERT_DTO.getKey(),
            "%sUpdateDTO",
            "dto.command",
            "/templates/base/entityUpdateDTO.java",
            ".java"
    );
}
