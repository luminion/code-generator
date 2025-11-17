package io.github.luminion.generator.config.core;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;

import java.util.Map;

/**
 * 输出文件配置
 *
 * @author luminion
 * @since 1.0.0
 */
@Data
public class OutputConfig implements TemplateRender {

    /**
     * 生成文件的输出目录
     */
    protected String outputDir = System.getProperty("user.dir") + "/src/main/java";
    /**
     * 全局文件覆盖
     */
    protected boolean outputFileGlobalOverride;
    /**
     * 是否打开输出目录
     */
    protected boolean outputDirOpen = true;

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    protected String parentPackage = "io.github.luminion";

    /**
     * 父包模块名
     */
    protected String moduleName = "";

    
    protected io.github.luminion.generator.po.TemplateFile entity = new io.github.luminion.generator.po.TemplateFile(
            TemplateFileEnum.entity.name(),
            "%s",
            "entity",
            "/templates/mybatisplus/entity.java",
            ".java"
    );

    protected io.github.luminion.generator.po.TemplateFile mapper = new io.github.luminion.generator.po.TemplateFile(
            TemplateFileEnum.mapper.name(),
            "%sMapper",
            "mapper",
            "/templates/mybatisplus/mapper.java",
            ".java"
    );
    
    protected io.github.luminion.generator.po.TemplateFile mapperXml = new io.github.luminion.generator.po.TemplateFile(
            TemplateFileEnum.mapperXml.name(),
            "%sMapper",
            "mapper.xml",
            "/templates/mybatisplus/mapper.xml",
            ".xml"
    );
    protected io.github.luminion.generator.po.TemplateFile service = new io.github.luminion.generator.po.TemplateFile(
            TemplateFileEnum.service.name(),
            "I%sService",
            "service",
            "/templates/mybatisplus/service.java",
            ".java"
    );
    protected io.github.luminion.generator.po.TemplateFile serviceImpl = new io.github.luminion.generator.po.TemplateFile(
            TemplateFileEnum.serviceImpl.name(),
            "%sServiceImpl",
            "service.impl",
            "/templates/mybatisplus/serviceImpl.java",
            ".java"
    );
    protected io.github.luminion.generator.po.TemplateFile controller = new io.github.luminion.generator.po.TemplateFile(
            TemplateFileEnum.controller.name(),
            "%sController",
            "controller",
            "/templates/mybatisplus/controller.java",
            ".java"
    );
    protected io.github.luminion.generator.po.TemplateFile insertDTO = new io.github.luminion.generator.po.TemplateFile(
            TemplateFileEnum.insertDTO.name(),
            "%sInsertDTO",
            "dto",
            "/templates/mybatisplus/insertDTO.java",
            ".java"
    );
    protected io.github.luminion.generator.po.TemplateFile updateDTO = new io.github.luminion.generator.po.TemplateFile(
            TemplateFileEnum.updateDTO.name(),
            "%sUpdateDTO",
            "dto",
            "/templates/mybatisplus/updateDTO.java",
            ".java"
    );
    protected io.github.luminion.generator.po.TemplateFile queryDTO = new io.github.luminion.generator.po.TemplateFile(
            TemplateFileEnum.queryDTO.name(),
            "%sQueryDTO",
            "dto",
            "/templates/mybatisplus/queryDTO.java",
            ".java"
    );
    protected io.github.luminion.generator.po.TemplateFile queryVO = new io.github.luminion.generator.po.TemplateFile(
            TemplateFileEnum.queryVO.name(),
            "%sQueryVO",
            "vo",
            "/templates/mybatisplus/queryVO.java",
            ".java"
    );

//
//    protected Stream<io.github.luminion.generator.po.TemplateFile> templateFileStream() {
//        return Stream.of(entity, mapper, mapperXml, service, serviceImpl, controller, insertDTO, updateDTO, queryDTO, queryVO);
//    }

//    /**
//     * 父包名
//     */
//    public String getParentPackage() {
//        if (StringUtils.isNotBlank(moduleName)) {
//            return parentPackage + "." + moduleName;
//        }
//        return parentPackage;
//    }
//
//    /**
//     * 连接父子包名
//     *
//     * @param subPackage 子包名
//     * @return 连接后的包名
//     */
//    public String joinPackage(String subPackage) {
//        String parent = getParentPackage();
//        return StringUtils.isBlank(parent) ? subPackage : (parent + "." + subPackage);
//    }
//
//    /**
//     * 连接路径字符串
//     *
//     * @param parentDir   路径常量字符串
//     * @param packageName 包名
//     * @return 连接后的路径
//     */
//    public String joinPath(String parentDir, String packageName) {
//        if (StringUtils.isBlank(parentDir)) {
//            parentDir = System.getProperty("java.io.tmpdir");
//        }
//        if (!StringUtils.endsWith(parentDir, File.separator)) {
//            parentDir += File.separator;
//        }
//        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
//        return parentDir + packageName;
//    }

//    /**
//     * 获取输出文件类规范名称map
//     *
//     * @param tableInfo 表信息
//     * @see TemplateFileEnum#name()
//     */
//    public Map<String, String> getOutputClassCanonicalNameMap(TableInfo tableInfo) {
//        return templateFileStream().collect(Collectors.toMap(
//                io.github.luminion.generator.po.TemplateFile::getKey,
//                e -> joinPackage(e.getSubPackage()) + "." + e.convertFormatName(tableInfo)
//        ));
//    }
//
//    /**
//     * 获取类简单名称map
//     *
//     * @param tableInfo 表信息
//     * @see TemplateFileEnum#name() 
//     */
//    public Map<String, String> getOutputClassSimpleNameMap(TableInfo tableInfo) {
//        return templateFileStream().collect(Collectors.toMap(
//                io.github.luminion.generator.po.TemplateFile::getKey,
//                e -> e.convertFormatName(tableInfo)
//        ));
//    }
//
//    /**
//     * 获取类生成信息
//     *
//     */
//    public Map<String, Boolean> getOutputClassGenerateMap() {
//        return templateFileStream().collect(Collectors.toMap(io.github.luminion.generator.po.TemplateFile::getKey, io.github.luminion.generator.po.TemplateFile::isGenerate));
//    }
//
//    /**
//     * 获取包信息
//     */
//    public Map<String, String> getOutputClassPackageInfoMap() {
//        return templateFileStream().collect(Collectors.toMap(io.github.luminion.generator.po.TemplateFile::getKey, e -> joinPackage(e.getSubPackage())));
//    }

//    /**
//     * 获取输出文件
//     */
//    public List<CustomFile> getCustomFiles() {
//        return templateFileStream().filter(io.github.luminion.generator.po.TemplateFile::isGenerate).map(e -> {
//            CustomFile customFile = new CustomFile();
//            String fileOutputDir = e.getOutputDir();
//            if (fileOutputDir == null) {
//                String joinPackage = joinPackage(e.getSubPackage());
//                fileOutputDir = joinPath(outputDir, joinPackage);
//            }
//            customFile.setFormatNameFunction(e::convertFormatName)
//                    .setTemplatePath(e.getTemplatePath())
//                    .setOutputFileSuffix(e.getOutputFileSuffix())
//                    .setOutputDir(fileOutputDir)
//                    .setFileOverride(e.isFileOverride() || this.outputFileGlobalOverride);
//            return customFile;
//        }).collect(Collectors.toList());
//    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> map = TemplateRender.super.renderData(tableInfo);
        map.putAll(this.getOutputClassSimpleNameMap(tableInfo));
        map.put("package", this.getOutputClassPackageInfoMap());
        map.put("class", this.getOutputClassCanonicalNameMap(tableInfo));
        map.put("generate", this.getOutputClassGenerateMap());
        return map;
    }

    /**
     * 根据设置处理文件是否构建
     *
     * @param configurer 配置器
     */
    public void processOutput(Configurer configurer) {
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        if (!globalConfig.isGenerateInsert()){
            this.insertDTO.setGenerate(false);
        }
        if (!globalConfig.isGenerateUpdate()){
            this.updateDTO.setGenerate(false);
        }
        if (!globalConfig.isGenerateQuery()){
            this.queryDTO.setGenerate(false);
            // todo 待完善
//            if (!globalConfig.isSqlBooster()){
//                this.queryVO.setGenerate(false);
//            }
        }
    }
}