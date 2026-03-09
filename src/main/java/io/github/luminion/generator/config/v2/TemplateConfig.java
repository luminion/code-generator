package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.config.Configurer;
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
    protected final Configurer configurer;

    /**
     * 生成文件的输出目录
     */
    protected String outputDir = System.getProperty("user.dir") + "/src/main/java";

    /**
     * 是否打开输出目录
     */
    protected boolean openOutputDir;

    /**
     * 输出文件覆盖(全局)
     */
    protected boolean fileOverride;

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    protected String parentPackage = "com.example";

    /**
     * 父包模块名
     */
    protected String parentPackageModule = "";
    

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
            "model.entity",
            "/templates/model/entity.java",
            ".java"
    );


    protected TemplateFile queryDto = new TemplateFile(
            TemplateFileEnum.QUERY_DTO.getKey(),
            "%sQueryDTO",
            "model.query",
            "/templates/model/queryDto.java",
            ".java"
    );


    protected TemplateFile queryVo = new TemplateFile(
            TemplateFileEnum.QUERY_VO.getKey(),
            "%sVO",
            "model.vo",
            "/templates/model/queryVo.java",
            ".java"
    );

    protected TemplateFile createDto = new TemplateFile(
            TemplateFileEnum.CREATE_DTO.getKey(),
            "%sCreateDTO",
            "model.command",
            "/templates/model/createDto.java",
            ".java"
    );

    protected TemplateFile updateDto = new TemplateFile(
            TemplateFileEnum.UPDATE_DTO.getKey(),
            "%sUpdateDTO",
            "model.command",
            "/templates/model/updateDto.java",
            ".java"
    );

    protected TemplateFile exportDto = new TemplateFile(
            TemplateFileEnum.EXPORT_DTO.getKey(),
            "%sExportDTO",
            "model.excel",
            "/templates/model/exportDto.java",
            ".java"
    );

    protected TemplateFile importDto = new TemplateFile(
            TemplateFileEnum.IMPORT_DTO.getKey(),
            "%sImportDTO",
            "model.excel",
            "/templates/model/importDto.java",
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
        
        templateFiles.add(exportDto);
        templateFiles.add(importDto);
    }

}
