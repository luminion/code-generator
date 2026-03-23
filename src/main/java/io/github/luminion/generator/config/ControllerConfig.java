package io.github.luminion.generator.config;

import io.github.luminion.generator.annotation.RenderField;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateEnum;
import io.github.luminion.generator.metadata.InvokeInfo;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ControllerConfig implements TemplateRender {
    private final Configurer configurer;

    /**
     * 自定义继承的Controller类全称，带包名
     */
    private String controllerSuperClass;

    /**
     * 生成 @RestController控制器
     */
    @RenderField
    private boolean restController = true;

    /**
     * 驼峰转连字符(managerUserActionHistory -> manager-user-action-history)
     */
    private boolean hyphenStyle = true;

    /**
     * 请求基础url
     */
    private String baseUrl;

    /**
     * 跨域注解
     */
    @RenderField
    private boolean crossOrigin;

    /**
     * restful样式
     */
    @RenderField
    private boolean restful;

    /**
     * 请求路径参数
     */
    private boolean pathVariable = true;

    /**
     * controller是否使用@RequestBody注解
     */
    private boolean requestBody = true;

    /**
     * 通过POST方式查询
     */
    private boolean queryViaPost;

    /**
     * 通过SqlContext查询
     * // todo
     */
    @RenderField
    private boolean queryViaSqlContext = false;

    /**
     * 返回结果类型
     */
    @RenderField
    private InvokeInfo returnType = new InvokeInfo("", "%s", "%s");

    /**
     * 分页结果类型
     */
    @RenderField
    private InvokeInfo pageType = new InvokeInfo(RuntimeClass.MYBATIS_PLUS_I_PAGE.getCanonicalName(), "IPage<%s>", "%s");


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);

        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();

        data.put("controllerSuperClass", ClassUtils.getSimpleName(controllerSuperClass));
        // 首字母小写
        String entityName = tableInfo.getEntityName();
        String entityPath = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
        String entityUrl = hyphenStyle ? StringUtils.camelToHyphen(entityPath) : entityPath;
        String parentModule = templateConfig.getParentModule();
        String requestBaseUrl = Stream.of(baseUrl, parentModule, entityUrl)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("/"));
        if (!requestBaseUrl.startsWith("/")) {
            requestBaseUrl = "/" + requestBaseUrl;
        }
        data.put("requestBaseUrl", requestBaseUrl);
        data.put("restful", restful);
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TemplateClassFile baseService = templateFileMap.get(TemplateEnum.SERVICE.getKey());
        if (!baseService.isGenerate()) {
            baseService = templateFileMap.get(TemplateEnum.SERVICE_IMPL.getKey());
        }

        data.put("baseService", baseService.getClassSimpleName());
        data.put("queryViaPost", queryViaPost);
        // 路径参数
        if (pathVariable) {
            data.put("idPathParam", "/{id}");
            data.put("idMethodParam", "@PathVariable(\"id\") ");
        }

        data.put("validStr", commandConfig.isValid() ? "@Validated " : null);
        String requestBodyStr = requestBody ? "@RequestBody " : null;
        data.put("requiredBodyStr", requestBodyStr);
        Optional.ofNullable(tableInfo.getIdField())
                .ifPresent(e -> data.put("primaryKeyPropertyType", e.getPropertyType()));


        data.put("queryBodyStr", queryViaPost ? requestBodyStr : null);
        data.put("queryRequestMapping", queryViaPost ? "@PostMapping" : "@GetMapping");


        // 导包
        data.putAll(resolveControllerImports(tableInfo));
        return data;
    }

    private Map<String, Object> resolveControllerImports(TableInfo tableInfo) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField idField = tableInfo.getIdField();
        String idFieldPropertyPkg = idField != null ? idField.getPropertyPkg() : null;

        Set<String> importPackages = new TreeSet<>();
        // spring-web包
        importPackages.add(RuntimeClass.SPRING_BOOT_WEB_ANNOTATION_S.getCanonicalName());
        if (!restController) {
            importPackages.add(RuntimeClass.SPRING_BOOT_CONTROLLER.getCanonicalName());
        }
        // lombok
        if (globalConfig.isLombok()) {
            importPackages.add(RuntimeClass.LOMBOK_REQUIRED_ARGS_CONSTRUCTOR.getCanonicalName());
        }
        // 文档
        switch (globalConfig.getDocType()) {
            case SPRING_DOC:
                importPackages.add(RuntimeClass.SWAGGER_V3_TAG.getCanonicalName());
                importPackages.add(RuntimeClass.SWAGGER_V3_OPERATION.getCanonicalName());
                break;
            case SWAGGER:
                importPackages.add(RuntimeClass.SWAGGER_V2_API.getCanonicalName());
                importPackages.add(RuntimeClass.SWAGGER_V2_API_OPERATION.getCanonicalName());
                break;
        }
        // 父类
        if (controllerSuperClass != null) {
            importPackages.add(controllerSuperClass);
        }
        // baseService
        TemplateClassFile baseService = templateFileMap.get(TemplateEnum.SERVICE.getKey());
        TemplateClassFile baseServiceImpl = templateFileMap.get(TemplateEnum.SERVICE_IMPL.getKey());
        if (baseService.isGenerate()) {
            importPackages.add(baseService.getClassCanonicalName());
        } else {
            importPackages.add(baseServiceImpl.getClassCanonicalName());
        }
        // 返回结果类型
        if (returnType != null && returnType.getClassCanonicalName() != null) {
            importPackages.add(returnType.getClassCanonicalName());
        }
        // 分页结果类型
        if (pageType != null && pageType.getClassCanonicalName() != null) {
            importPackages.add(pageType.getClassCanonicalName());
        }

        // 增
        if (globalConfig.isGenerateCreate()) {
            importPackages.add(templateFileMap.get(TemplateEnum.CREATE_PARAM.getKey()).getClassCanonicalName());
            importPackages.add(RuntimeClass.SPRING_BOOT_VALIDATED.getCanonicalName());
            if (idFieldPropertyPkg != null) {
                importPackages.add(idField.getPropertyPkg());
            }
        }
        // 改
        if (globalConfig.isGenerateUpdate() && idField != null) {
            importPackages.add(templateFileMap.get(TemplateEnum.UPDATE_PARAM.getKey()).getClassCanonicalName());
            importPackages.add(RuntimeClass.SPRING_BOOT_VALIDATED.getCanonicalName());
        }

        // 删
        if (globalConfig.isGenerateDelete() && idField != null) {
            if (idFieldPropertyPkg != null) {
                importPackages.add(idField.getPropertyPkg());
            }
        }

        // 通过id查询
        if (globalConfig.isGenerateQueryById() && idField != null) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getClassCanonicalName());
            if (idFieldPropertyPkg != null) {
                importPackages.add(idField.getPropertyPkg());
            }
        }

        // 列表
        if (globalConfig.isGenerateQueryList()) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getClassCanonicalName());
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getClassCanonicalName());
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
        }

        // 分页
        if (globalConfig.isGenerateQueryPage()) {
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getClassCanonicalName());
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()).getClassCanonicalName());
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getCanonicalName());
            if (pageType != null) {
                importPackages.add(pageType.getClassCanonicalName());
            }
        }

        String responseClass = globalConfig.getJavaEEApi().getPackagePrefix()
                + RuntimeClass.PREFIX_JAKARTA_SERVLET_RESPONSE.getCanonicalName();

        // 导入
        if (globalConfig.isGenerateExcelImport()) {
            // 导入需要下载导入模板, 导入response
            importPackages.add(responseClass);
            importPackages.add(RuntimeClass.JAVA_IO_IOEXCEPTION.getCanonicalName());
            importPackages.add(RuntimeClass.SPRING_BOOT_MULTIPART_FILE.getCanonicalName());
        }

        // 导出
        if (globalConfig.isGenerateExcelExport()) {
            importPackages.add(responseClass);
            importPackages.add(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()).getClassCanonicalName());
            importPackages.add(RuntimeClass.JAVA_IO_IOEXCEPTION.getCanonicalName());
            importPackages.add(RuntimeClass.SPRING_BOOT_MULTIPART_FILE.getCanonicalName());
        }

        // 导包数据
        Map<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("controllerFramePkg", frameworkPackages);
        data.put("controllerJavaPkg", javaPackages);
        return data;
    }
}
