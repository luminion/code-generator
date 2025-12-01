package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.Resolver;
import io.github.luminion.generator.config.core.GlobalConfig;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mapper属性配置
 *
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
@Data
public class MapperConfig implements TemplateRender {
    /**
     * 模板文件
     */
    protected TemplateFile templateFile = new TemplateFile(
            TemplateFileEnum.MAPPER.getKey(),
            "%sMapper",
            "mapper",
            "/templates/mybatis_plus/mapper.java",
            ".java"
    );

    /**
     * 自定义继承的Mapper类全称，带包名
     */
    protected String superClass;

    /**
     * Mapper标记注解
     *
     */
//    protected Class<? extends Annotation> mapperAnnotationClass = org.apache.ibatis.annotations.Mapper.class;
    protected String mapperAnnotationClass = "org.apache.ibatis.annotations.Mapper";

    @Override
    public List<TemplateFile> renderTemplateFiles() {
        return Collections.singletonList(templateFile);
    }


    @Override
    @SneakyThrows
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        Set<String> importPackages = new TreeSet<>();

        Resolver resolver = tableInfo.getResolver();
        Configurer<?> configurer = resolver.getConfigurer();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        
        importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
        switch (globalConfig.getRuntimeEnv()) {
            case MYBATIS_PLUS:
                this.superClass = RuntimeClass.MYBATIS_PLUS_BASE_MAPPER.getClassName();
                if (globalConfig.isGenerateQuery()) {
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_DTO, tableInfo));
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
                    importPackages.add(RuntimeClass.MYBATIS_PLUS_I_PAGE.getClassName());
                }
                break;
            case MY_BATIS_PLUS_SQL_BOOSTER:
                this.superClass = RuntimeClass.SQL_BOOSTER_MP_MAPPER.getClassName();
                importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
                if (globalConfig.isGenerateQuery()) {
                    importPackages.add(RuntimeClass.SQL_BOOSTER_SQL_CONTEXT.getClassName());
                    importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());
                }
                break;
            default:
                throw new RuntimeException("Unknown runtime environment:" + globalConfig.getRuntimeEnv());
        }
        if (mapperAnnotationClass != null) {
            data.put("mapperAnnotationClass","@"+ ClassUtils.getSimpleName(mapperAnnotationClass));
            importPackages.add(mapperAnnotationClass);
        }
        if (superClass != null) {
            importPackages.add(superClass);
            data.put("mapperSuperClass", ClassUtils.getSimpleName(this.superClass));
        }

        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        
        data.put("mapperFrameworkPkg", frameworkPackages);
        data.put("mapperJavaPkg", javaPackages);


        return data;
    }

}