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
import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.metadata.GenerationSummary;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateFile;
import io.github.luminion.generator.util.FileUtils;
import io.github.luminion.generator.util.RuntimeUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public abstract class AbstractTemplateEngine {

    @Getter
    protected final Configurer configurer;
    @Getter
    private GenerationSummary lastGenerationSummary = new GenerationSummary();

    protected boolean outputFile(File file, Map<String, Object> objectMap, String templatePath, boolean fileOverride) {
        if (isCreate(file, fileOverride)) {
            try {
                if (!file.exists()) {
                    File parentFile = file.getParentFile();
                    FileUtils.forceMkdir(parentFile);
                }
                writer(objectMap, templatePath, file);
                return true;
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        return false;
    }

    public AbstractTemplateEngine batchOutput() {
        GenerationSummary generationSummary = new GenerationSummary();
        try {
            List<TableInfo> tableInfoList = configurer.queryTableInfos();
            tableInfoList.forEach(tableInfo -> {
                generationSummary.recordMatchedTable(tableInfo);
                RenderContext renderContext = configurer.createRenderContext(tableInfo);
                Map<String, Object> objectMap = configurer.renderMap(renderContext);
                for (TemplateFile templateFile : renderContext.getTemplateFiles().values()) {
                    if (!templateFile.isGenerate()) {
                        log.info("Skip generating [{}] for table [{}]: {}", templateFile.getKey(), tableInfo.getTableName(), templateFile.getSkipReason());
                        continue;
                    }
                    templateFile.validate();
                    File outputFile = templateFile.resolveOutputFile(tableInfo.getEntityName());
                    if (outputFile(outputFile, objectMap, templateFile.getTemplatePath(), templateFile.isFileOverride())) {
                        generationSummary.recordGeneratedFile(tableInfo, templateFile);
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Unable to create file, please check configuration information", e);
        }
        this.lastGenerationSummary = generationSummary;
        return this;
    }

    protected void setLastGenerationSummary(GenerationSummary generationSummary) {
        this.lastGenerationSummary = generationSummary;
    }

    @SuppressWarnings("unused")
    public abstract String writer(Map<String, Object> objectMap, String templateName, String templateString) throws Exception;

    public abstract void writer(Map<String, Object> objectMap, String templatePath, File outputFile) throws Exception;

    public void open() {
        if (!configurer.getTemplateConfig().isOpenOutputDir()) {
            return;
        }
        String outDir = configurer.getTemplateConfig().getOutputDir();
        if (StringUtils.isBlank(outDir) || !new File(outDir).exists()) {
            System.err.println("Output directory not found：" + outDir);
        } else {
            try {
                RuntimeUtils.openDir(outDir);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public abstract String templateFilePath(String filePath);

    protected boolean isCreate(File file, boolean fileOverride) {
        if (file.exists() && !fileOverride) {
            log.warn("文件[{}]已存在，且未开启文件覆盖配置，需要开启配置可到策略配置中设置！！！", file.getName());
        }
        return !file.exists() || fileOverride;
    }
}
