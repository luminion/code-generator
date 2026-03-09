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

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.enums.*;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * 全局配置
 *
 * @author hubin
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
@Data
@Deprecated
public class GlobalConfig implements TemplateRender {


    

    @Override
    public void init() {
        //if (!isGenerateQuery() && generateExport) {
        //    log.warn("The generated export has been configured but the generated query has not been configured. " +
        //            "The export function depends on the query function. No export related functions will be generated!!!");
        //    generateExport = false;
        //}
        if (javaEEApi.equals(JavaEEApi.JAVAX)) {
            // javax一般为低版本, 不支持@serial
            this.serializableAnnotation = false;
        }
    }

    @Override
    public void renderDataPreProcess(TableInfo tableInfo) {
        boolean havePrimaryKey = tableInfo.isHavePrimaryKey();
        if (!havePrimaryKey) {
            if (generateVoById) {
                log.warn("Table [{}] has no primary key , voById() will not be generated", tableInfo.getName());
            }
            if (generateDelete) {
                log.warn("Table [{}] has no primary key , removeById() will not be generated", tableInfo.getName());
            }
        }
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("author", this.author);
        data.put("date", this.date);
   
        data.put("docLink", this.docLink);
        data.put("lombok", this.lombok);
        data.put("chainModel", this.chainModel);

        data.put("validated", this.validated);
        
        switch (this.docType) {
            case SWAGGER_V3:
                data.put("swagger3", true);
                break;
            case SWAGGER_V2:
                data.put("swagger2", true);
                break;
        }
        switch (this.runtimeEnv) {
            case MY_BATIS_PLUS_SQL_BOOSTER:
                data.put("sqlBooster", true);
            case MYBATIS_PLUS:
            default:
        }
        data.put("serializableUID", this.serializableUID);
        data.put("serializableAnnotation", this.serializableAnnotation);

        data.put("javaApiPackagePrefix", javaEEApi.getPackagePrefix());
        
        data.put("excelApiPackagePrefix", excelApi.getPackagePrefix());
        data.put("excelApiClass", excelApi.getMainEntrance());


        data.put("generateCreate", this.generateCreate);
        data.put("generateUpdate", this.generateUpdate);
        data.put("generateDelete", this.generateDelete);

        data.put("generateVoById", this.generateVoById);
        data.put("generateVoList", this.generateVoList);
        data.put("generateVoPage", this.generateVoPage);
    
        
        data.put("generateImport", this.generateImport);
        data.put("generateExport", this.generateExport);

        data.put("generateSelectByXml", this.isGenerateSelectByXml());

        return data;
    }


    /**
     * 是否生成查询
     */
    public boolean isGenerateSelectByXml(){
        return generateVoById || generateVoList || generateVoPage || generateExport;
    }

    /**
     * 获取领域类序列化需要导入的包
     *
     * @return 包
     */
    public Set<String> getModelSerializableImportPackages() {
        Set<String> importPackages = new TreeSet<>();
        if (this.serializableUID) {
            importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
            if (this.serializableAnnotation) {
                importPackages.add(RuntimeClass.JAVA_IO_SERIAL.getClassName());
            }
        }
        return importPackages;
    }


    /**
     * 获取领域类文档需要导入的包
     *
     * @return 包
     */
    public Set<String> getModelDocImportPackages() {
        Set<String> importPackages = new TreeSet<>();
        switch (this.docType) {
            case SWAGGER_V3:
                importPackages.add(RuntimeClass.SWAGGER_V3_SCHEMA.getClassName());
                break;
            case SWAGGER_V2:
                importPackages.add(RuntimeClass.SWAGGER_V2_API_MODEL.getClassName());
                importPackages.add(RuntimeClass.SWAGGER_V2_API_MODEL_PROPERTY.getClassName());
                break;
        }
        return importPackages;
    }

    public Set<String> getModelLombokImportPackages() {
        Set<String> importPackages = new TreeSet<>();
        if (this.lombok) {
            if (this.chainModel) {
                importPackages.add("lombok.experimental.Accessors");
                importPackages.add(RuntimeClass.LOMBOK_ACCESSORS.getClassName());
            }
            importPackages.add(RuntimeClass.LOMBOK_DATA.getClassName());
        }
        return importPackages;
    }

}