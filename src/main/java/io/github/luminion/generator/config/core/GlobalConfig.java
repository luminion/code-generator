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

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.enums.DocType;
import io.github.luminion.generator.enums.ExcelApi;
import io.github.luminion.generator.enums.JavaEEApi;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.po.ClassPayload;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.sqlbooster.core.Page;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Map;


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

    //---------------- lombok---------------

    /**
     * 是否为lombok模型（默认 false）
     */
    protected boolean lombok = true;

    /**
     * 是否为链式模型（默认 false）
     */
    protected boolean lombokChainModel;

    //---------------- 注释文档---------------

    /**
     * 文档注释类型
     */
    protected DocType docType = DocType.JAVA_DOC;
    /**
     * doc作者
     */
    protected String docAuthor = System.getProperty("user.name");
    /**
     * 注释日期
     */
    protected String docDate = LocalDate.now().toString();
    /**
     * 文档注释添加相关类链接
     */
    protected boolean docLink = true;

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

    /**
     * 全局分页类
     */
    protected ClassPayload pageClassPayload = new ClassPayload();
    

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
     * 生成新增方法及配套类
     */
    protected boolean generateInsert = true;

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
    protected boolean generateImport = false;

    /**
     * 生成导出方法(需允许查询)
     */
    protected boolean generateExport = false;

    @Override
    public void init() {
        switch (runtimeEnv) {
            case MYBATIS_PLUS:
                this.pageClassPayload = new ClassPayload("com.baomidou.mybatisplus.core.metadata.IPage");
                break;
            case SQL_BOOSTER_MY_BATIS_PLUS:
                this.pageClassPayload = new ClassPayload("io.github.luminion.sqlbooster.core.Page");
            default:
                throw new IllegalArgumentException("暂不支持的运行环境:" + runtimeEnv);
        }

        if (!generateQuery && generateExport) {
            log.warn("已配置生成导出但未配置生成查询, 导出功能依赖查询功能, 将不会生成导出相关功能!!!");
            generateExport = false;
        }
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        data.put("docAuthor", this.docAuthor);
        data.put("docDate", this.docDate);
        data.put("validated", this.validated);
        data.put("docLink", this.docLink);
        data.put("lombok", this.lombok);
        data.put("lombokChainModel", this.lombokChainModel);

        switch (this.docType) {
            case SPRING_DOC:
                data.put("springdoc", true);
                break;
            case SWAGGER:
                data.put("swagger", true);
                break;
        }
        switch (this.runtimeEnv){
            case SQL_BOOSTER_MY_BATIS_PLUS:
                data.put("sqlBooster", true);
            case MYBATIS_PLUS:
            default:
        }

        data.put("javaApiPackagePrefix", javaEEApi.getPackagePrefix());
        data.put("excelApiPackagePrefix", excelApi.getPackagePrefix());
        data.put("excelApiClass", excelApi.getMainEntrance());

        data.put("generateQuery", this.generateQuery);
        data.put("generateInsert", this.generateInsert);
        data.put("generateUpdate", this.generateUpdate);
        data.put("generateDelete", this.generateDelete);
        data.put("generateImport", this.generateImport);
        data.put("generateExport", this.generateExport);

        return data;
    }

}