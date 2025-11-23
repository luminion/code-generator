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
     * 导入的包
     */
    private Set<String> importPackages = new TreeSet<>();

    /**
     * 自定义继承的Service类全称，带包名
     */
    protected String superClass;

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        Configurer configurer = tableInfo.getConfigurer();
        Resolver resolver = configurer.getResolver();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
      

        switch (globalConfig.getRuntimeEnv()) {
            case MY_BATIS_PLUS_SQL_BOOSTER:
                this.superClass = "io.github.luminion.sqlbooster.extension.mybatisplus.BoosterMpService";
                importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
                importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
                if (globalConfig.isGenerateInsert()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_INSERT_DTO, tableInfo));
                }
                if (globalConfig.isGenerateUpdate()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_UPDATE_DTO, tableInfo));
                }
                if (globalConfig.isGenerateDelete()) {
                    importPackages.add("java.io.Serializable");
                }
                if (globalConfig.isGenerateQuery()) {
                    importPackages.add(globalConfig.getPageClassPayload().getClassName());
                    importPackages.add("io.github.luminion.sqlbooster.model.api.Wrapper");
                    importPackages.add("java.util.List");
                    importPackages.add("java.io.Serializable");
                }
                if (globalConfig.isGenerateImport()) {
                    importPackages.add("java.io.InputStream");
                    importPackages.add("java.io.OutputStream");
                }
                if (globalConfig.isGenerateExport()) {
                    importPackages.add("io.github.luminion.sqlbooster.model.api.Wrapper");
                    importPackages.add("java.io.OutputStream");
                }
                break;
            case MYBATIS_PLUS:
                this.superClass = "com.baomidou.mybatisplus.extension.service.IService";
                importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
                if (globalConfig.isGenerateInsert()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_INSERT_DTO, tableInfo));
                    importPackages.add("java.io.Serializable");
                }
                if (globalConfig.isGenerateUpdate()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_UPDATE_DTO, tableInfo));
                }
                if (globalConfig.isGenerateDelete()) {
                    importPackages.add("java.io.Serializable");
                }
                if (globalConfig.isGenerateQuery()) {
                    importPackages.add("java.io.Serializable");
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_DTO, tableInfo));
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
                    importPackages.add("java.util.List");
                    importPackages.add(globalConfig.getPageClassPayload().getClassName());
                }
                if (globalConfig.isGenerateImport()) {
                    importPackages.add("java.io.InputStream");
                    importPackages.add("java.io.OutputStream");
                }
                if (globalConfig.isGenerateExport()) {
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_DTO, tableInfo));
                    importPackages.add("java.io.OutputStream");
                }
                break;
            default:
                throw new RuntimeException("暂不支持的运行环境");
        }


        data.put("serviceSuperClass", ClassUtils.getSimpleName(this.superClass));
        if (superClass != null) {
            importPackages.add(this.superClass);
        }

        
        Collection<String> serviceImportPackages4Framework = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> serviceImportPackages4Java = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        data.put("serviceFrameworkPkg", serviceImportPackages4Framework);
        data.put("serviceJavaPkg", serviceImportPackages4Java);
        
        return data;
    }

}