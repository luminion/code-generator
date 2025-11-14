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
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.*;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
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
public class Controller implements TemplateRender {

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(TemplateFileEnum.CONTROLLER.name(), "%sController", "controller", "/templates/base/controller.java", ".java");

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


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);

        Configurer configurer = tableInfo.getConfigurer();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Resolver resolver = configurer.getResolver();
        TreeSet<String> importPackages = new TreeSet<>();

        data.put("controllerMappingHyphenStyle", this.hyphenStyle);
        data.put("restControllerStyle", this.restController);
        data.put("superControllerClassPackage", StringUtils.isBlank(superClass) ? null : superClass);
        data.put("superControllerClass", ClassUtils.getSimpleName(this.superClass));


        // 首字母小写
        String entityName = tableInfo.getEntityName();
        String entityPath = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
        // 实体类对应请求路径
        String url = this.hyphenStyle ? StringUtils.camelToHyphen(entityPath) : entityPath;
        // 完整请求路径
        String requestBaseUrl = Stream.of(this.baseUrl, globalConfig.getParentPackageModule(), url).filter(StringUtils::isNotBlank).collect(Collectors.joining("/"));
        if (!requestBaseUrl.startsWith("/")) {
            requestBaseUrl = "/" + requestBaseUrl;
        }
        data.put("requestBaseUrl", requestBaseUrl);
        data.put("crossOrigin", this.crossOrigin);
        data.put("restful", this.restful);
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
        // body 参数和校验
        String requestBodyStr = "@RequestBody ";
        String validatedStr = "@Validated ";
        if (requestBody) {
            data.put("requestBodyStr", requestBodyStr);
            if (batchQueryPost) {
                data.put("optionalBodyStr", requestBodyStr);
            }
        }
        if (superClass != null) {
            importPackages.add(superClass);
        }
        importPackages.add("org.springframework.web.bind.annotation.*");
        if (!restController) {
            importPackages.add("org.springframework.stereotype.Controller");
        }
        if (globalConfig.isLombok()) {
            importPackages.add("lombok.RequiredArgsConstructor");
        }
        // doc包
        switch (globalConfig.getDocType()) {
            case SPRING_DOC:
                importPackages.add("io.swagger.v3.oas.annotations.tags.Tag");
                importPackages.add("io.swagger.v3.oas.annotations.Operation");
                importPackages.add("io.swagger.v3.oas.annotations.Parameter");
                importPackages.add("io.swagger.v3.oas.annotations.Parameters");
                break;
            case SWAGGER:
                importPackages.add("io.swagger.annotations.Api");
                importPackages.add("io.swagger.annotations.ApiOperation");
                importPackages.add("io.swagger.annotations.ApiParam");
                break;
        }
        // 返回类包
        if (returnMethod.isClassReady()) {
            importPackages.add(returnMethod.getClassName());
        }

        if (resolver.isGenerate(TemplateFileEnum.SERVICE, tableInfo)) {
            data.put("baseService", resolver.getClassSimpleName(TemplateFileEnum.SERVICE, tableInfo));
            importPackages.add(resolver.getClassName(TemplateFileEnum.SERVICE, tableInfo));
        } else {
            data.put("baseService", resolver.getClassSimpleName(TemplateFileEnum.SERVICE_IMPL, tableInfo));
            importPackages.add(resolver.getClassName(TemplateFileEnum.SERVICE_IMPL, tableInfo));
        }

        data.put("primaryKeyPropertyType", "Object");
        TableField primaryKeyTableField = tableInfo.getPrimaryKeyTableField();
        if (primaryKeyTableField != null) {
            data.put("primaryKeyPropertyType", primaryKeyTableField.getJavaType().getType());
            importPackages.add(primaryKeyTableField.getJavaType().getPkg());
        }
        if (globalConfig.isValidated()) {
            data.put("validatedStr", validatedStr);
            importPackages.add("org.springframework.validation.annotation.Validated");
        }
        if (globalConfig.isGenerateInsert()) {
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_INSERT_DTO, tableInfo));
        }
        if (globalConfig.isGenerateUpdate()) {
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_UPDATE_DTO, tableInfo));
        }
        if (globalConfig.isGenerateQuery()) {
            importPackages.add("java.util.List");
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_DTO, tableInfo));
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_VO, tableInfo));
            if (pageMethod != null && pageMethod.isClassReady()) {
                importPackages.add(pageMethod.getClassName());
                data.put("pageReturnType", pageMethod.returnGenericTypeStr(resolver.getClassSimpleName(TemplateFileEnum.ENTITY_VO, tableInfo)));
            } else {
                ClassPayload pageClassPayload = globalConfig.getPageClassPayload();
                importPackages.add(pageClassPayload.getClassName());
                data.put("pageReturnType", pageClassPayload.returnGenericTypeStr(resolver.getClassSimpleName(TemplateFileEnum.ENTITY_VO, tableInfo)));
            }
        }
        String responseClass = globalConfig.getJavaEEApi().packagePrefix + ".servlet.http.HttpServletResponse";
        if (globalConfig.isGenerateImport()) {
            importPackages.add("org.springframework.web.multipart.MultipartFile");
            importPackages.add("java.io.IOException");
            importPackages.add(responseClass);
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_EXCEL_IMPORT_DTO, tableInfo));
        }
        if (globalConfig.isGenerateExport()) {
            importPackages.add("java.io.IOException");
            importPackages.add(responseClass);
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_EXCEL_EXPORT_DTO, tableInfo));
        }
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("controllerImportPackages4Java", javaPackages);
        data.put("controllerImportPackages4Framework", frameworkPackages);

        return data;
    }

}