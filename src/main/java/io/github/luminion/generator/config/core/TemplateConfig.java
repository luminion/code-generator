package io.github.luminion.generator.config.core;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateFile;
import io.github.luminion.generator.metadata.TemplateClassFile;
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
            "/templates/controller.java",
            ".java"
    );

    private TemplateFile service = new TemplateFile(
            TemplateFileEnum.SERVICE.getKey(),
            "%sService",
            "service",
            "/templates/service.java",
            ".java"
    );

    private TemplateFile serviceImpl = new TemplateFile(
            TemplateFileEnum.SERVICE_IMPL.getKey(),
            "%sServiceImpl",
            "service.impl",
            "/templates/serviceImpl.java",
            ".java"
    );

    private TemplateFile mapper = new TemplateFile(
            TemplateFileEnum.MAPPER.getKey(),
            "%sMapper",
            "mapper",
            "/templates/mapper.java",
            ".java"
    );

    private TemplateFile mapperXml = new TemplateFile(
            TemplateFileEnum.MAPPER_XML.getKey(),
            "%sMapper",
            "xml",
            "/templates/mapper.xml",
            ".xml"
    );

    private TemplateFile entity = new TemplateFile(
            TemplateFileEnum.ENTITY.getKey(),
            "%s",
            "model.entity",
            "/templates/entity.java",
            ".java"
    );


    private TemplateFile queryParam = new TemplateFile(
            TemplateFileEnum.QUERY_PARAM.getKey(),
            "%sQueryDTO",
            "model.query",
            "/templates/queryParam.java",
            ".java"
    );


    private TemplateFile queryResult = new TemplateFile(
            TemplateFileEnum.QUERY_RESULT.getKey(),
            "%sVO",
            "model.vo",
            "/templates/queryResult.java",
            ".java"
    );

    private TemplateFile createParam = new TemplateFile(
            TemplateFileEnum.CREATE_PARAM.getKey(),
            "%sCreateDTO",
            "model.command",
            "/templates/createParam.java",
            ".java"
    );

    private TemplateFile updateParam = new TemplateFile(
            TemplateFileEnum.UPDATE_PARAM.getKey(),
            "%sUpdateDTO",
            "model.command",
            "/templates/updateParam.java",
            ".java"
    );

    private TemplateFile excelExportParam = new TemplateFile(
            TemplateFileEnum.EXCEL_EXPORT_PARAM.getKey(),
            "%sExcelExportDTO",
            "model.excel",
            "/templates/excelExportParam.java",
            ".java"
    );

    private TemplateFile excelImportParam = new TemplateFile(
            TemplateFileEnum.EXCEL_IMPORT_PARAM.getKey(),
            "%sExcelImportDTO",
            "model.excel",
            "/templates/excelImportParam.java",
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

        templateFiles.add(queryParam);
        templateFiles.add(queryResult);

        templateFiles.add(createParam);
        templateFiles.add(updateParam);

        templateFiles.add(excelExportParam);
        templateFiles.add(excelImportParam);
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
