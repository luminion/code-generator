package io.github.luminion.generator.config;

import io.github.luminion.generator.config.core.GlobalConfig;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.CustomFile;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.StringUtils;
import lombok.NonNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用于为其他配置提供使用方法
 *
 * @author luminion
 * @since 1.0.0
 */
public class Resolver {
    private final Configurer configurer;
    private final Map<TemplateFileEnum, TemplateFile> templateFileMap = new HashMap<>();

    public Resolver(@NonNull Configurer configurer) {
        this.configurer = configurer;
        // 将模板文件添加进map
    }

    /**
     * 获取父包名( parent.module.xxxx)
     *
     * @return {@link String }
     * @since 1.0.0
     */
    public String getParentPackage() {
        String outputModule = configurer.getGlobalConfig().getParentPackageModule();
        String outputParentPackage = configurer.getGlobalConfig().getParentPackage();
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
        TemplateFile templateFile = this.templateFileMap.get(templateFileEnum);
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
        TemplateFile templateFile = this.templateFileMap.get(templateFileEnum);
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
        TemplateFile templateFile = this.templateFileMap.get(templateFileEnum);
        return this.joinPackage(templateFile.getSubPackage()) + "." + templateFile.convertFormatName(tableInfo);
    }


    /**
     * 获取应当输出的所有文件
     *
     * @return {@link List<CustomFile> }
     * @since 1.0.0
     */
    public List<CustomFile> getCustomFiles() {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        return this.templateFileMap.values()
                .stream().filter(TemplateFile::isGenerate)
                .map(e -> {
                    CustomFile customFile = new CustomFile();
                    String fileOutputDir = e.getOutputDir();
                    if (fileOutputDir == null) {
                        String joinPackage = joinPackage(e.getSubPackage());
                        fileOutputDir = joinPath(e.getOutputDir(), joinPackage);
                    }
                    customFile.setFormatNameFunction(e::convertFormatName)
                            .setTemplatePath(e.getTemplatePath())
                            .setOutputFileSuffix(e.getOutputFileSuffix())
                            .setOutputDir(fileOutputDir)
                            .setFileOverride(e.isFileOverride() || globalConfig.isFileOverride());
                    return customFile;
                }).collect(Collectors.toList());
    }


    /**
     * 获取输出类生成映射
     *
     * @return 
     */
    public Map<String, Boolean> getOutputClassGenerateMap() {
        return null;
    }

    /**
     * 获取输出类名称映射
     *
     * @param templateFileEnum 模板文件枚举
     * @param tableInfo        表信息
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public Map<String, String> getOutputClassNameMap(TemplateFileEnum templateFileEnum, TableInfo tableInfo) {

        return null;
    }

    /**
     * 获取输出类简单名称映射
     *
     * @param templateFileEnum 模板文件枚举
     * @param tableInfo        表信息
     * @return 
     */
    public Map<String, String> getOutputClassSimpleNameMap(TemplateFileEnum templateFileEnum, TableInfo tableInfo) {

        return null;
    }

    /**
     * 获取输出类包信息映射
     *
     * @param templateFileEnum 模板文件枚举
     * @param tableInfo        表信息
     * @return 
     */
    public Map<String, String> getOutputClassPackageInfoMap(TemplateFileEnum templateFileEnum, TableInfo tableInfo) {

        return null;
    }


}
