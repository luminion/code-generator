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
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service属性配置
 *
 * @author nieqiurong 2020/10/11.
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
@Data
public class ServiceConfig implements TemplateRender {

    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.SERVICE.getKey(),
            "%sService",
            "service",
            "/templates/mybatis_plus/service.java",
            ".java"
    );

    /**
     * 自定义继承的Service类全称，带包名
     */
    protected String superClass;

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        Set<String> importPackages = new TreeSet<>();

        Resolver resolver = tableInfo.getResolver();
        Configurer<?> configurer = resolver.getConfigurer();
        GlobalConfig globalConfig = configurer.getGlobalConfig();


        switch (globalConfig.getRuntimeEnv()) {
            case MY_BATIS_PLUS_SQL_BOOSTER:
                this.superClass = RuntimeClass.SQL_BOOSTER_MP_SERVICE.getClassName();
                importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
                importPackages.add(resolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                if (globalConfig.isGenerateCreate()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.CREATE_DTO, tableInfo));
                }
                if (globalConfig.isGenerateUpdate()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.UPDATE_DTO, tableInfo));
                }
                if (globalConfig.isGenerateDelete()) {
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateVoById()) {
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateVoList()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                }
                if (globalConfig.isGenerateVoPage()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                    importPackages.add(RuntimeClass.SQL_BOOSTER_BOOSTER_PAGE.getClassName());
                }
                if (globalConfig.isGenerateImport()) {
                    importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getClassName());
                    importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
                }
                if (globalConfig.isGenerateExport()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                    importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
                }
                break;
            case MYBATIS_PLUS:
                this.superClass = RuntimeClass.MYBATIS_PLUS_I_SERVICE.getClassName();
                importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
                if (globalConfig.isGenerateCreate()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.CREATE_DTO, tableInfo));
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateUpdate()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.UPDATE_DTO, tableInfo));
                }
                if (globalConfig.isGenerateDelete()) {
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateVoById()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateVoList()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                    importPackages.add(resolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                }
                if (globalConfig.isGenerateVoPage()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                    importPackages.add(resolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                    importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getClassName());
                }
                if (globalConfig.isGenerateImport()) {
                    importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getClassName());
                    importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
                }
                if (globalConfig.isGenerateExport()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                    importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
                }
                break;
            default:
                throw new RuntimeException("Unknown runtime environment:" + globalConfig.getRuntimeEnv());
        }


        data.put("serviceSuperClass", ClassUtils.getSimpleName(this.superClass));
        if (superClass != null) {
            importPackages.add(this.superClass);
        }


        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("serviceFrameworkPkg", frameworkPackages);
        data.put("serviceJavaPkg", javaPackages);

        return data;
    }

}