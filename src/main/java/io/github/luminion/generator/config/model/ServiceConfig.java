package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.MultiTemplateModelRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.ConfigResolver;
import io.github.luminion.generator.config.base.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service功能配置
 * <p>
 * 整合Service接口和ServiceImpl实现类的配置
 *
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ServiceConfig implements MultiTemplateModelRender {

    /**
     * Service接口模板文件
     */
    private TemplateFile serviceTemplateFile = new TemplateFile(
            TemplateFileEnum.SERVICE.getKey(),
            "%sService",
            "service",
            "/templates/mybatis_plus/service.java",
            ".java"
    );

    /**
     * ServiceImpl实现类模板文件
     */
    private TemplateFile serviceImplTemplateFile = new TemplateFile(
            TemplateFileEnum.SERVICE_IMPL.getKey(),
            "%sServiceImpl",
            "service.impl",
            "/templates/mybatis_plus/serviceImpl.java",
            ".java"
    );

    /**
     * Service接口自定义父类
     */
    private String serviceSuperClass;

    /**
     * ServiceImpl实现类自定义父类
     */
    private String serviceImplSuperClass;

    /**
     * 是否启用ServiceImpl生成（默认启用）
     */
    private boolean generateServiceImpl = true;

    @Override
    public void init() {
    }

    @Override
    public void renderDataPreProcess(TableInfo tableInfo) {
        if (!generateServiceImpl) {
            serviceImplTemplateFile.setGenerate(false);
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

        // Service接口渲染数据
        renderServiceData(tableInfo, data, configResolver, globalConfig);

        // ServiceImpl渲染数据
        if (generateServiceImpl) {
            renderServiceImplData(tableInfo, data, configResolver, globalConfig);
        }

        return data;
    }

    private void renderServiceData(TableInfo tableInfo, Map<String, Object> data,
                                   ConfigResolver configResolver, GlobalConfig globalConfig) {
        Set<String> importPackages = new TreeSet<>();

        switch (globalConfig.getRuntimeEnv()) {
            case MY_BATIS_PLUS_SQL_BOOSTER:
                this.serviceSuperClass = RuntimeClass.SQL_BOOSTER_MP_SERVICE.getClassName();
                importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
                importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                if (globalConfig.isGenerateCreate()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.CREATE_DTO, tableInfo));
                }
                if (globalConfig.isGenerateUpdate()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.UPDATE_DTO, tableInfo));
                }
                if (globalConfig.isGenerateDelete()) {
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateVoById()) {
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateVoList()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                }
                if (globalConfig.isGenerateVoPage()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                    importPackages.add(RuntimeClass.SQL_BOOSTER_BOOSTER_PAGE.getClassName());
                }
                if (globalConfig.isGenerateImport()) {
                    importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getClassName());
                    importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
                }
                if (globalConfig.isGenerateExport()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                    importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
                }
                break;
            case MYBATIS_PLUS:
                this.serviceSuperClass = RuntimeClass.MYBATIS_PLUS_I_SERVICE.getClassName();
                importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
                if (globalConfig.isGenerateCreate()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.CREATE_DTO, tableInfo));
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateUpdate()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.UPDATE_DTO, tableInfo));
                }
                if (globalConfig.isGenerateDelete()) {
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateVoById()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateVoList()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                }
                if (globalConfig.isGenerateVoPage()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                    importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getClassName());
                }
                if (globalConfig.isGenerateImport()) {
                    importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getClassName());
                    importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
                }
                if (globalConfig.isGenerateExport()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                    importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
                }
                break;
            default:
                throw new RuntimeException("Unknown runtime environment:" + globalConfig.getRuntimeEnv());
        }

        data.put("serviceSuperClass", ClassUtils.getSimpleName(this.serviceSuperClass));
        if (serviceSuperClass != null) {
            importPackages.add(this.serviceSuperClass);
        }

        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("serviceFrameworkPkg", frameworkPackages);
        data.put("serviceJavaPkg", javaPackages);
    }

    private void renderServiceImplData(TableInfo tableInfo, Map<String, Object> data,
                                      ConfigResolver configResolver, GlobalConfig globalConfig) {
        Set<String> importPackages = new TreeSet<>();

        switch (globalConfig.getRuntimeEnv()) {
            case MY_BATIS_PLUS_SQL_BOOSTER:
                this.serviceImplSuperClass = RuntimeClass.SQL_BOOSTER_MP_SERVICE_IMPL.getClassName();
                importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                if (globalConfig.isGenerateVoById()) {
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                }
                if (globalConfig.isGenerateVoList()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                }
                if (globalConfig.isGenerateVoPage()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                    importPackages.add(RuntimeClass.SQL_BOOSTER_BOOSTER_PAGE.getClassName());
                }
                if (globalConfig.isGenerateExport()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                }
                break;
            case MYBATIS_PLUS:
                this.serviceImplSuperClass = RuntimeClass.MYBATIS_PLUS_SERVICE_IMPL.getClassName();
                if (globalConfig.isGenerateVoById()) {
                    importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                }
                if (globalConfig.isGenerateVoList()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                }
                if (globalConfig.isGenerateVoPage()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_VO, tableInfo));
                    importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getClassName());
                    importPackages.add(RuntimeClass.MYBATIS_PLUS_PAGE.getClassName());
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                }
                if (globalConfig.isGenerateExport()) {
                    importPackages.add(configResolver.getClassName(TemplateFileEnum.QUERY_DTO, tableInfo));
                }
                break;
            default:
                throw new RuntimeException("Unknown runtime environment:" + globalConfig.getRuntimeEnv());
        }

        if (serviceImplSuperClass != null) {
            importPackages.add(this.serviceImplSuperClass);
            data.put("serviceImplSuperClass", ClassUtils.getSimpleName(this.serviceImplSuperClass));
        }

        // 类注解及信息
        importPackages.add(RuntimeClass.SPRING_BOOT_SERVICE.getClassName());
        importPackages.add(configResolver.getClassName(TemplateFileEnum.MAPPER, tableInfo));
        importPackages.add(configResolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));

        if (configResolver.isGenerate(TemplateFileEnum.SERVICE, tableInfo)) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.SERVICE, tableInfo));
        }

        if (globalConfig.isGenerateCreate()) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.CREATE_DTO, tableInfo));
            importPackages.add(RuntimeClass.SPRING_BOOT_BEAN_UTILS.getClassName());
            importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
        }
        if (globalConfig.isGenerateUpdate()) {
            importPackages.add(configResolver.getClassName(TemplateFileEnum.UPDATE_DTO, tableInfo));
            importPackages.add(RuntimeClass.SPRING_BOOT_BEAN_UTILS.getClassName());
        }
        if (globalConfig.isGenerateDelete()) {
            importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
        }
        String excelMain = globalConfig.getExcelApi().getMainEntrance();
        String excelPackagePrefix = globalConfig.getExcelApi().getPackagePrefix();
        String excelClass = excelPackagePrefix + excelMain;
        String longestMatchColumnWidthStyleStrategyClass = excelPackagePrefix + RuntimeClass.PREFIX_EXCEL_LONGEST_MATCH_COLUMN_WIDTH_STYLE_STRATEGY.getClassName();
        if (globalConfig.isGenerateImport()) {
            importPackages.add(excelClass);
            importPackages.add(configResolver.getClassName(TemplateFileEnum.IMPORT_DTO, tableInfo));
            importPackages.add(longestMatchColumnWidthStyleStrategyClass);
            importPackages.add(RuntimeClass.JAVA_UTIL_COLLECTIONS.getClassName());
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
            importPackages.add(RuntimeClass.JAVA_IO_INPUT_STREAM.getClassName());
            importPackages.add(RuntimeClass.JAVA_STREAM_COLLECTORS.getClassName());
            importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
            importPackages.add(RuntimeClass.SPRING_BOOT_BEAN_UTILS.getClassName());
        }

        if (globalConfig.isGenerateExport()) {
            importPackages.add(excelClass);
            importPackages.add(configResolver.getClassName(TemplateFileEnum.EXPORT_DTO, tableInfo));
            importPackages.add(RuntimeClass.JAVA_IO_OUTPUT_STREAM.getClassName());
            importPackages.add(longestMatchColumnWidthStyleStrategyClass);
        }

        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));

        data.put("serviceImplFrameworkPkg", frameworkPackages);
        data.put("serviceImplJavaPkg", javaPackages);
    }

    @Override
    public void renderDataPostProcess(TableInfo tableInfo, Map<String, Object> renderData) {
    }

    @Override
    public List<TemplateFile> renderTemplateFiles() {
        List<TemplateFile> files = new ArrayList<>();
        files.add(serviceTemplateFile);
        files.add(serviceImplTemplateFile);
        return files;
    }
}
