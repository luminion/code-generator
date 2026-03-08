package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.MultiTemplateModelRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.ConfigResolver;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mapper功能配置
 * <p>
 * 整合Mapper接口和MapperXml的配置
 *
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
@Data
public class MapperConfig implements MultiTemplateModelRender {

    /**
     * Mapper接口模板文件
     */
    private TemplateFile mapperTemplateFile = new TemplateFile(
            TemplateFileEnum.MAPPER.getKey(),
            "%sMapper",
            "mapper",
            "/templates/mybatis_plus/mapper.java",
            ".java"
    );

    /**
     * MapperXml模板文件
     */
    private TemplateFile mapperXmlTemplateFile = new TemplateFile(
            TemplateFileEnum.MAPPER_XML.getKey(),
            "%sMapper",
            "xml",
            "/templates/mybatis_plus/mapper.xml",
            ".xml"
    );

    /**
     * Mapper接口自定义父类
     */
    private String mapperSuperClass;

    /**
     * Mapper标记注解
     */
    private String mapperAnnotationClass = "org.apache.ibatis.annotations.Mapper";

    /**
     * 是否启用MapperXml生成（默认启用）
     */
    private boolean generateMapperXml = true;

    /**
     * 是否开启BaseResultMap（默认关闭）
     */
    private boolean baseResultMap = false;

    /**
     * 是否开启baseColumnList（默认关闭）
     */
    private boolean baseColumnList = false;

    /**
     * 缓存实现类
     */
    private String cacheClass;

    /**
     * 排序字段map
     * 字段名 -> 是否倒序
     */
    private Map<String, Boolean> sortColumnMap = new LinkedHashMap<>();

    @Override
    public void init() {
    }

    @Override
    public void renderDataPreProcess(TableInfo tableInfo) {
        if (!generateMapperXml) {
            mapperXmlTemplateFile.setGenerate(false);
        }
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();

        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();
        GlobalConfig globalConfig = configCollector.getGlobalConfig();

        // Mapper接口渲染数据
        renderMapperData(tableInfo, data, configResolver, globalConfig);

        // MapperXml渲染数据
        if (generateMapperXml) {
            renderMapperXmlData(tableInfo, data);
        }

        return data;
    }

    private void renderMapperData(TableInfo tableInfo, Map<String, Object> data,
                                  ConfigResolver configResolver, GlobalConfig globalConfig) {
        Set<String> importPackages = new TreeSet<>();

        importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
        switch (globalConfig.getRuntimeEnv()) {
            case MYBATIS_PLUS:
                this.mapperSuperClass = RuntimeClass.MYBATIS_PLUS_BASE_MAPPER.getClassName();
                if (globalConfig.isGenerateSelectByXml()) {
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                    importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getClassName());
                }
                break;
            case MY_BATIS_PLUS_SQL_BOOSTER:
                this.mapperSuperClass = RuntimeClass.SQL_BOOSTER_MP_MAPPER.getClassName();
                importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                if (globalConfig.isGenerateSelectByXml()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                }
                break;
            default:
                throw new RuntimeException("Unknown runtime environment:" + globalConfig.getRuntimeEnv());
        }
        if (mapperAnnotationClass != null) {
            data.put("mapperAnnotationClass", "@" + ClassUtils.getSimpleName(mapperAnnotationClass));
            importPackages.add(mapperAnnotationClass);
        }
        if (mapperSuperClass != null) {
            importPackages.add(mapperSuperClass);
            data.put("mapperSuperClass", ClassUtils.getSimpleName(this.mapperSuperClass));
        }

        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));

        data.put("mapperFrameworkPkg", frameworkPackages);
        data.put("mapperJavaPkg", javaPackages);
    }

    @SneakyThrows
    private void renderMapperXmlData(TableInfo tableInfo, Map<String, Object> data) {
        if (cacheClass != null) {
            data.put("mapperCacheClass", cacheClass);
        }

        data.put("baseResultMap", this.baseResultMap);
        data.put("baseColumnList", this.baseColumnList);
        // 排序字段sql
        List<TableField> sortFields = tableInfo.getFields();
        List<String> existColumnNames = sortFields.stream().map(TableField::getColumnName).collect(Collectors.toList());
        if (sortColumnMap != null && !sortColumnMap.isEmpty()) {
            sortColumnMap.entrySet().stream()
                    .filter(e -> existColumnNames.contains(e.getKey()))
                    .map(e -> String.format("a.%s%s", e.getKey(), e.getValue() ? " DESC" : ""))
                    .reduce((e1, e2) -> e1 + ", " + e2)
                    .ifPresent(e -> data.put("orderColumnSql", e));
        }
    }

    @Override
    public void renderDataPostProcess(TableInfo tableInfo, Map<String, Object> renderData) {
    }

    @Override
    public List<TemplateFile> renderTemplateFiles() {
        List<TemplateFile> files = new ArrayList<>();
        files.add(mapperTemplateFile);
        files.add(mapperXmlTemplateFile);
        return files;
    }
}
