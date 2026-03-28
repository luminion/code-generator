package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateEnum;
import io.github.luminion.generator.internal.render.ImportPackageSupport;
import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.metadata.InvokeInfo;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ControllerConfig implements TemplateRender {
    private final Configurer configurer;

    private String controllerSuperClass;

    @RenderField
    private boolean restController = true;

    private boolean hyphenStyle = true;

    private String baseUrl;

    @RenderField
    private boolean crossOrigin;

    @RenderField
    private boolean restful;

    private boolean pathVariable = true;

    private boolean requestBody = true;

    private boolean queryViaPost;

    @RenderField
    private boolean queryViaSqlContext = false;

    @RenderField
    private InvokeInfo returnType = new InvokeInfo("", "%s", "%s");

    @RenderField
    private InvokeInfo pageType = new InvokeInfo(RuntimeClass.MYBATIS_PLUS_I_PAGE.getCanonicalName(), "IPage<%s>", "%s");

    @Override
    public Map<String, Object> renderData(RenderContext context) {
        TableInfo tableInfo = context.getTableInfo();
        Map<String, Object> data = TemplateRender.super.renderData(context);

        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        Map<String, TemplateClassFile> templateFileMap = context.getTemplateFiles();

        data.put("controllerSuperClass", ClassUtils.getSimpleName(controllerSuperClass));
        String entityVariableName = tableInfo.getEntityVariableName();
        String entityUrl = hyphenStyle ? StringUtils.camelToHyphen(entityVariableName) : entityVariableName;
        String requestBaseUrl = Stream.of(baseUrl, templateConfig.getParentModule(), entityUrl)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("/"));
        if (!requestBaseUrl.startsWith("/")) {
            requestBaseUrl = "/" + requestBaseUrl;
        }
        data.put("requestBaseUrl", requestBaseUrl);
        data.put("restful", restful);

        TemplateClassFile baseService = templateFileMap.get(TemplateEnum.SERVICE.getKey());
        if (!baseService.isGenerate()) {
            baseService = templateFileMap.get(TemplateEnum.SERVICE_IMPL.getKey());
        }
        data.put("baseService", baseService.getSimpleClassName());
        data.put("queryViaPost", queryViaPost);
        if (pathVariable) {
            data.put("idPathParam", "/{id}");
            data.put("idMethodParam", "@PathVariable(\"id\") ");
        }

        data.put("validStr", commandConfig.isValid() ? "@Validated " : null);
        String requestBodyStr = requestBody ? "@RequestBody " : null;
        data.put("requiredBodyStr", requestBodyStr);
        Optional.ofNullable(tableInfo.getPrimaryKeyField())
                .ifPresent(field -> data.put("primaryKeyPropertyType", field.getPropertyType()));

        data.put("queryBodyStr", queryViaPost ? requestBodyStr : null);
        data.put("queryRequestMapping", queryViaPost ? "@PostMapping" : "@GetMapping");
        data.putAll(resolveControllerImports(context));
        return data;
    }

    private Map<String, Object> resolveControllerImports(RenderContext context) {
        TableInfo tableInfo = context.getTableInfo();
        Map<String, TemplateClassFile> templateFileMap = context.getTemplateFiles();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TableField idField = tableInfo.getPrimaryKeyField();
        String idFieldPropertyPkg = idField != null ? idField.getJavaTypeCanonicalName() : null;

        Set<String> importPackages = new TreeSet<>();
        importPackages.add(RuntimeClass.SPRING_BOOT_WEB_ANNOTATION_S.getCanonicalName());
        if (!restController) {
            importPackages.add(RuntimeClass.SPRING_BOOT_CONTROLLER.getCanonicalName());
        }
        if (globalConfig.isLombok()) {
            importPackages.add(RuntimeClass.LOMBOK_REQUIRED_ARGS_CONSTRUCTOR.getCanonicalName());
        }
        switch (globalConfig.getDocType()) {
            case SPRING_DOC:
                importPackages.add(RuntimeClass.SWAGGER_V3_TAG.getCanonicalName());
                importPackages.add(RuntimeClass.SWAGGER_V3_OPERATION.getCanonicalName());
                break;
            case SWAGGER:
                importPackages.add(RuntimeClass.SWAGGER_V2_API.getCanonicalName());
                importPackages.add(RuntimeClass.SWAGGER_V2_API_OPERATION.getCanonicalName());
                break;
            default:
                break;
        }
        ImportPackageSupport.addIfPresent(importPackages, controllerSuperClass);

        TemplateClassFile baseService = templateFileMap.get(TemplateEnum.SERVICE.getKey());
        TemplateClassFile baseServiceImpl = templateFileMap.get(TemplateEnum.SERVICE_IMPL.getKey());
        importPackages.add(baseService.isGenerate() ? baseService.getFullyQualifiedClassName() : baseServiceImpl.getFullyQualifiedClassName());

        if (returnType != null) {
            ImportPackageSupport.addIfPresent(importPackages, returnType.getFullyQualifiedClassName());
        }
        if (pageType != null) {
            ImportPackageSupport.addIfPresent(importPackages, pageType.getFullyQualifiedClassName());
        }

        if (globalConfig.isGenerateCreate()) {
            importPackages.add(templateFileMap.get(TemplateEnum.CREATE_PARAM.getKey()).getFullyQualifiedClassName());
            importPackages.add(RuntimeClass.SPRING_BOOT_VALIDATED.getCanonicalName());
            ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
        }
        if (globalConfig.isGenerateUpdate() && idField != null) {
            importPackages.add(templateFileMap.get(TemplateEnum.UPDATE_PARAM.getKey()).getFullyQualifiedClassName());
            importPackages.add(RuntimeClass.SPRING_BOOT_VALIDATED.getCanonicalName());
        }
        if (globalConfig.isGenerateDelete() && idField != null) {
            ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
        }
        if (globalConfig.isGenerateQueryById() && idField != null) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
            ImportPackageSupport.addIfPresent(importPackages, idFieldPropertyPkg);
        }
        if (globalConfig.isGenerateQueryList()) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
        }
        if (globalConfig.isGenerateQueryPage()) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getFullyQualifiedClassName());
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            if (pageType != null) {
                ImportPackageSupport.addIfPresent(importPackages, pageType.getFullyQualifiedClassName());
            }
        }

        String responseClass = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_SERVLET_RESPONSE.getCanonicalName();
        if (globalConfig.isGenerateExcelImport()) {
            importPackages.add(responseClass);
            importPackages.add(RuntimeClass.JAVA_IO_IOEXCEPTION.getCanonicalName());
            importPackages.add(RuntimeClass.SPRING_BOOT_MULTIPART_FILE.getCanonicalName());
        }
        if (globalConfig.isGenerateExcelExport()) {
            importPackages.add(responseClass);
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getFullyQualifiedClassName());
            importPackages.add(RuntimeClass.JAVA_IO_IOEXCEPTION.getCanonicalName());
            importPackages.add(RuntimeClass.SPRING_BOOT_MULTIPART_FILE.getCanonicalName());
        }

        return ImportPackageSupport.splitImportPackages(importPackages, "controllerFramePkg", "controllerJavaPkg");
    }
}