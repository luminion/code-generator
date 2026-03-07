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

import io.github.luminion.generator.render.engine.TemplateRender;
import io.github.luminion.generator.config.base.DataSourceConfig;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.config.base.InjectionConfig;
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
     * 注入配置
     */
    private final InjectionConfig injectionConfig = new InjectionConfig();
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
     * Service功能配置 (整合Service和ServiceImpl)
     */
    private final ServiceConfig serviceConfig = new ServiceConfig();
    /**
     * Mapper功能配置 (整合Mapper和MapperXml)
     */
    private final MapperConfig mapperConfig = new MapperConfig();
    /**
     * 实体配置
     */
    private final EntityConfig entityConfig = new EntityConfig();

    /**
     * 查询功能配置 (整合QueryDTO和QueryVO)
     */
    private final QueryConfig queryConfig = new QueryConfig();

    /**
     * 命令功能配置 (整合CreateDTO和UpdateDTO)，对应CQRS模式的Command
     */
    private final CommandConfig commandConfig = new CommandConfig();

    /**
     * Excel功能配置 (整合ImportDTO和ExportDTO)
     */
    private final ExcelConfig excelConfig = new ExcelConfig();

    /**
     * 特殊配置
     */
    private final C specialConfig;

    //====================模型配置--结束====================

    public ConfigCollector(String url, String username, String password) {
        this.dataSourceConfig = new DataSourceConfig(url, username, password);
        this.specialConfig = null;
    }

    public ConfigCollector(String url, String username, String password, C specialConfig) {
        this.dataSourceConfig = new DataSourceConfig(url, username, password);
        this.specialConfig = specialConfig;
    }


}
