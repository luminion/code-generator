package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.po.TemplateClassFile;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class TemplateConfig {
    private final Configurer configurer;

    /**
     * 生成文件的输出目录
     */
    private String outputDir = System.getProperty("user.dir") + "/src/main/java";

    /**
     * 是否打开输出目录
     */
    private boolean openOutputDir;

    /**
     * 输出文件覆盖(全局)
     */
    private boolean fileOverride;

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parentPackage = "com.example";

    /**
     * 父包模块名,会拼接在父包名之后
     */
    private String parentModule = "";


    private TemplateFile controller = new TemplateFile(
            TemplateFileEnum.CONTROLLER.getKey(),
            "%sController",
            "controller",
            "/templates/model/controller.java",
            ".java"
    );

    private TemplateFile service = new TemplateFile(
            TemplateFileEnum.SERVICE.getKey(),
            "%sService",
            "service",
            "/templates/mybatis_plus/service.java",
            ".java"
    );

    private TemplateFile serviceImpl = new TemplateFile(
            TemplateFileEnum.SERVICE_IMPL.getKey(),
            "%sServiceImpl",
            "service.impl",
            "/templates/mybatis_plus/serviceImpl.java",
            ".java"
    );

    private TemplateFile mapper = new TemplateFile(
            TemplateFileEnum.MAPPER.getKey(),
            "%sMapper",
            "mapper",
            "/templates/mybatis_plus/mapper.java",
            ".java"
    );

    private TemplateFile mapperXml = new TemplateFile(
            TemplateFileEnum.MAPPER_XML.getKey(),
            "%sMapper",
            "xml",
            "/templates/mybatis_plus/mapper.xml",
            ".xml"
    );

    private TemplateFile entity = new TemplateFile(
            TemplateFileEnum.ENTITY.getKey(),
            "%s",
            "model.entity",
            "/templates/model/entity.java",
            ".java"
    );


    private TemplateFile queryDto = new TemplateFile(
            TemplateFileEnum.QUERY_DTO.getKey(),
            "%sQueryDTO",
            "model.query",
            "/templates/model/queryDto.java",
            ".java"
    );


    private TemplateFile queryVo = new TemplateFile(
            TemplateFileEnum.QUERY_VO.getKey(),
            "%sVO",
            "model.vo",
            "/templates/model/queryVo.java",
            ".java"
    );

    private TemplateFile createDto = new TemplateFile(
            TemplateFileEnum.CREATE_DTO.getKey(),
            "%sCreateDTO",
            "model.command",
            "/templates/model/createDto.java",
            ".java"
    );

    private TemplateFile updateDto = new TemplateFile(
            TemplateFileEnum.UPDATE_DTO.getKey(),
            "%sUpdateDTO",
            "model.command",
            "/templates/model/updateDto.java",
            ".java"
    );

    private TemplateFile exportDto = new TemplateFile(
            TemplateFileEnum.EXPORT_DTO.getKey(),
            "%sExportDTO",
            "model.excel",
            "/templates/model/exportDto.java",
            ".java"
    );

    private TemplateFile importDto = new TemplateFile(
            TemplateFileEnum.IMPORT_DTO.getKey(),
            "%sImportDTO",
            "model.excel",
            "/templates/model/importDto.java",
            ".java"
    );

    private List<TemplateFile> templateFiles = new ArrayList<>();

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

    public void setFileOverride(boolean fileOverride) {
        this.fileOverride = fileOverride;
        templateFiles.forEach(e -> e.setFileOverride(fileOverride));
    }

    private String getParentPackage() {
        if (StringUtils.isNotBlank(parentModule)) {
            return parentPackage + "." + parentModule;
        }
        return parentPackage;
    }

    private String joinPackage(String subPackage) {
        String parent = getParentPackage();
        return StringUtils.isBlank(parent) ? subPackage : (parent + "." + subPackage);
    }

    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty("java.io.tmpdir");
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    public Map<String, TemplateClassFile> resolveTemplateFileMap(TableInfo tableInfo) {
        return templateFiles.stream().collect(Collectors.toMap(
                e -> e.getKey(), e -> {
                    e.validate();
                    String fileOutputDir = e.getFileOutputDir();
                    String joinPackage = this.joinPackage(e.getSubPackage());
                    if (fileOutputDir == null) {
                        fileOutputDir = joinPath(this.outputDir, joinPackage);
                        e.setFileOutputDir(fileOutputDir);
                    }
                    TemplateClassFile templateClassFile = new TemplateClassFile(e);
                    templateClassFile.setPackageName(joinPackage);
                    String classSimpleName = e.convertFormatName(tableInfo.getEntityName());
                    templateClassFile.setClassSimpleName(classSimpleName);
                    templateClassFile.setClassCanonicalName(joinPackage + "." + classSimpleName);
                    return templateClassFile;
                }));
    }

}
