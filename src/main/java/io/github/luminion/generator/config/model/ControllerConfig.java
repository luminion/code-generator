package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.TemplateModelRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.ConfigResolver;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 控制器属性配置
 *
 * @author nieqiurong 2020/10/11.
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
@Data
public class ControllerConfig implements TemplateModelRender {

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.CONTROLLER.getKey(), 
            "%sController", 
            "controller", 
            "/templates/model/controller.java", 
            ".java"
    );

    /**
     * 自定义继承的Controller类全称，带包名
     */
    protected String superClass;

    /**
     * 生成 @RestController控制器
     */
    protected boolean restController = true;

    /**
     * 驼峰转连字符(managerUserActionHistory -> manager-user-action-history)
     */
    protected boolean hyphenStyle = true;

    /**
     * 请求基础url
     */
    protected String baseUrl;

    /**
     * 跨域注解
     */
    protected boolean crossOrigin;

    /**
     * restful样式
     */
    protected boolean restful;

    /**
     * 请求路径参数
     */
    protected boolean pathVariable = true;

    /**
     * controller是否使用@RequestBody注解
     */
    protected boolean requestBody = true;

    /**
     * 批量查询使用post请求
     */
    protected boolean batchQueryPost = true;

    /**
     * 追加SqlContext查询
     */
    private boolean sqlContextQuery = true;

    /**
     * 返回结果方法
     */
    protected ClassMethodPayload returnMethod = new ClassMethodPayload();

    /**
     * 分页返回方法
     */
    protected ClassMethodPayload pageMethod = new ClassMethodPayload();

    @Override
    public TemplateFile renderTemplateFile() {
        return templateFile;
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        Set<String> importPackages = new TreeSet<>();

        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        data.put("crossOrigin", this.crossOrigin);
        data.put("restController", this.restController);
        data.put("controllerSuperClass", ClassUtils.getSimpleName(this.superClass));
        data.put("sqlContextQuery", this.sqlContextQuery);
        // 首字母小写
        String entityName = tableInfo.getEntityName();
        String entityPath = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
        String url = this.hyphenStyle ? StringUtils.camelToHyphen(entityPath) : entityPath;
        String requestBaseUrl = Stream.of(this.baseUrl, globalConfig.getParentPackageModule(), url).filter(StringUtils::isNotBlank).collect(Collectors.joining("/"));
        if (!requestBaseUrl.startsWith("/")) {
            requestBaseUrl = "/" + requestBaseUrl;
        }
        data.put("requestBaseUrl", requestBaseUrl);
        data.put("restful", this.restful);
        boolean isGenerateService = configResolver.isGenerate(TemplateFileEnum.SERVICE, tableInfo);
        data.put("baseService", isGenerateService ? configResolver.getClassSimpleName(TemplateFileEnum.SERVICE, tableInfo) : configResolver.getClassSimpleName(TemplateFileEnum.SERVICE_IMPL, tableInfo));
        data.put("returnMethod", this.returnMethod);
        data.put("pageMethod", this.pageMethod);
        data.put("batchQueryMethod", batchQueryPost ? "@PostMapping" : "@GetMapping");
        // 路径参数
        if (pathVariable) {
            data.put("idPathParams", "/{id}");
            data.put("idMethodParams", "@PathVariable(\"id\") ");
            data.put("pagePathParams", "/{current}/{size}");
            data.put("pageMethodParams", "@PathVariable(\"current\") Long current, @PathVariable(\"size\") Long size");
        } else {
            data.put("pageMethodParams", "Long current, Long size");
        }
        data.put("validatedStr", globalConfig.isValidated() ? "@Validated " : null);
        String requestBodyStr = requestBody ? "@RequestBody " : null;
        data.put("requiredBodyStr", requestBodyStr);
        data.put("optionalBodyStr", batchQueryPost ? requestBodyStr : null);
        Optional.ofNullable(tableInfo.getPrimaryKeyField()).ifPresent(e -> data.put("primaryKeyPropertyType", e.getJavaType().getType()));
        // 分页
        data.put("pageReturnType", pageMethod.returnGenericTypeStr(configResolver.getClassSimpleName(TemplateFileEnum.QUERY_VO, tableInfo)));

        // ======================导包======================
        // spring组件
        importPackages.add(RuntimeClass.SPRING_BOOT_WEB_ANNOTATION_S.getClassName());
        if (!restController) {
            importPackages.add(RuntimeClass.SPRING_BOOT_CONTROLLER.getClassName());
        }
        if (globalConfig.isLombok()) {
            importPackages.add(RuntimeClass.LOMBOK_REQUIRED_ARGS_CONSTRUCTOR.getClassName());
        }

        //// 运行环境
        //switch (globalConfig.getRuntimeEnv()) {
        //    case MY_BATIS_PLUS_SQL_BOOSTER:
        //        importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_BUILDER.getClassName());
        //        importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
        //        break;
        //    case MYBATIS_PLUS:
        //        break;
        //}

        // 文档类型
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
        // 类信息
        if (superClass != null) {
            importPackages.add(this.superClass);
        }
        if (isGenerateService) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.SERVICE, tableInfo));
        } else {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.SERVICE_IMPL, tableInfo));
        }
        // 返回类包
        if (returnMethod.isClassReady()) {
            importPackages.add(returnMethod.getClassName());
        }
        // 参数校验
        if (globalConfig.isValidated()) {
            importPackages.add(RuntimeClass.SPRING_BOOT_VALIDATED.getClassName());
        }
        // 新增
        if (globalConfig.isGenerateCreate()) {
            importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
            importPackages.add(configResolver.getClassName(TemplateFileEnum.CREATE_DTO, tableInfo));
        }
        // 修改
        if (globalConfig.isGenerateUpdate()) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.UPDATE_DTO, tableInfo));
        }
        // 删除
        if (globalConfig.isGenerateDelete()) {
            if (tableInfo.isHavePrimaryKey()) {
                TableField primaryKeyTableField = tableInfo.getPrimaryKeyField();
                Optional.ofNullable(primaryKeyTableField.getJavaType().getPkg()).ifPresent(importPackages::add);
            }
        }
        // voById
        if (globalConfig.isGenerateVoById()) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
            // 根据id查询
            if (tableInfo.isHavePrimaryKey()) {
                TableField primaryKeyTableField = tableInfo.getPrimaryKeyField();
                Optional.ofNullable(primaryKeyTableField.getJavaType().getPkg()).ifPresent(importPackages::add);
            } else {
                log.warn("table [{}] does not have a primary key, won't generate voById", tableInfo.getName());
            }
        }
        // voList
        if (globalConfig.isGenerateVoList()) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
            importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
            if (RuntimeEnv.MY_BATIS_PLUS_SQL_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
                importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_BUILDER.getClassName());
                importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
            }
        }
        // voPage
        if (globalConfig.isGenerateVoPage()) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
            importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
            if (pageMethod != null && pageMethod.isClassReady()) {
                importPackages.add(pageMethod.getClassName());
            }
            if (RuntimeEnv.MY_BATIS_PLUS_SQL_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
                importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_BUILDER.getClassName());
                importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
            }
        }

        String responseClass = globalConfig.getJavaEEApi().getPackagePrefix() + RuntimeClass.PREFIX_JAKARTA_SERVLET_RESPONSE.getClassName();

        // 导入
        if (globalConfig.isGenerateImport()) {
            // 导入需要下载导入模板, 导入response
            importPackages.add(responseClass);
            importPackages.add(RuntimeClass.JAVA_IO_IOEXCEPTION.getClassName());
            importPackages.add(RuntimeClass.SPRING_BOOT_MULTIPART_FILE.getClassName());
        }

        // 导出
        if (globalConfig.isGenerateExport()) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
            importPackages.add(RuntimeClass.JAVA_IO_IOEXCEPTION.getClassName());
            importPackages.add(responseClass);
            if (RuntimeEnv.MY_BATIS_PLUS_SQL_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
                importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_BUILDER.getClassName());
                importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
            }
        }

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