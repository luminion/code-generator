package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.*;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.util.*;
import java.util.function.Function;
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
    private boolean crossOrigin;

    /**
     * restful样式
     */
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
     * 通过POST方式查询(@RequestBody)
     */
    private boolean queryViaPost = true;

    /**
     * 通过SqlContext查询(@RequestBody)
     */
    private boolean queryViaSqlContext = false;

    /**
     * 返回结果类型
     */
    private String returnTypeClass;
    /**
     * 返回结果类型格式化
     */
    private Function<String, String> returnTypeFormat = (s -> s);
    /**
     * 返回结果方法格式化
     */
    private Function<String, String> returnMethodFormat = (s -> s);

    /**
     * 分页结果类型
     */
    private String pageTypeClass;
    /**
     * 分页结果类型格式化
     */
    private Function<String, String> pageTypeFormat = (s -> s);
    /**
     * 分页结果方法格式化
     */
    private Function<String, String> pageMethodFormat = (s -> s);


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();

        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();


        data.put("crossOrigin", crossOrigin);
        data.put("restController", restController);
        data.put("controllerSuperClass", ClassUtils.getSimpleName(controllerSuperClass));
        data.put("queryWithSqlContext", queryViaSqlContext);
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
        TemplateClassFile baseService = templateFileMap.get(TemplateFileEnum.SERVICE.getKey());
        if (!baseService.isEnabled()) {
            baseService = templateFileMap.get(TemplateFileEnum.SERVICE_IMPL.getKey());
        }

        data.put("baseService", baseService.getClassSimpleName());
        data.put("queryViaPost", queryViaPost);
        // 路径参数
        if (pathVariable) {
            data.put("idPathParam", "/{id}");
            data.put("idMethodParam", "@PathVariable(\"id\") ");
        }

        data.put("validatedStr", commandConfig.isValid() ? "@Validated " : null);
        String requestBodyStr = requestBody ? "@RequestBody " : null;
        data.put("requiredBodyStr", requestBodyStr);
        Optional.ofNullable(tableInfo.getPrimaryKeyField())
                .ifPresent(e -> data.put("primaryKeyPropertyType", e.getPropertyType()));


        data.put("queryBodyStr", queryViaPost ? requestBodyStr : null);
        data.put("queryRequestMapping", queryViaPost ? "@PostMapping" : "@GetMapping");
        // 分页
        if (queryConfig.getQueryDtoSuperClass() == null) {
            data.put("pageMethodParams", ", Long current, Long size");
        }

        // 导包
        data.putAll(resolveControllerImports(tableInfo));
        return data;
    }

    private Map<String, Object> resolveControllerImports(TableInfo tableInfo) {
        StrategyConfig strategyConfig = configurer.getStrategyConfig();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        TemplateConfig templateConfig = configurer.getTemplateConfig();
        CommandConfig commandConfig = configurer.getCommandConfig();
        QueryConfig queryConfig = configurer.getQueryConfig();
        ExcelConfig excelConfig = configurer.getExcelConfig();
        Map<String, TemplateClassFile> templateFileMap = templateConfig.resolveTemplateFileMap(tableInfo);
        TableField primaryKeyField = tableInfo.getPrimaryKeyField();
        String primaryKeyFieldPropertyPkg = primaryKeyField != null ? primaryKeyField.getPropertyPkg() : null;
    
        Set<String> importPackages = new TreeSet<>();
        // spring-web包
        importPackages.add(RuntimeClass.SPRING_BOOT_WEB_ANNOTATION_S.getClassName());
        if (!restController) {
            importPackages.add(RuntimeClass.SPRING_BOOT_CONTROLLER.getClassName());
        }
        // lombok
        if (strategyConfig.isLombok()) {
            importPackages.add(RuntimeClass.LOMBOK_REQUIRED_ARGS_CONSTRUCTOR.getClassName());
        }
        // 文档
        switch (globalConfig.getDocType()) {
            case SWAGGER_V3:
                importPackages.add(RuntimeClass.SWAGGER_V3_TAG.getClassName());
                importPackages.add(RuntimeClass.SWAGGER_V3_OPERATION.getClassName());
                break;
            case SWAGGER_V2:
                importPackages.add(RuntimeClass.SWAGGER_V2_API.getClassName());
                importPackages.add(RuntimeClass.SWAGGER_V2_API_OPERATION.getClassName());
                break;
        }
        // 父类
        if (controllerSuperClass != null) {
            importPackages.add(controllerSuperClass);
        }
        // baseService
        TemplateClassFile baseService = templateFileMap.get(TemplateFileEnum.SERVICE.getKey());
        TemplateClassFile baseServiceImpl = templateFileMap.get(TemplateFileEnum.SERVICE_IMPL.getKey());
        if (baseService.isEnabled()) {
            importPackages.add(baseService.getClassCanonicalName());
        } else {
            importPackages.add(baseServiceImpl.getClassCanonicalName());
        }
        // 返回结果类型
        if (returnTypeClass != null) {
            importPackages.add(returnTypeClass);
        }

        // 增
        if (commandConfig.isEnableCreateByDto()) {
            importPackages.add(templateFileMap.get(TemplateFileEnum.CREATE_DTO.getKey()).getClassCanonicalName());
            importPackages.add(RuntimeClass.SPRING_BOOT_VALIDATED.getClassName());
            if (primaryKeyFieldPropertyPkg != null) {
                importPackages.add(primaryKeyField.getPropertyPkg());
            }
        }
        // 改
        if (commandConfig.isEnableUpdateByDto() && primaryKeyField != null) {
            importPackages.add(templateFileMap.get(TemplateFileEnum.UPDATE_DTO.getKey()).getClassCanonicalName());
            importPackages.add(RuntimeClass.SPRING_BOOT_VALIDATED.getClassName());
        }

        // 删
        if (commandConfig.isEnableDeleteById() && primaryKeyField != null) {
            if (primaryKeyFieldPropertyPkg != null) {
                importPackages.add(primaryKeyField.getPropertyPkg());
            }
        }

        // 通过id查询
        if (queryConfig.isEnableSelectVoById() && primaryKeyField != null) {
            importPackages.add(templateFileMap.get(TemplateFileEnum.QUERY_VO.getKey()).getClassCanonicalName());
            if (primaryKeyFieldPropertyPkg != null) {
                importPackages.add(primaryKeyField.getPropertyPkg());
            }
        }

        // 列表
        if (queryConfig.isEnableSelectVoList()) {
            importPackages.add(templateFileMap.get(TemplateFileEnum.QUERY_DTO.getKey()).getClassCanonicalName());
            importPackages.add(templateFileMap.get(TemplateFileEnum.QUERY_VO.getKey()).getClassCanonicalName());
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
        }

        // 分页
        if (queryConfig.isEnableSelectVoPage()) {
            importPackages.add(templateFileMap.get(TemplateFileEnum.QUERY_DTO.getKey()).getClassCanonicalName());
            importPackages.add(templateFileMap.get(TemplateFileEnum.QUERY_VO.getKey()).getClassCanonicalName());
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
            if (pageTypeClass != null) {
                importPackages.add(pageTypeClass);
            }
        }

        String responseClass = globalConfig.getJavaEEApi().getPackagePrefix() 
                + RuntimeClass.PREFIX_JAKARTA_SERVLET_RESPONSE.getClassName();

        // 导入
        if (excelConfig.isEnableExcelImport()) {
            // 导入需要下载导入模板, 导入response
            importPackages.add(responseClass);
            importPackages.add(RuntimeClass.JAVA_IO_IOEXCEPTION.getClassName());
            importPackages.add(RuntimeClass.SPRING_BOOT_MULTIPART_FILE.getClassName());
        }
        
        // 导出
        if (excelConfig.isEnableExcelExport()) {
            importPackages.add(responseClass);
            importPackages.add(templateFileMap.get(TemplateFileEnum.QUERY_DTO.getKey()).getClassCanonicalName());
            importPackages.add(RuntimeClass.JAVA_IO_IOEXCEPTION.getClassName());
            importPackages.add(RuntimeClass.SPRING_BOOT_MULTIPART_FILE.getClassName());
        }

        // 导包数据
        Map<String, Object> data = new HashMap<>();
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("controllerFrameworkPkg", frameworkPackages);
        data.put("controllerJavaPkg", javaPackages);
        return data;
    }
}
