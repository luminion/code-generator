package io.github.luminion.generator.config;

import io.github.luminion.generator.enums.TemplateEnum;
import io.github.luminion.generator.internal.template.TemplateFileResolver;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
import io.github.luminion.generator.metadata.TemplateFile;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class TemplateConfig {
    private final Configurer configurer;
    private final TemplateFileResolver templateFileResolver = new TemplateFileResolver(this);

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
            TemplateEnum.CONTROLLER.getKey(),
            "%sController",
            "controller",
            "/templates/controller.java",
            ".java"
    );

    private TemplateFile service = new TemplateFile(
            TemplateEnum.SERVICE.getKey(),
            "%sService",
            "service",
            "/templates/service.java",
            ".java"
    );

    private TemplateFile serviceImpl = new TemplateFile(
            TemplateEnum.SERVICE_IMPL.getKey(),
            "%sServiceImpl",
            "service.impl",
            "/templates/serviceImpl.java",
            ".java"
    );

    private TemplateFile mapper = new TemplateFile(
            TemplateEnum.MAPPER.getKey(),
            "%sMapper",
            "mapper",
            "/templates/mapper.java",
            ".java"
    );

    private TemplateFile mapperXml = new TemplateFile(
            TemplateEnum.MAPPER_XML.getKey(),
            "%sMapper",
            "xml",
            "/templates/mapper.xml",
            ".xml"
    );

    private TemplateFile entity = new TemplateFile(
            TemplateEnum.ENTITY.getKey(),
            "%s",
            "model.entity",
            "/templates/entity.java",
            ".java"
    );

    private TemplateFile queryParam = new TemplateFile(
            TemplateEnum.QUERY_PARAM.getKey(),
            "%sQueryDTO",
            "model.query",
            "/templates/queryParam.java",
            ".java"
    );

    private TemplateFile queryResult = new TemplateFile(
            TemplateEnum.QUERY_RESULT.getKey(),
            "%sVO",
            "model.vo",
            "/templates/queryResult.java",
            ".java"
    );

    private TemplateFile createParam = new TemplateFile(
            TemplateEnum.CREATE_PARAM.getKey(),
            "%sCreateDTO",
            "model.command",
            "/templates/createParam.java",
            ".java"
    );

    private TemplateFile updateParam = new TemplateFile(
            TemplateEnum.UPDATE_PARAM.getKey(),
            "%sUpdateDTO",
            "model.command",
            "/templates/updateParam.java",
            ".java"
    );

    private TemplateFile excelExportParam = new TemplateFile(
            TemplateEnum.EXCEL_EXPORT_PARAM.getKey(),
            "%sExcelExportDTO",
            "model.excel",
            "/templates/excelExportParam.java",
            ".java"
    );

    private TemplateFile excelImportParam = new TemplateFile(
            TemplateEnum.EXCEL_IMPORT_PARAM.getKey(),
            "%sExcelImportDTO",
            "model.excel",
            "/templates/excelImportParam.java",
            ".java"
    );

    private final List<TemplateFile> templateFiles = new ArrayList<>();

    {
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
        templateFiles.forEach(templateFile -> templateFile.setFileOverride(fileOverride));
    }

    public void addTemplateFile(TemplateFile templateFile) {
        addTemplateFile(Collections.singletonList(templateFile));
    }

    public void addTemplateFile(List<TemplateFile> templateFiles) {
        for (TemplateFile templateFile : templateFiles) {
            registerTemplateFile(templateFile);
        }
    }

    private void registerTemplateFile(TemplateFile templateFile) {
        if (StringUtils.isBlank(templateFile.getKey())) {
            throw new IllegalArgumentException("Template key cannot be empty");
        }
        boolean duplicated = this.templateFiles.stream().anyMatch(exist -> exist.getKey().equals(templateFile.getKey()));
        if (duplicated) {
            throw new IllegalArgumentException("Template key already exists: " + templateFile.getKey());
        }
        templateFile.validate();
        if (fileOverride) {
            templateFile.setFileOverride(true);
        }
        this.templateFiles.add(templateFile);
    }

    public Map<String, TemplateClassFile> resolveTemplateFileMap(TableInfo tableInfo) {
        return templateFileResolver.resolve(tableInfo, templateFiles);
    }
}
