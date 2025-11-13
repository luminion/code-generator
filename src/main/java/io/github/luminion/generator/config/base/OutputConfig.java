package io.github.luminion.generator.config.base;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.OutputFile;
import io.github.luminion.generator.po.CustomFile;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    protected String parentPackage = "io.github.luminion";

    /**
     * 父包模块名
     */
    protected String moduleName = "";

    /**
     * 全局文件覆盖
     */
    protected boolean globalFileOverride;

    /**
     * 是否打开输出目录
     */
    protected boolean open = true;
    
    protected TemplateFile entity = new TemplateFile(
            OutputFile.entity.name(),
            "%s",
            "entity",
            "/templates/base/entity.java",
            ".java"
    );

    protected TemplateFile mapper = new TemplateFile(
            OutputFile.mapper.name(),
            "%sMapper",
            "mapper",
            "/templates/base/mapper.java",
            ".java"
    );
    
    protected TemplateFile mapperXml = new TemplateFile(
            OutputFile.mapperXml.name(),
            "%sMapper",
            "mapper.xml",
            "/templates/base/mapper.xml",
            ".xml"
    );
    protected TemplateFile service = new TemplateFile(
            OutputFile.service.name(),
            "I%sService",
            "service",
            "/templates/base/service.java",
            ".java"
    );
    protected TemplateFile serviceImpl = new TemplateFile(
            OutputFile.serviceImpl.name(),
            "%sServiceImpl",
            "service.impl",
            "/templates/base/serviceImpl.java",
            ".java"
    );
    protected TemplateFile controller = new TemplateFile(
            OutputFile.controller.name(),
            "%sController",
            "controller",
            "/templates/base/controller.java",
            ".java"
    );
    protected TemplateFile insertDTO = new TemplateFile(
            OutputFile.insertDTO.name(),
            "%sInsertDTO",
            "dto",
            "/templates/base/insertDTO.java",
            ".java"
    );
    protected TemplateFile updateDTO = new TemplateFile(
            OutputFile.updateDTO.name(),
            "%sUpdateDTO",
            "dto",
            "/templates/base/updateDTO.java",
            ".java"
    );
    protected TemplateFile queryDTO = new TemplateFile(
            OutputFile.queryDTO.name(),
            "%sQueryDTO",
            "dto",
            "/templates/base/queryDTO.java",
            ".java"
    );
    protected TemplateFile queryVO = new TemplateFile(
            OutputFile.queryVO.name(),
            "%sQueryVO",
            "vo",
            "/templates/base/queryVO.java",
            ".java"
    );

    public TemplateFile getInsertDTO() {
        return insertDTO;
    }

    public TemplateFile getUpdateDTO() {
        return updateDTO;
    }

    public TemplateFile getQueryDTO() {
        return queryDTO;
    }

    public TemplateFile getQueryVO() {
        return queryVO;
    }

    protected Stream<TemplateFile> templateFileStream() {
        return Stream.of(entity, mapper, mapperXml, service, serviceImpl, controller, insertDTO, updateDTO, queryDTO, queryVO);
    }

    /**
     * 父包名
     */
    public String getParentPackage() {
        if (StringUtils.isNotBlank(moduleName)) {
            return parentPackage + "." + moduleName;
        }
        return parentPackage;
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
     * 获取输出文件类规范名称map
     *
     * @param tableInfo 表信息
     * @see OutputFile#name()
     */
    public Map<String, String> getOutputClassCanonicalNameMap(TableInfo tableInfo) {
        return templateFileStream().collect(Collectors.toMap(
                TemplateFile::getKey,
                e -> joinPackage(e.getSubPackage()) + "." + e.convertFormatName(tableInfo)
        ));
    }

    /**
     * 获取类简单名称map
     *
     * @param tableInfo 表信息
     * @see OutputFile#name() 
     */
    public Map<String, String> getOutputClassSimpleNameMap(TableInfo tableInfo) {
        return templateFileStream().collect(Collectors.toMap(
                TemplateFile::getKey,
                e -> e.convertFormatName(tableInfo)
        ));
    }

    /**
     * 获取类生成信息
     *
     */
    public Map<String, Boolean> getOutputClassGenerateMap() {
        return templateFileStream().collect(Collectors.toMap(TemplateFile::getKey, TemplateFile::isGenerate));
    }

    /**
     * 获取包信息
     */
    public Map<String, String> getOutputClassPackageInfoMap() {
        return templateFileStream().collect(Collectors.toMap(TemplateFile::getKey, e -> joinPackage(e.getSubPackage())));
    }

    /**
     * 获取输出文件
     */
    public List<CustomFile> getCustomFiles() {
        return templateFileStream().filter(TemplateFile::isGenerate).map(e -> {
            CustomFile customFile = new CustomFile();
            String fileOutputDir = e.getOutputDir();
            if (fileOutputDir == null) {
                String joinPackage = joinPackage(e.getSubPackage());
                fileOutputDir = joinPath(outputDir, joinPackage);
            }
            customFile.setFormatNameFunction(e::convertFormatName)
                    .setTemplatePath(e.getTemplatePath())
                    .setOutputFileSuffix(e.getOutputFileSuffix())
                    .setOutputDir(fileOutputDir)
                    .setFileOverride(e.isFileOverride() || this.globalFileOverride);
            return customFile;
        }).collect(Collectors.toList());
    }

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