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

import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.common.TemplateRender;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    /**
     * 作者
     */
    protected String author = "luminion";



    /**
     * 获取注释日期
     *
     * @since 3.5.0
     */
    protected String commentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    /**
     * 【实体】是否为lombok模型（默认 false）<br>
     * <a href="https://projectlombok.org/">document</a>
     */
    protected boolean lombok;

    /**
     * 【实体】是否为链式模型（默认 false）
     *
     * @since 3.3.2
     */
    protected boolean chainModel;

    /**
     * 开启 swagger 模式（默认 false 与 springdoc 不可同时使用）
     */
    protected boolean swagger;

    public boolean isSwagger() {
        // springdoc 设置优先于 swagger
        return !springdoc && swagger;
    }

    /**
     * 开启 springdoc 模式（默认 false 与 swagger 不可同时使用）
     */
    protected boolean springdoc;

    /**
     * 文档注释添加相关类链接
     */
    protected boolean commentLink;

    /**
     * javaEE api包(jakarta或javax)
     * <p>
     * 涉及HttpServletRequest,HttpServletResponse,@Resource
     */
    protected String jakartaApiPackagePrefix = "jakarta";

    /**
     * excel注解的包
     */
    protected String excelApiPackagePrefix = "cn.idev.excel";

    /**
     * excel类
     */
    protected String excelApiClass = "FastExcel";

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
    protected boolean generateImport = true;

    /**
     * 生成导出方法(需允许查询)
     */
    protected boolean generateExport = true;

    public boolean isGenerateExport() {
        return generateExport && generateQuery;
    }

    /**
     * 参数校验
     */
    protected boolean validated = true;


    /**
     * 解析jakarta类规范名称
     * <p>
     * 根据{@link #jakartaApiPackagePrefix}解析
     *
     * @param suffix 后缀
     */
    public String resolveJakartaClassCanonicalName(String suffix) {
        return jakartaApiPackagePrefix + "." + suffix;
    }

    /**
     * 解析excel类规范名称
     * <p>
     * 根据{@link #excelApiPackagePrefix}解析
     *
     * @param suffix 后缀
     */
    public String resolveExcelClassCanonicalName(String suffix) {
        return excelApiPackagePrefix + "." + suffix;
    }

    public String resolveExcelClassApiCanonicalName() {
        return excelApiPackagePrefix + "." + excelApiClass;
    }

    public void validate() {
        if (!generateQuery && generateExport) {
            log.warn("已配置生成导出但未配置生成查询, 导出功能依赖查询功能, 将不会生成导出相关功能!!!");
            generateExport = false;
        }
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        data.put("author", this.author);
        data.put("date", this.getCommentDate());
        data.put("validated", this.validated);
        data.put("commentLink", this.commentLink);
        data.put("lombok", this.lombok);
        data.put("chainModel", this.chainModel);

        data.put("swagger", this.isSwagger());
        data.put("springdoc", this.springdoc);
        //  todo sqlBooster
//        data.put("sqlBooster", this.sqlBooster);

        data.put("javaApiPackagePrefix", this.jakartaApiPackagePrefix);
        data.put("excelApiPackagePrefix", this.excelApiPackagePrefix);
        data.put("excelApiClass", this.excelApiClass);


        data.put("generateQuery", this.generateQuery);
        data.put("generateInsert", this.generateInsert);
        data.put("generateUpdate", this.generateUpdate);
        data.put("generateDelete", this.generateDelete);
        data.put("generateImport", this.generateImport);
        data.put("generateExport", this.generateExport);

        return data;
    }
}