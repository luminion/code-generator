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
package io.github.luminion.generator.common;

import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import lombok.SneakyThrows;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 配置属性渲染器
 *
 * @author luminion
 * @since 1.0.0
 */
public interface TemplateRender extends Serializable, Comparable<TemplateRender> {

    default int order() {
        return 100;
    }
    
    @Override
    default int compareTo(TemplateRender o) {
        return this.order() - o.order();
    }
    

    /**
     * 验证/初始化配置项
     *
     * @since 1.0.0
     */
    default void init() {

    }

    /**
     * 输出的模板文件
     *
     */
    default List<TemplateFile> renderTemplateFiles() {
        return null;
    }


    /**
     * 渲染前处理, 允许在这一步读取修改配置及表信息
     *
     * @param tableInfo 表信息
     * @since 1.0.0
     */
    default void renderDataPreProcess(TableInfo tableInfo) {

    }

    /**
     * 渲染数据, 这一步应该只渲染信息, 不能再修改配置及表信息
     *
     * @param tableInfo 表信息
     * @return map
     * @since 1.0.0
     */
    @SneakyThrows
    default Map<String, Object> renderData(TableInfo tableInfo) {
        // 添加自定义配置字段信息
//        HashMap<String, Object> data = new HashMap<>();
//        Collection<Field> fields = ReflectUtils.fieldMap(getClass()).values();
//        for (Field field : fields) {
//            data.put(field.getName(), field.get(this));
//        }
//        return data;
        return new HashMap<>();
    }

    /**
     * 渲染后, 对渲染的map进行修改
     *
     * @param tableInfo 表信息
     * @param renderData 渲染的数据
     * @since 1.0.0
     */
    default void renderDataPostProcess(TableInfo tableInfo, Map<String, Object> renderData) {

    }
}
