package io.github.luminion.generator.config;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.common.support.DefaultDatabaseQuery;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.StringUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用于为其他配置提供使用方法
 *
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
public class Resolver {
    /**
     * 配置信息
     */
    @Getter
    private final ConfigCollector<?> configCollector;
    /**
     * 数据库表信息
     */
    @Getter
    private final List<TableInfo> tableInfoList = new ArrayList<>();

    /**
     * 模板渲染列表
     */
    @Getter
    private final List<TemplateRender> templateRenderList = new ArrayList<>();

    /**
     * 模板文件map
     */
    @Getter
    private final Map<String, TemplateFile> templateFileMap = new HashMap<>();

    public Resolver(@NonNull ConfigCollector<?> configCollector) {
        this.configCollector = configCollector;
        // 添加表信息
        try {
            DefaultDatabaseQuery defaultQuery = new DefaultDatabaseQuery(this);
            // 设置表信息
            List<TableInfo> tableInfos = defaultQuery.queryTables();
            if (!tableInfos.isEmpty()) {
                this.tableInfoList.addAll(tableInfos);
            }
        } catch (Exception exception) {
            throw new RuntimeException("创建IDatabaseQuery实例出现错误:", exception);
        }

        // 添加类渲染信息
        templateRenderList.add(this.configCollector.getGlobalConfig());
        templateRenderList.add(this.configCollector.getStrategyConfig());

        templateRenderList.add(this.configCollector.getControllerConfig());

        templateRenderList.add(this.configCollector.getServiceConfig());
        templateRenderList.add(this.configCollector.getServiceImplConfig());

        templateRenderList.add(this.configCollector.getMapperConfig());
        templateRenderList.add(this.configCollector.getMapperXmlConfig());

        templateRenderList.add(this.configCollector.getEntityConfig());

        templateRenderList.add(this.configCollector.getCreateDTOConfig());
        templateRenderList.add(this.configCollector.getUpdateDTOConfig());

        templateRenderList.add(this.configCollector.getQueryDTOConfig());
        templateRenderList.add(this.configCollector.getQueryVOConfig());

        templateRenderList.add(this.configCollector.getImportDTOConfig());
        templateRenderList.add(this.configCollector.getExportDTOConfig());

        // 自定义配置
        if (this.configCollector.getCustomConfig() != null) {
            templateRenderList.add(this.configCollector.getCustomConfig());
        }
        templateRenderList.sort(Comparator.comparingInt(TemplateRender::order));
        // 遍历渲染, 初始化, 添加模板
        for (TemplateRender templateRender : templateRenderList) {
            templateRender.init();
            TemplateFile templateFile = templateRender.renderTemplateFile();
            if (templateFile != null) {
                templateFileMap.put(templateFile.getKey(), templateFile);
            }
        }
        
    }

    /**
     * 获取父包名( parent.module.xxxx)
     *
     * @return {@link String }
     * @since 1.0.0
     */
    public String getParentPackage() {
        String outputModule = configCollector.getGlobalConfig().getParentPackageModule();
        String outputParentPackage = configCollector.getGlobalConfig().getParentPackage();
        if (StringUtils.isNotBlank(outputModule)) {
            return outputParentPackage + "." + outputModule;
        }
        return outputParentPackage;
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    public String joinPath(String parentDir, String packageName) {
        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty("java.io.tmpdir");
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    /**
     * 连接父子包名
     *
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    public String joinPackage(String subPackage) {
        String parent = getParentPackage();
        return StringUtils.isBlank(parent) ? subPackage : (parent + "." + subPackage);
    }

    /**
     * 是否生成指定模板文件
     *
     * @param templateFileEnum 输出文件
     * @return {@link TemplateFileEnum }
     * @since 1.0.0
     */
    public boolean isGenerate(TemplateFileEnum templateFileEnum, TableInfo tableInfo) {
        TemplateFile templateFile = templateFileMap.get(templateFileEnum.getKey());
        return templateFile.isGenerate();
    }

    /**
     * 获取类简单名称
     *
     * @param templateFileEnum 模板文件枚举
     * @return {@link String }
     * @since 1.0.0
     */
    public String getClassSimpleName(TemplateFileEnum templateFileEnum, TableInfo tableInfo) {
        TemplateFile templateFile = templateFileMap.get(templateFileEnum.getKey());
        return templateFile.convertFormatName(tableInfo);
    }

    /**
     * 获取类规范名称
     *
     * @param templateFileEnum 模板文件枚举
     * @return {@link String }
     * @since 1.0.0
     */
    public String getClassName(TemplateFileEnum templateFileEnum, TableInfo tableInfo) {
        TemplateFile templateFile = templateFileMap.get(templateFileEnum.getKey());
        return this.joinPackage(templateFile.getSubPackage()) + "." + templateFile.convertFormatName(tableInfo);
    }


    /**
     * 获取应当生成的所有文件
     *
     * @return 文件列表
     * @since 1.0.0
     */
    public List<TemplateFile> getTemplateFiles() {
        GlobalConfig globalConfig = configCollector.getGlobalConfig();
        return templateFileMap.values()
                .stream().filter(TemplateFile::isGenerate)
                .peek(e -> {
                    String fileOutputDir = e.getOutputDir();
                    if (fileOutputDir == null) {
                        String joinPackage = joinPackage(e.getSubPackage());
                        fileOutputDir = joinPath(globalConfig.getOutputDir(), joinPackage);
                        e.setOutputDir(fileOutputDir);
                    }
                    e.setFileOverride(e.isFileOverride() || globalConfig.isFileOverride());
                }).collect(Collectors.toList());
    }


    /**
     * 获取指定文件是否生成的映射
     * <p>
     * key {@link TemplateFileEnum}
     * value 是否生成
     *
     * @return map
     */
    public Map<String, Boolean> getOutputClassGenerateMap() {
        return templateFileMap.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getValue().getKey(),
                        e -> e.getValue().isGenerate()
                ));
    }

