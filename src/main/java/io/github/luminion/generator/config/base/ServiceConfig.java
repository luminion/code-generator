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

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.enums.OutputFile;
import io.github.luminion.generator.config.po.TableInfo;
import io.github.luminion.generator.config.fill.ITemplate;
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
public class ServiceConfig implements ITemplate {
    /**
     * service层导入的类
     */
    protected Set<String> serviceImportPackages = new TreeSet<>();
    
    /**
     * ServiceImpl导入的类
     */
    protected Set<String> serviceImplImportPackages = new TreeSet<>();

    /**
     * 自定义继承的Service类全称，带包名
     */
    protected String superServiceClass = "com.baomidou.mybatisplus.extension.service.IService";

    /**
     * 自定义继承的ServiceImpl类全称，带包名
     */
    protected String superServiceImplClass = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("superServiceClassPackage", this.superServiceClass);
        data.put("superServiceClass", ClassUtils.getSimpleName(this.superServiceClass));
        data.put("superServiceImplClassPackage", this.superServiceImplClass);
        data.put("superServiceImplClass", ClassUtils.getSimpleName(this.superServiceImplClass));

        Collection<String> serviceImportPackages4Java = serviceImportPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> serviceImportPackages4Framework = serviceImportPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("serviceImportPackages4Java", serviceImportPackages4Java);
        data.put("serviceImportPackages4Framework", serviceImportPackages4Framework);

        Collection<String> serviceImplImportPackages4Java = serviceImplImportPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> serviceImplImportPackages4Framework = serviceImplImportPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        data.put("serviceImplImportPackages4Java", serviceImplImportPackages4Java);
        data.put("serviceImplImportPackages4Framework", serviceImplImportPackages4Framework);
        return data;
    }

    private Set<String> serviceImportPackages(TableInfo tableInfo) {
        Set<String> importPackages = new TreeSet<>();
        Configurer configurer = tableInfo.getConfigurer();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        OutputConfig outputConfig = configurer.getOutputConfig();
        Map<String, String> outputClassCanonicalNameMap = outputConfig.getOutputClassCanonicalNameMap(tableInfo);
        importPackages.add(outputClassCanonicalNameMap.get(OutputFile.entity.name()));
        importPackages.add(this.superServiceClass);
        
        // todo sqlBooster
//        if (globalConfig.isSqlBooster()) {
//            importPackages.add("io.github.luminion.mybatisplus.enhancer.EnhancedService");
//            importPackages.add(outputClassCanonicalNameMap.get(OutputFile.queryVO.name()));
        if (1==1){
            
        } else {
            if (globalConfig.isGenerateQuery()) {
                importPackages.add(outputClassCanonicalNameMap.get(OutputFile.queryVO.name()));
                importPackages.add("java.util.List");
                importPackages.add("java.io.Serializable");
                importPackages.add("com.baomidou.mybatisplus.core.metadata.IPage");
            }
            if (globalConfig.isGenerateExport()) {
                importPackages.add("java.io.OutputStream");
            }
            if (globalConfig.isGenerateImport()) {
                importPackages.add("java.io.InputStream");
                importPackages.add("java.io.OutputStream");
            }
            if (globalConfig.isGenerateDelete()) {
                importPackages.add("java.io.Serializable");
            }
        }
        return importPackages;
    }

    private Set<String> serviceImplImportPackages(TableInfo tableInfo) {
        Set<String> importPackages = new TreeSet<>();
        Configurer configurer = tableInfo.getConfigurer();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        OutputConfig outputConfig = configurer.getOutputConfig();
        Map<String, String> outputClassCanonicalNameMap = outputConfig.getOutputClassCanonicalNameMap(tableInfo);
        importPackages.add(outputClassCanonicalNameMap.get(OutputFile.entity.name()));
        importPackages.add(outputClassCanonicalNameMap.get(OutputFile.mapper.name()));
        importPackages.add(this.superServiceImplClass);
        importPackages.add("org.springframework.stereotype.Service");
        if (outputConfig.getService().isGenerate()) {
            importPackages.add(outputClassCanonicalNameMap.get(OutputFile.service.name()));
        }
        // 生成项
        if (globalConfig.isGenerateQuery()) {
            importPackages.add("java.util.List");
            importPackages.add("java.io.Serializable");
            importPackages.add("com.baomidou.mybatisplus.core.metadata.IPage");
            importPackages.add(outputClassCanonicalNameMap.get(OutputFile.queryVO.name()));
        }
        if (globalConfig.isGenerateExport()) {
            importPackages.add("java.io.OutputStream");
        }
        if (globalConfig.isGenerateImport()) {
            importPackages.add("java.io.InputStream");
            importPackages.add("java.io.OutputStream");
        }
        if (globalConfig.isGenerateDelete()) {
            importPackages.add("java.io.Serializable");
        }
        // todo sqlBooster
//        if (globalConfig.isSqlBooster()) {
        if (false) {
            if (!outputConfig.getService().isGenerate()){
                importPackages.add("io.github.luminion.mybatisplus.enhancer.EnhancedService");
                importPackages.add(outputClassCanonicalNameMap.get(OutputFile.queryVO.name()));
            }
        } else {
            if (globalConfig.isGenerateQuery()) {
//                importPackages.add("com.baomidou.mybatisplus.core.metadata.TableInfo");
                importPackages.add("com.baomidou.mybatisplus.core.metadata.TableInfoHelper");
                importPackages.add("java.util.HashMap");
                importPackages.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
                importPackages.add("org.apache.ibatis.exceptions.TooManyResultsException");
                importPackages.add(outputClassCanonicalNameMap.get(OutputFile.queryVO.name()));
            }
            if (globalConfig.isGenerateExport()) {
                importPackages.add(globalConfig.resolveExcelClassApiCanonicalName());
                importPackages.add("java.util.Arrays");
                importPackages.add(globalConfig.resolveExcelClassCanonicalName("write.style.column.LongestMatchColumnWidthStyleStrategy"));
            }
            if (globalConfig.isGenerateImport()) {
                importPackages.add(globalConfig.resolveExcelClassApiCanonicalName());
                importPackages.add("java.util.List");
                importPackages.add("java.util.Collections");
                importPackages.add("java.util.stream.Collectors");
                importPackages.add("org.springframework.beans.BeanUtils");
            }
            if (globalConfig.isGenerateInsert()) {
                importPackages.add("com.baomidou.mybatisplus.core.metadata.TableInfo");
                importPackages.add("com.baomidou.mybatisplus.core.metadata.TableInfoHelper");
                importPackages.add("org.springframework.beans.BeanUtils");
            }
            if (globalConfig.isGenerateUpdate()) {
                importPackages.add("org.springframework.beans.BeanUtils");
            }
        }
        return importPackages;
    }

}