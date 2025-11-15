package io.github.luminion.generator.config.model;

import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TemplateFile;

/**
 * @author luminion
 * @since 1.0.0
 */
public class EntityQueryVOConfig {

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.ENTITY_QUERY_VO.getKey(),
            "%sQueryVO",
            "vo",
            "/templates/base/entityQueryVO.java",
            ".java"
    );
    
}