    /**
     * 获取输出类包信息映射
     * <p>
     * key {@link TemplateFileEnum}
     * value 包名(不含类名)
     *
     * @return map
     */
    public Map<String, String> getOutputClassPackageInfoMap() {
        return templateFileMap.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getValue().getKey(),
                        e -> joinPackage(e.getValue().getSubPackage())
                ));
    }

    /**
     * 获取输出类名称映射
     * <p>
     * key {@link TemplateFileEnum}
     * value 完整类名(含包名)
     *
     * @param tableInfo 表信息
     * @return map
     */
    public Map<String, String> getOutputClassNameMap(TableInfo tableInfo) {
        return templateFileMap.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getValue().getKey(),
                        e -> joinPackage(e.getValue().getSubPackage()) + "." + e.getValue().convertFormatName(tableInfo)
                ));
    }

    /**
     * 获取输出类简单名称映射
     * key {@link TemplateFileEnum}
     * value 声明类名(不含包)
     *
     * @param tableInfo 表信息
     * @return map
     */
    public Map<String, String> getOutputClassSimpleNameMap(TableInfo tableInfo) {
        return templateFileMap.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getValue().getKey(),
                        e -> e.getValue().convertFormatName(tableInfo)
                ));
    }


    /**
     * 获取输出的模板参数
     *
     * @return {@link Map }
     * @since 1.0.0
     */
    public Map<String, Object> renderMap(TableInfo tableInfo) {
        HashMap<String, Object> result = new HashMap<>();

        // 渲染前处理, 这一步提供给配置器, 允许其修改配置
        for (TemplateRender templateRender : templateRenderList) {
            templateRender.renderDataPreProcess(tableInfo);
        }

        // 此时配置已完全确定
        tableInfo.processExtraField();

        // 渲染数据
        for (TemplateRender templateRender : templateRenderList) {
            result.putAll(templateRender.renderData(tableInfo));
        }

        // 表信息
        result.put("table", tableInfo);
        // 类名
        result.putAll(this.getOutputClassSimpleNameMap(tableInfo));
        // 类包
        result.put("package", this.getOutputClassPackageInfoMap());
        // 类全名
        result.put("class", this.getOutputClassNameMap(tableInfo));
        // 类是否生成
        result.put("generate", this.getOutputClassGenerateMap());

        // 策略配置
        result.put("booleanColumnRemoveIsPrefix", this.configCollector.getStrategyConfig().isBooleanColumnRemoveIsPrefix());
        result.put("editExcludeColumns", this.configCollector.getStrategyConfig().getEditExcludeColumns());
        result.put("extraFieldSuffixMap", this.configCollector.getStrategyConfig().getExtraFieldSuffixMap());
        result.put("extraFieldProvider", this.configCollector.getStrategyConfig().getExtraFieldProvider());
        
        if (this.configCollector.getStrategyConfig().isShowSchema()) {
            String schemaName = this.configCollector.getDataSourceConfig().getSchemaName();
            if (schemaName == null) {
                log.warn("showSchema is true, but the schemaName could not be obtained from the database information");
            } else {
                result.put("schemaName", schemaName + ".");
            }
        }

        // 渲染后处理
        for (TemplateRender templateRender : templateRenderList) {
            templateRender.renderDataPostProcess(tableInfo, result);
        }

        return result;
    }


}
