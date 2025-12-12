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

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.enums.*;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
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
public class GlobalConfig implements TemplateRender {

    //---------------- 通用模型项---------------

    /**
     * 是否为lombok模型（默认 false）
     */
    protected boolean lombok = true;

    /**
     * 是否为链式模型setter（默认 false）
     */
    protected boolean chainModel;

    /**
     * 实体是否生成 serialVersionUID
     */
    protected boolean serializableUID = true;

    /**
     * 实体是否启用java.io.Serial (需JAVA 14) 注解
     *
     */
    protected boolean serializableAnnotation = true;

    //---------------- 注释文档---------------

    /**
     * 文档注释类型
     */
    protected DocType docType = DocType.JAVA_DOC;
    /**
     * 文档注释添加相关类链接
     */
    protected boolean docLink = true;
    /**
     * doc作者
     */
    protected String author = System.getProperty("user.name");
    /**
     * 注释日期
     */
    protected String date = LocalDate.now().toString();


    //---------------- 运行时环境相关--------------

    /**
     * java ee api
     */
    protected JavaEEApi javaEEApi = JavaEEApi.JAKARTA;
    /**
     * excel api
     */
    protected ExcelApi excelApi = ExcelApi.EASY_EXCEL;

    /**
     * 外部运行环境
     */
    protected RuntimeEnv runtimeEnv = RuntimeEnv.MYBATIS_PLUS;


    // ----------------输出目录及包相关-----------------

    /**
     * 生成文件的输出目录
     */
    protected String outputDir = System.getProperty("user.dir") + "/src/main/java";
    /**
     * 是否打开输出目录
     */
    protected boolean openOutputDir;

    /**
     * 输出文件覆盖(全局)
     */
    protected boolean fileOverride;

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    protected String parentPackage = "com.example";

    /**
     * 父包模块名
     */
    protected String parentPackageModule = "";

    // ----------------生成内容相关-----------------

    /**
     * 生成参数校验相关注解
     */
    protected boolean validated;

    /**
     * 生成查询相关方法及配套类
     */
    protected boolean generateQuery = true;

    /**
     * 生成id查询方法
     */
    protected boolean generateQueryVoById = true;
    
    /**
     * 批量查询相关方法及配套类
     */
    protected boolean generateQueryVoList = true;
    
    /**
     * 批量查询分页相关方法及配套类
     */
    protected boolean generateQueryPage = true;

    /**
     * 生成新增方法及配套类
     */
    protected boolean generateCreate = true;

    /**
     * 生成更新方法及配套类
     */
    protected boolean generateUpdate = true;

    /**
     * 生成删除方法及配套类
     */
    protected boolean generateDelete = true;

    /**
     * 生成导入方法及配套类(需允许新增)
     */
    protected boolean generateImport = true;

    /**
     * 生成导出方法(需允许查询)
     */
    protected boolean generateExport = true;

    @Override
    public void init() {
        if (!generateQuery && generateExport) {
            log.warn("已配置生成导出但未配置生成查询, 导出功能依赖查询功能, 将不会生成导出相关功能!!!");
            generateExport = false;
        }
        if (javaEEApi.equals(JavaEEApi.JAVAX)) {
            // javax一般为低版本, 不支持@serial
            this.serializableAnnotation = false;
        }
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        data.put("author", this.author);
        data.put("date", this.date);
        data.put("validated", this.validated);
        data.put("docLink", this.docLink);
        data.put("lombok", this.lombok);
        data.put("chainModel", this.chainModel);

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

        data.put("generateQuery", this.generateQuery);
        data.put("generateCreate", this.generateCreate);
        data.put("generateUpdate", this.generateUpdate);
        data.put("generateDelete", this.generateDelete);
        data.put("generateImport", this.generateImport);
        data.put("generateExport", this.generateExport);

        return data;
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