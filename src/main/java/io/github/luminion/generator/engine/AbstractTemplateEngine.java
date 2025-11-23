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
package io.github.luminion.generator.engine;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.FileUtils;
import io.github.luminion.generator.util.RuntimeUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 模板引擎抽象类
 *
 * @author hubin
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractTemplateEngine {

    /**
     * 配置信息
     */
    @Getter
    protected final Configurer<?> configurer;

    public AbstractTemplateEngine(Configurer<?> configurer) {
        this.configurer = configurer;
    }

    /**
     * 输出文件
     *
     * @param file         文件
     * @param objectMap    渲染信息
     * @param templatePath 模板路径
     * @param fileOverride 是否覆盖已有文件
     * @since 3.5.2
     */
    protected void outputFile(File file, Map<String, Object> objectMap, String templatePath, boolean fileOverride) {
        if (isCreate(file, fileOverride)) {
            try {
                // 全局判断【默认】
                boolean exist = file.exists();
                if (!exist) {
                    File parentFile = file.getParentFile();
                    FileUtils.forceMkdir(parentFile);
                }
                writer(objectMap, templatePath, file);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * 批量输出 java xml 文件
     */
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = configurer.getTableInfo();
            tableInfoList.forEach(tableInfo -> {
                Map<String, Object> objectMap = configurer.renderMap(tableInfo);
                List<TemplateFile> templateFiles = configurer.getTemplateFiles();
                for (TemplateFile file : templateFiles) {
                    file.validate();
                    String outputDir = file.getOutputDir();
                    String format = String.format(file.getNameFormat(), tableInfo.getEntityName());
                    String fileName = outputDir + File.separator + format + file.getOutputFileSuffix();
                    outputFile(new File(fileName), objectMap, file.getTemplatePath(), file.isFileOverride());
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }


    /**
     * 将模板转化成为字符串
     *
     * @param objectMap      渲染对象 MAP 信息
     * @param templateName   模板名称
     * @param templateString 模板字符串
     * @since 3.5.0
     */
    @SuppressWarnings("unused")
    public abstract String writer(Map<String, Object> objectMap, String templateName, String templateString) throws Exception;

    /**
     * 将模板转化成为文件
     *
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     * @throws Exception 异常
     * @since 3.5.0
     */
    public abstract void writer(Map<String, Object> objectMap, String templatePath, File outputFile) throws Exception;

    /**
     * 打开输出目录
     */
    public void open() {
        String outDir = getConfigurer().getGlobalConfig().getOutputDir();
        if (StringUtils.isBlank(outDir) || !new File(outDir).exists()) {
            System.err.println("未找到输出目录：" + outDir);
        } else if (getConfigurer().getGlobalConfig().isOpenOutputDir()) {
            try {
                RuntimeUtils.openDir(outDir);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    /**
     * 模板真实文件路径
     *
     * @param filePath 文件路径
     * @return ignore
     */
    public abstract String templateFilePath(String filePath);

    /**
     * 检查文件是否创建文件
     *
     * @param file         文件
     * @param fileOverride 是否覆盖已有文件
     * @return 是否创建文件
     * @since 3.5.2
     */
    protected boolean isCreate(File file, boolean fileOverride) {
        if (file.exists() && !fileOverride) {
            log.warn("文件[{}]已存在，且未开启文件覆盖配置，需要开启配置可到策略配置中设置！！！", file.getName());
        }
        return !file.exists() || fileOverride;
    }
}
