package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.config.GeneratorConfig;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class TemplateConfig {
    protected final GeneratorConfig generatorConfig;

    protected TemplateFile controller = new TemplateFile(
            TemplateFileEnum.CONTROLLER.getKey(),
            "%sController",
            "controller",
            "/templates/model/controller.java",
            ".java"
    );

    protected TemplateFile service = new TemplateFile(
            TemplateFileEnum.SERVICE.getKey(),
            "%sService",
            "service",
            "/templates/mybatis_plus/service.java",
            ".java"
    );

    protected TemplateFile serviceImpl = new TemplateFile(
            TemplateFileEnum.SERVICE.getKey(),
            "%sService",
            "service",
            "/templates/mybatis_plus/service.java",
            ".java"
    );

    protected TemplateFile mapper = new TemplateFile(
            TemplateFileEnum.MAPPER.getKey(),
            "%sMapper",
            "mapper",
            "/templates/mybatis_plus/mapper.java",
            ".java"
    );

    protected TemplateFile mapperXml = new TemplateFile(
            TemplateFileEnum.MAPPER_XML.getKey(),
            "%sMapper",
            "xml",
            "/templates/mybatis_plus/mapper.xml",
            ".xml"
    );

    protected TemplateFile entity = new TemplateFile(
            TemplateFileEnum.ENTITY.getKey(),
            "%s",
            "entity",
            "/templates/model/entity.java",
            ".java"
    );


    protected TemplateFile queryDto = new TemplateFile(
            TemplateFileEnum.QUERY_DTO.getKey(),
            "%sQueryDTO",
            "model.dto",
            "/templates/model/queryDTO.java",
            ".java"
    );


    protected TemplateFile queryVo = new TemplateFile(
            TemplateFileEnum.QUERY_VO.getKey(),
            "%sVO",
            "model.vo",
            "/templates/model/queryVO.java",
            ".java"
    );

    protected TemplateFile createDto = new TemplateFile(
            TemplateFileEnum.CREATE_DTO.getKey(),
            "%sCreateDTO",
            "model.dto",
            "/templates/model/createDTO.java",
            ".java"
    );

    protected TemplateFile updateDto = new TemplateFile(
            TemplateFileEnum.UPDATE_DTO.getKey(),
            "%sUpdateDTO",
            "model.dto",
            "/templates/model/updateDTO.java",
            ".java"
    );

    protected TemplateFile excelExportDto = new TemplateFile(
            TemplateFileEnum.EXPORT_DTO.getKey(),
            "%sExportDTO",
            "model.excel",
            "/templates/model/exportDTO.java",
            ".java"
    );

    protected TemplateFile excelImportDto = new TemplateFile(
            TemplateFileEnum.IMPORT_DTO.getKey(),
            "%sImportDTO",
            "model.excel",
            "/templates/model/importDTO.java",
            ".java"
    );
    
    protected List<TemplateFile> templateFiles = new ArrayList<>();
    
    {
        // 添加模板文件
        templateFiles.add(controller);
        templateFiles.add(service);
        templateFiles.add(serviceImpl);
        templateFiles.add(mapper);
        templateFiles.add(mapperXml);
        templateFiles.add(entity);
        templateFiles.add(queryDto);
        templateFiles.add(queryVo);
        templateFiles.add(createDto);
        templateFiles.add(updateDto);
        templateFiles.add(excelExportDto);
        templateFiles.add(excelImportDto);
    }

}
