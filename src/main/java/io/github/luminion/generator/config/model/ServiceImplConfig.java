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
public class ServiceImplConfig implements TemplateRender {
    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.SERVICE_IMPL.getKey(),
            "%sServiceImpl",
            "service.impl",
            "/templates/mybatis_plus/serviceImpl.java",
            ".java"
    );

    /**
     * 自定义继承的ServiceImpl类全称，带包名
     */
    protected String superClass = "com.baomidou.mybatisplus.extension.service.impl.ServiceImplConfig";


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();

        Set<String> importPackages = new TreeSet<>();
        Configurer configurer = tableInfo.getConfigurer();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        Resolver resolver = tableInfo.getResolver();

        switch (globalConfig.getRuntimeEnv()) {
            case MY_BATIS_PLUS_SQL_BOOSTER:
                this.superClass = "io.github.luminion.sqlbooster.extension.mybatisplus.BoosterMpServiceImpl";
                importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
                if (globalConfig.isGenerateQuery()) {
                    importPackages.add(globalConfig.getPageClassPayload().getClassName());
                    importPackages.add("java.io.Serializable");
                    importPackages.add("io.github.luminion.sqlbooster.model.api.Wrapper");
                    importPackages.add("java.util.List");
              
                }
                break;
            case MYBATIS_PLUS:
                this.superClass = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";
                if (globalConfig.isGenerateQuery()) {
                    importPackages.add("java.io.Serializable");
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_DTO, tableInfo));
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
                    importPackages.add("java.util.List");
                    importPackages.add(globalConfig.getPageClassPayload().getClassName());
                    importPackages.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
                }
                break;
            default:
                throw new RuntimeException("暂不支持的运行环境");
        }
    
        if (superClass != null) {
            importPackages.add(this.superClass);
            data.put("serviceImplSuperClass", ClassUtils.getSimpleName(this.superClass));
        }

        // 类注解及信息
        importPackages.add("org.springframework.stereotype.Service");
        importPackages.add(resolver.getClassName(TemplateFileEnum.MAPPER, tableInfo));
        importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));

        if (resolver.isGenerate(TemplateFileEnum.SERVICE, tableInfo)) {
            importPackages.add(resolver.getClassName(TemplateFileEnum.SERVICE, tableInfo));
        }

        if (globalConfig.isGenerateInsert()) {
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_INSERT_DTO, tableInfo));
            importPackages.add("org.springframework.beans.BeanUtils");
            importPackages.add("java.io.Serializable");
        }
        if (globalConfig.isGenerateUpdate()) {
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_UPDATE_DTO, tableInfo));
            importPackages.add("org.springframework.beans.BeanUtils");
        }
        if (globalConfig.isGenerateDelete()) {
            importPackages.add("java.io.Serializable");
        }
        String excelMain = globalConfig.getExcelApi().getMainEntrance();
        String excelPackagePrefix = globalConfig.getExcelApi().getPackagePrefix();
        String excelClass = excelPackagePrefix + excelMain;
        if (globalConfig.isGenerateImport()) {
            importPackages.add(excelClass);
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_EXCEL_IMPORT_DTO, tableInfo));
            importPackages.add("java.io.OutputStream");
            importPackages.add("java.util.Collections");
            importPackages.add("java.io.InputStream");
            importPackages.add("java.util.List");
            importPackages.add("java.util.stream.Collectors");
            importPackages.add("org.springframework.beans.BeanUtils");
        }

        if (globalConfig.isGenerateExport()) {
            importPackages.add(excelClass);
            importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_EXCEL_EXPORT_DTO, tableInfo));
//            importPackages.add(excelPackagePrefix + "write.style.column.LongestMatchColumnWidthStyleStrategy");
            importPackages.add("java.io.OutputStream");
        }

        Collection<String> serviceImplImportPackages4Java = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> serviceImplImportPackages4Framework = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("serviceImplFrameworkPkg", serviceImplImportPackages4Framework);
        data.put("serviceImplJavaPkg", serviceImplImportPackages4Java);

        return data;
    }

}