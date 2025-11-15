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
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper属性配置
 *
 * @author nieqiurong 2020/10/11.
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
@Data
public class MapperXmlConfig implements TemplateRender {

    /**
     * 是否开启BaseResultMap
     *
     */
    protected boolean baseResultMap;

    /**
     * 是否开启baseColumnList
     *
     */
    protected boolean baseColumnList;

    /**
     * 设置缓存实现类
     *
     */
//    protected Class<? extends org.apache.ibatis.cache.Cache> cache;
    protected String cacheClass;

    /**
     * 排序字段map
     * 字段名 -> 是否倒序
     */
    protected Map<String, Boolean> sortColumnMap = new LinkedHashMap<>();

    @Override
    @SneakyThrows
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        if (cacheClass != null) {
            data.put("mapperCacheClass", cacheClass);
        }

        data.put("baseResultMap", this.baseResultMap);
        data.put("baseColumnList", this.baseColumnList);
        // 排序字段sql
        List<TableField> sortFields = tableInfo.getFields();
        List<String> existColumnNames = sortFields.stream().map(TableField::getColumnName).collect(Collectors.toList());
        if (sortColumnMap != null && !sortColumnMap.isEmpty()) {
            sortColumnMap.entrySet().stream()
                    .filter(e -> existColumnNames.contains(e.getKey()))
                    .map(e -> String.format("a.%s%s", e.getKey(), e.getValue() ? " DESC" : ""))
                    .reduce((e1, e2) -> e1 + ", " + e2)
                    .ifPresent(e -> data.put("orderColumnSql", e));
        }

        return data;
    }

}