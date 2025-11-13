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
package io.github.luminion.generator.config.core;

import io.github.luminion.generator.po.CustomFile;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.common.TemplateRender;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 注入配置
 *
 * @author hubin
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
@Data
public class InjectionConfig implements TemplateRender {

    /**
     * 输出文件之前消费者
     */
    protected BiConsumer<TableInfo, Map<String, Object>> beforeOutputFileBiConsumer;

    /**
     * 自定义配置 Map 对象
     */
    protected Map<String, Object> customMap = new HashMap<>();

    /**
     * 自定义模板文件列表
     *
     * @since 3.5.3
     */
    protected final List<CustomFile> customFiles = new ArrayList<>();

    /**
     * 输出文件前
     */
    public void beforeOutputFile(TableInfo tableInfo, Map<String, Object> objectMap) {
        if (!customMap.isEmpty()) {
            objectMap.putAll(customMap);
            //增加一个兼容兼容取值,推荐还是直接取值外置key即可,例如abc取值${abc}而不需要${cfg.abc}
            objectMap.put("cfg", customMap);
        }
        if (null != beforeOutputFileBiConsumer) {
            beforeOutputFileBiConsumer.accept(tableInfo, objectMap);
        }
    }

}