/*
 * Copyright (c) 2011-2025, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.Resolver;
import io.github.luminion.generator.config.core.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.*;
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
public class ControllerConfig implements TemplateRender {

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(TemplateFileEnum.CONTROLLER.getKey(), "%sController", "controller", "/templates/mybatis_plus/controller.java", ".java");

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
     * 返回结果方法
     */
    protected ClassMethodPayload returnMethod = new ClassMethodPayload();

    /**
     * 分页返回方法
     */
    protected ClassMethodPayload pageMethod = new ClassMethodPayload();

    /**
     * 导入的包
     */
    private Set<String> importPackages = new TreeSet<>();


    @Override
    public List<TemplateFile> renderTemplateFiles() {
        return Collections.singletonList(templateFile);
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        
        Resolver resolver = tableInfo.getResolver();
        Configurer<?> configurer = resolver.getConfigurer();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        
        data.put("crossOrigin", this.crossOrigin);
        data.put("restController", this.restController);
        data.put("controllerSuperClass", ClassUtils.getSimpleName(this.superClass));
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
        boolean isGenerateService = resolver.isGenerate(TemplateFileEnum.SERVICE, tableInfo);
        data.put("baseService", isGenerateService ? resolver.getClassSimpleName(TemplateFileEnum.SERVICE, tableInfo) : resolver.getClassSimpleName(TemplateFileEnum.SERVICE_IMPL, tableInfo));
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
        data.put("requestBodyStr", requestBodyStr);
        data.put("optionalBodyStr", batchQueryPost ? requestBodyStr : null);
        Optional.ofNullable(tableInfo.getPrimaryKeyField()).ifPresent(e -> data.put("primaryKeyPropertyType", e.getJavaType().getType()));
        if (pageMethod != null && pageMethod.isClassReady()) {
            data.put("pageReturnType", pageMethod.returnGenericTypeStr(resolver.getClassSimpleName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo)));
        } else {
            ClassPayload pageClassPayload = globalConfig.getPageClassPayload();
            data.put("pageReturnType", pageClassPayload.returnGenericTypeStr(resolver.getClassSimpleName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo)));
        }


        // ======================导包======================
        // spring组件
        importPackages.add("org.springframework.web.bind.annotation.*");
        if (!restController) {
            importPackages.add("org.springframework.stereotype.Controller");
        }
        if (globalConfig.isLombok()) {
            importPackages.add("lombok.RequiredArgsConstructor");
        }
        // 文档类型
        switch (globalConfig.getDocType()) {
            case SPRING_DOC:
                importPackages.add("io.swagger.v3.oas.annotations.tags.Tag");
                importPackages.add("io.swagger.v3.oas.annotations.Operation");
                //importPackages.add("io.swagger.v3.oas.annotations.Parameter");
                //importPackages.add("io.swagger.v3.oas.annotations.Parameters");
                break;
            case SWAGGER:
                importPackages.add("io.swagger.annotations.Api");
                importPackages.add("io.swagger.annotations.ApiOperation");
                //importPackages.add("io.swagger.annotations.ApiParam");
                break;
        }
        // 类信息
        if (superClass != null) {
            importPackages.add(this.superClass);
        }
        if (isGenerateService) {
            importPackages.add(resolver.getClassName(TemplateFileEnum.SERVICE, tableInfo));
        } else {
            importPackages.add(resolver.getClassName(TemplateFileEnum.SERVICE_IMPL, tableInfo));
        }
        // 返回类包
        if (returnMethod.isClassReady()) {
            importPackages.add(returnMethod.getClassName());
        }
        // 参数校验
        if (globalConfig.isValidated()) {
            importPackages.add("org.springframework.validation.annotation.Validated");
        }
        // 新增
        if (globalConfig.isGenerateCreate()) {
            importPackages.add("java.io.Serializable");
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_CREATE_DTO, tableInfo));
        }
        // 修改
        if (globalConfig.isGenerateUpdate()) {
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_UPDATE_DTO, tableInfo));
        }
        // 删除
        if (globalConfig.isGenerateDelete()) {
            if (tableInfo.isHavePrimaryKey()) {
                TableField primaryKeyTableField = tableInfo.getPrimaryKeyField();

                Optional.ofNullable(primaryKeyTableField.getJavaType().getPkg()).ifPresent(importPackages::add);
            } else {
                log.warn("{}表无主键, 不生成根据id删除", tableInfo.getName());
            }
        }

        // 查询
        if (globalConfig.isGenerateQuery()) {
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
            // 根据id查询
            if (tableInfo.isHavePrimaryKey()) {
                TableField primaryKeyTableField = tableInfo.getPrimaryKeyField();
                Optional.ofNullable(primaryKeyTableField.getJavaType().getPkg()).ifPresent(importPackages::add);
            } else {
                log.warn("{}表无主键, 不生成根据id查询", tableInfo.getName());
            }
            importPackages.add("java.util.List");
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_DTO, tableInfo));

            if (RuntimeEnv.MY_BATIS_PLUS_SQL_BOOSTER.equals(globalConfig.getRuntimeEnv())) {
                importPackages.add("io.github.luminion.sqlbooster.model.sql.helper.SqlHelper");
                importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
            }

            if (pageMethod != null && pageMethod.isClassReady()) {
                importPackages.add(pageMethod.getClassName());
                data.put("pageReturnType", pageMethod.returnGenericTypeStr(resolver.getClassSimpleName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo)));
            } else {
                ClassPayload pageClassPayload = globalConfig.getPageClassPayload();
                importPackages.add(pageClassPayload.getClassName());
                data.put("pageReturnType", pageClassPayload.returnGenericTypeStr(resolver.getClassSimpleName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo)));
            }
        }
        String responseClass = globalConfig.getJavaEEApi().getPackagePrefix() + "servlet.http.HttpServletResponse";

        // 导入
        if (globalConfig.isGenerateImport()) {
            // 导入需要下载导入模板, 导入response
            importPackages.add(responseClass);
            importPackages.add("java.io.IOException");
            importPackages.add("org.springframework.web.multipart.MultipartFile");
        }

        // 导出
        if (globalConfig.isGenerateExport()) {
            importPackages.add("java.io.IOException");
            importPackages.add(responseClass);
        }

        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        data.put("controllerFrameworkPkg", frameworkPackages);
        data.put("controllerJavaPkg", javaPackages);


        // 类注解
        String comment = Optional.ofNullable(tableInfo.getComment()).orElse("");
        TreeSet<String> annotations = new TreeSet<>();
        switch (globalConfig.getDocType()) {
            case SPRING_DOC:
                annotations.add(String.format("@Tag(name= \"%s\", description = \"%s\")", comment, comment));
                break;
            case SWAGGER:
                annotations.add(String.format("@Api(tags = \"%s\")", comment));
                break;
        }
        if (crossOrigin) {
            annotations.add("@CrossOrigin");
        }

        if (restController) {
            annotations.add("@RestController");
        } else {
            annotations.add("@Controller");
        }
        annotations.add(String.format("@RequestMapping(\"%s\")", requestBaseUrl));
        if (globalConfig.isLombok()) {
            annotations.add("@RequiredArgsConstructor");
        }


        data.put("controllerAnnotations", annotations);
        return data;
    }

}