package io.github.luminion.generator.config;

import io.github.luminion.generator.config.core.GlobalConfig;
import io.github.luminion.generator.enums.TemplateFileEnum;
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
    private final Map<String, TemplateFile> templateFileMap = new HashMap<>();

    public Resolver(@NonNull Configurer configurer) {
        this.configurer = configurer;
        // controller
        TemplateFile controller = configurer.getControllerConfig().getTemplateFile();
        templateFileMap.put(controller.getKey(), controller);
        
        //  service
        TemplateFile service = configurer.getServiceConfig().getTemplateFile();
        templateFileMap.put(service.getKey(), service);
        TemplateFile serviceImpl = configurer.getServiceImplConfig().getTemplateFile();
        templateFileMap.put(serviceImpl.getKey(), serviceImpl);
        
        // mapper
        TemplateFile mapper = configurer.getMapperConfig().getTemplateFile();
        templateFileMap.put(mapper.getKey(), mapper);
        TemplateFile mapperXml = configurer.getMapperXmlConfig().getTemplateFile();
        templateFileMap.put(mapperXml.getKey(), mapperXml);
        
        // entity
        TemplateFile entity = configurer.getEntityConfig().getTemplateFile();
        templateFileMap.put(entity.getKey(), entity);
        
        // query
        TemplateFile entityQueryDTO = configurer.getEntityQueryDTOConfig().getTemplateFile();
        templateFileMap.put(entityQueryDTO.getKey(), entityQueryDTO);
        TemplateFile entityQueryVO = configurer.getEntityQueryVOConfig().getTemplateFile();
        templateFileMap.put(entityQueryVO.getKey(), entityQueryVO);
        
        // inset/update
        TemplateFile entityInsertDTO = configurer.getEntityInsertDTOConfig().getTemplateFile();
        templateFileMap.put(entityInsertDTO.getKey(), entityInsertDTO);
        TemplateFile entityUpdateDTO = configurer.getEntityUpdateDTOConfig().getTemplateFile();
        templateFileMap.put(entityUpdateDTO.getKey(), entityUpdateDTO);
        
        // excel
        TemplateFile entityExcelImportDTO = configurer.getEntityExcelImportDTOConfig().getTemplateFile();
        templateFileMap.put(entityExcelImportDTO.getKey(), entityExcelImportDTO);
        TemplateFile entityExcelExportDTO = configurer.getEntityExcelExportDTOConfig().getTemplateFile();
        templateFileMap.put(entityExcelExportDTO.getKey(), entityExcelExportDTO);

        // 将模板文件添加进map
        for (TemplateFile customFile : configurer.getInjectionConfig().getCustomFiles()) {
            this.templateFileMap.put(customFile.getKey(), customFile);
        }
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
        TemplateFile templateFile = this.templateFileMap.get(templateFileEnum.getKey());
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
        TemplateFile templateFile = this.templateFileMap.get(templateFileEnum.getKey());
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
        TemplateFile templateFile = this.templateFileMap.get(templateFileEnum.getKey());
        return this.joinPackage(templateFile.getSubPackage()) + "." + templateFile.convertFormatName(tableInfo);
    }


    /**
     * 获取应当生成的所有文件
     *
     * @return 文件列表
     * @since 1.0.0
     */
    public List<TemplateFile> getTemplateFiles() {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        return this.templateFileMap.values()
                .stream().filter(TemplateFile::isGenerate)
                .peek(e -> {
                    String fileOutputDir = e.getOutputDir();
                    if (fileOutputDir == null) {
                        String joinPackage = joinPackage(e.getSubPackage());
                        fileOutputDir = joinPath(e.getOutputDir(), joinPackage);
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


}
