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
package io.github.luminion.generator.config.base;

import io.github.luminion.generator.config.enums.OutputFile;
import io.github.luminion.generator.config.po.TableField;
import io.github.luminion.generator.config.po.TableInfo;
import io.github.luminion.generator.config.fill.ITemplate;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
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
public class MapperConfig implements ITemplate {

    /**
     * 自定义继承的Mapper类全称，带包名
     */
    protected String superClass = "com.baomidou.mybatisplus.core.mapper.BaseMapper";

    /**
     * Mapper标记注解
     *
     * @since 3.5.3
     * @see org.apache.ibatis.annotations.Mapper
     */
//    protected Class<?> mapperAnnotationClass = ClassUtils.toClass("org.apache.ibatis.annotations.Mapper");
    protected Class<?> mapperAnnotationClass;

    /**
     * 是否开启BaseResultMap（默认 false）
     *
     * @since 3.5.0
     */
    protected boolean baseResultMap;

    /**
     * 是否开启baseColumnList（默认 false）
     *
     * @since 3.5.0
     */
    protected boolean baseColumnList;

    /**
     * 设置缓存实现类
     *
     * @see org.apache.ibatis.cache.Cache
     * @since 3.5.0
     */
    protected Class<?> cache;

    /**
     * 排序字段map
     * 字段名 -> 是否倒序
     */
    protected Map<String, Boolean> sortColumnMap = new LinkedHashMap<>();

    @Override
    @SneakyThrows
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = ITemplate.super.renderData(tableInfo);
        boolean enableCache = this.cache != null;
        data.put("enableCache", enableCache);
        data.put("mapperAnnotation", mapperAnnotationClass != null);
        data.put("mapperAnnotationClass", mapperAnnotationClass);
        data.put("baseResultMap", this.baseResultMap);
        data.put("baseColumnList", this.baseColumnList);
        data.put("superMapperClassPackage", this.superClass);
        if (enableCache) {
            data.put("cache", cache);
            data.put("cacheClassName", cache.getName());
        }
        data.put("superMapperClass", ClassUtils.getSimpleName(this.superClass));
        // 排序字段sql
        List<TableField> sortFields = tableInfo.getFields();
        List<String> existColumnNames = sortFields.stream().map(TableField::getColumnName).collect(Collectors.toList());
        if (sortColumnMap != null && !sortColumnMap.isEmpty()) {
            sortColumnMap.entrySet().stream().filter(e -> existColumnNames.contains(e.getKey())).map(e -> String.format("a.%s%s", e.getKey(), e.getValue() ? " DESC" : "")).reduce((e1, e2) -> e1 + ", " + e2).ifPresent(e -> data.put("orderBySql", e));
        }

        Set<String> mapperImportPackages = this.mapperImportPackages(tableInfo);
        Collection<String> javaPackages = mapperImportPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> frameworkPackages = mapperImportPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("mapperImportPackages4Java", javaPackages);
        data.put("mapperImportPackages4Framework", frameworkPackages);

        return data;
    }

    public Set<String> mapperImportPackages(TableInfo tableInfo) {
        Set<String> importPackages = new TreeSet<>();
        if (StringUtils.isNotBlank(superClass)) {
            importPackages.add(superClass);
        }
        if (mapperAnnotationClass != null) {
            importPackages.add(mapperAnnotationClass.getName());
        }
        Map<String, String> classCanonicalNameMap = tableInfo.getConfigurer().getOutputConfig().getOutputClassCanonicalNameMap(tableInfo);
        importPackages.add(classCanonicalNameMap.get(OutputFile.entity.name()));
        GlobalConfig globalConfig = tableInfo.getConfigurer().getGlobalConfig();
        if (globalConfig.isGenerateQuery()) {
            importPackages.add(List.class.getCanonicalName());
            importPackages.add(classCanonicalNameMap.get(OutputFile.queryVO.name()));
            importPackages.add("com.baomidou.mybatisplus.core.metadata.IPage");
        }
        return importPackages;
    }

}