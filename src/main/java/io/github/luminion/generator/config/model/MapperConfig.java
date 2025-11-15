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
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.sqlbooster.extension.mybatisplus.BoosterMpMapper;
import io.github.luminion.sqlbooster.extension.mybatisplus.BoosterMpService;
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
public class MapperConfig implements TemplateRender {

    /**
     * 自定义继承的Mapper类全称，带包名
     */
    protected String superClass = "com.baomidou.mybatisplus.core.mapper.BaseMapper";

    /**
     * Mapper标记注解
     *
     */
//    protected Class<? extends Annotation> mapperAnnotationClass = org.apache.ibatis.annotations.Mapper.class;
    protected String mapperAnnotationClass = "org.apache.ibatis.annotations.Mapper";

    /**
     * 是否开启BaseResultMap（默认 false）
     *
     */
    protected boolean baseResultMap;

    /**
     * 是否开启baseColumnList（默认 false）
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
        Set<String> importPackages = new TreeSet<>();

        Configurer configurer = tableInfo.getConfigurer();
        Resolver resolver = configurer.getResolver();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
        switch (globalConfig.getRuntimeEnv()) {
            case MYBATIS_PLUS:
                if (globalConfig.isGenerateQuery()) {
                    importPackages.add(List.class.getName());
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_DTO, tableInfo));
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
                    importPackages.add(globalConfig.getPageClassPayload().getClassName());
                }
                break;
            case SQL_BOOSTER_MY_BATIS_PLUS:
                this.superClass = "io.github.luminion.sqlbooster.extension.mybatisplus.BoosterMpMapper";
                importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
                if (globalConfig.isGenerateQuery()) {
                    importPackages.add("io.github.luminion.sqlbooster.model.api.Wrapper");
                    importPackages.add(List.class.getName());
                }
                break;
            default:
                throw new RuntimeException("未定义的运行环境");
        }
        if (mapperAnnotationClass != null) {
            data.put("mapperAnnotationClass", mapperAnnotationClass);
            data.put("mapperAnnotationClassSimpleName", mapperAnnotationClass);
        }
        if (superClass != null) {
            importPackages.add(superClass);
            data.put("mapperSuperClassSimpleName", ClassUtils.getSimpleName(this.superClass));
        }

        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("mapperImportPackages4Java", javaPackages);
        data.put("mapperImportPackages4Framework", frameworkPackages);

        return data;
    }

}