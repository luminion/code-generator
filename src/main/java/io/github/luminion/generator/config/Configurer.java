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

import io.github.luminion.generator.config.base.DataSourceConfig;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.common.support.DatabaseQueryDefault;
import io.github.luminion.generator.config.base.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置汇总 传递给文件生成工具
 *
 * @author YangHu, tangguo, hubin, Juzi, lanjerry
 * @author luminion
 * @since 1.0.0
 */
@Getter
public class Configurer {
    /**
     * 数据库配置信息
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * 全局配置信息
     */
    private final GlobalConfig globalConfig = new GlobalConfig();
    /**
     * 输出文件配置
     */
    private final OutputConfig outputConfig = new OutputConfig();
    /**
     * 策略配置信息
     */
    private final StrategyConfig strategyConfig = new StrategyConfig();
    /**
     * 注入配置信息
     */
    private final InjectionConfig injectionConfig = new InjectionConfig();
    /**
     * 实体配置
     */
    private final EntityConfig entityConfig = new EntityConfig();
    /**
     * 映射器配置
     */
    private final MapperConfig mapperConfig = new MapperConfig();
    /**
     * 服务配置
     */
    private final ServiceConfig serviceConfig = new ServiceConfig();
    /**
     * 控制器配置
     */
    private final ControllerConfig controllerConfig = new ControllerConfig();
    /**
     * 模型配置
     */
    private final ModelConfig modelConfig = new ModelConfig();
    /**
     * 数据库表信息
     * 配置
     */
    private final List<TableInfo> tableInfo = new ArrayList<>();


    public Configurer(String url, String username, String password) {
        this.dataSourceConfig = new DataSourceConfig(url, username, password);
    }

    public List<TableInfo> getTableInfo() {
        getStrategyConfig().validate();
        getGlobalConfig().validate();
        getOutputConfig().processOutput(this);
        if (this.tableInfo.isEmpty()){
            try {
                DatabaseQueryDefault defaultQuery = new DatabaseQueryDefault(this);
                // 设置表信息
                List<TableInfo> tableInfos = defaultQuery.queryTables();
                if (!tableInfos.isEmpty()) {
                    this.tableInfo.addAll(tableInfos);
                }
            } catch (Exception exception) {
                throw new RuntimeException("创建IDatabaseQuery实例出现错误:", exception);
            }
        }
        return tableInfo;
    }

}
