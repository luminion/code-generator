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
import io.github.luminion.generator.config.base.DataSourceConfig;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.config.base.StrategyConfig;
import io.github.luminion.generator.config.model.*;
import lombok.Getter;

/**
 * 配置汇总器具 传递给文件生成工具
 *
 * @author YangHu, tangguo, hubin, Juzi, lanjerry
 * @author luminion
 * @since 1.0.0
 */
@Getter
public class ConfigCollector<C extends TemplateRender> {
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
    private final QueryDTOConfig queryDTOConfig = new QueryDTOConfig();
    /**
     * 查询VO配置
     */
    private final QueryVOConfig queryVOConfig = new QueryVOConfig();

    /**
     * 新增DTO配置
     */
    private final CreateDTOConfig createDTOConfig = new CreateDTOConfig();
    /**
     * 修改DTO配置
     */
    private final UpdateDTOConfig updateDTOConfig = new UpdateDTOConfig();

    /**
     * Excel导入DTO配置
     */
    private final ImportDTOConfig importDTOConfig = new ImportDTOConfig();
    /**
     * Excel导出DTO配置
     */
    private final ExportDTOConfig exportDTOConfig = new ExportDTOConfig();
    /**
     * 特殊配置
     */
    private final C customConfig;

    //====================模型配置--结束====================

    public ConfigCollector(String url, String username, String password) {
        this.dataSourceConfig = new DataSourceConfig(url, username, password);
        this.customConfig = null;
    }

    public ConfigCollector(String url, String username, String password, C customConfig) {
        this.dataSourceConfig = new DataSourceConfig(url, username, password);
        this.customConfig = customConfig;
    }


}
