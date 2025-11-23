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
package io.github.luminion.generator.config;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.model.*;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.common.support.DefaultDatabaseQuery;
import io.github.luminion.generator.config.core.*;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Getter;

import java.util.*;

/**
 * 配置汇总 传递给文件生成工具
 *
 * @author YangHu, tangguo, hubin, Juzi, lanjerry
 * @author luminion
 * @since 1.0.0
 */
@Getter
public class Configurer<C extends TemplateRender> {
    /**
     * 数据库配置信息
     */
    private final DataSourceConfig dataSourceConfig;

    // ====================模型渲染所需====================

    /**
     * 全局配置信息
     */
    private final GlobalConfig globalConfig = new GlobalConfig();
    /**
     * 策略配置信息
     */
    private final StrategyConfig strategyConfig = new StrategyConfig();


    //====================模型配置--开始====================

    /**
     * 控制器配置
     */
    private final ControllerConfig controllerConfig = new ControllerConfig();
    /**
     * 服务配置
     */
    private final ServiceConfig serviceConfig = new ServiceConfig();
    /**
     * 服务实现配置
     */
    private final ServiceImplConfig serviceImplConfig = new ServiceImplConfig();
    /**
     * 映射器配置
     */
    private final MapperConfig mapperConfig = new MapperConfig();
    /**
     * 映射器XML配置
     */
    private final MapperXmlConfig mapperXmlConfig = new MapperXmlConfig();
    /**
     * 实体配置
     */
    private final EntityConfig entityConfig = new EntityConfig();

    /**
     * 查询DTO配置
     */
    private final EntityQueryDTOConfig entityQueryDTOConfig = new EntityQueryDTOConfig();
    /**
     * 查询VO配置
     */
    private final EntityQueryVOConfig entityQueryVOConfig = new EntityQueryVOConfig();

    /**
     * 新增DTO配置
     */
    private final EntityInsertDTOConfig entityInsertDTOConfig = new EntityInsertDTOConfig();
    /**
     * 修改DTO配置
     */
    private final EntityUpdateDTOConfig entityUpdateDTOConfig = new EntityUpdateDTOConfig();

    /**
     * Excel导入DTO配置
     */
    private final EntityExcelImportDTOConfig entityExcelImportDTOConfig = new EntityExcelImportDTOConfig();
    /**
     * Excel导出DTO配置
     */
    private final EntityExcelExportDTOConfig entityExcelExportDTOConfig = new EntityExcelExportDTOConfig();
    /**
     * 特殊配置
     */
    private final C customConfig;

    //====================模型配置--结束====================

    /**
     * 数据库表信息
     * 配置
     */
    private final List<TableInfo> tableInfoList = new ArrayList<>();

    /**
     * 模板渲染列表
     */
    private final List<TemplateRender> templateRenderList = new ArrayList<>();

    /**
     * 模板文件map
     */
    private final Map<String, TemplateFile> templateFileMap = new HashMap<>();


    public Configurer(String url, String username, String password) {
        this.dataSourceConfig = new DataSourceConfig(url, username, password);
        this.customConfig = null;
        init();
    }

    public Configurer(String url, String username, String password, C customConfig) {
        this.dataSourceConfig = new DataSourceConfig(url, username, password);
        this.customConfig = customConfig;
        init();
    }

    public Configurer(String url, String username, String password, String schemaName) {
        this.dataSourceConfig = new DataSourceConfig(url, username, password, schemaName);
        this.customConfig = null;
        init();
    }

    public Configurer(String url, String username, String password, String schemaName, C customConfig) {
        this.dataSourceConfig = new DataSourceConfig(url, username, password, schemaName);
        this.customConfig = customConfig;
        init();
    }

    private void init() {
        if (templateRenderList.isEmpty()) {
            templateRenderList.add(globalConfig);
            templateRenderList.add(strategyConfig);
            templateRenderList.add(controllerConfig);
            templateRenderList.add(serviceConfig);
            templateRenderList.add(serviceImplConfig);
            templateRenderList.add(mapperConfig);
            templateRenderList.add(mapperXmlConfig);
            templateRenderList.add(entityConfig);
            templateRenderList.add(entityQueryDTOConfig);
            templateRenderList.add(entityQueryVOConfig);
            templateRenderList.add(entityInsertDTOConfig);
            templateRenderList.add(entityUpdateDTOConfig);
            templateRenderList.add(entityExcelImportDTOConfig);
            templateRenderList.add(entityExcelExportDTOConfig);
            if (customConfig != null) {
                templateRenderList.add(customConfig);
            }
        }
        for (TemplateRender templateRender : templateRenderList) {
            List<TemplateFile> templateFiles = templateRender.renderTemplateFiles();
            if (templateFiles != null) {
                for (TemplateFile templateFile : templateFiles) {
                    templateFileMap.put(templateFile.getKey(), templateFile);
                }
            }
        }

        if (this.tableInfoList.isEmpty()) {
            try {
                DefaultDatabaseQuery defaultQuery = new DefaultDatabaseQuery(this);
                // 设置表信息
                List<TableInfo> tableInfos = defaultQuery.queryTables();
                if (!tableInfos.isEmpty()) {
                    this.tableInfoList.addAll(tableInfos);
                }
            } catch (Exception exception) {
                throw new RuntimeException("创建IDatabaseQuery实例出现错误:", exception);
            }
        }
    }


    /**
     * 获取解析器
     *
     */
    public Resolver getResolver() {
        return new Resolver(this);
    }

    /**
     * 获取模板文件
     */
    public List<TemplateFile> getTemplateFiles() {
        return getResolver().getTemplateFiles();
    }

    /**
     * 获取输出的模板参数
     *
     * @return {@link Map }
     * @since 1.0.0
     */
    public Map<String, Object> renderMap(TableInfo tableInfo) {
        HashMap<String, Object> result = new HashMap<>();
        Resolver resolver = getResolver();

        // 初始化配置
        for (TemplateRender templateRender : getTemplateRenderList()) {
            templateRender.init();
        }

        // 渲染前处理
        for (TemplateRender templateRender : getTemplateRenderList()) {
            templateRender.renderDataPreProcess(tableInfo);
        }

        // 渲染数据
        for (TemplateRender templateRender : getTemplateRenderList()) {
            result.putAll(templateRender.renderData(tableInfo));
        }

        // 表信息
        result.put("table", tableInfo);
        // 类名
        result.putAll(resolver.getOutputClassSimpleNameMap(tableInfo));
        // 类包
        result.put("package", resolver.getOutputClassPackageInfoMap());
        // 类全名
        result.put("class", resolver.getOutputClassNameMap(tableInfo));
        // 类是否生成
        result.put("generate", resolver.getOutputClassGenerateMap());
        if (dataSourceConfig.getSchemaName() != null) {
            result.put("schemaName", dataSourceConfig.getSchemaName() + ".");
        }

        return result;
    }


}
