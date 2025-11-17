package io.github.luminion.generator.config.model;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.Resolver;
import io.github.luminion.generator.config.core.GlobalConfig;
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
            "/templates/mybatisplus/mapper.java",
            ".java"
    );

    /**
     * 自定义继承的Mapper类全称，带包名
     */
    protected String superClass = "com.baomidou.mybatisplus.core.mapper.BaseMapper";

    /**
     * Mapper标记注解
     *
     */
//    protected Class<? extends Annotation> mapperAnnotationClass = org.apache.ibatis.annotations.Mapper.class;
    protected String mapperAnnotationClass = "org.apache.ibatis.annotations.Mapper";

    @Override
    @SneakyThrows
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        Set<String> importPackages = new TreeSet<>();

        Configurer configurer = tableInfo.getConfigurer();
        Resolver resolver = configurer.getResolver();
        GlobalConfig globalConfig = configurer.getGlobalConfig();
        importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY, tableInfo));
        switch (globalConfig.getRuntimeEnv()) {
            case MYBATIS_PLUS:
                if (globalConfig.isGenerateQuery()) {
                    importPackages.add(List.class.getName());
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_DTO, tableInfo));
                    importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
                    importPackages.add(globalConfig.getPageClassPayload().getClassName());
                }
                break;
            case SQL_BOOSTER_MY_BATIS_PLUS:
                this.superClass = "io.github.luminion.sqlbooster.extension.mybatisplus.BoosterMpMapper";
                importPackages.add(resolver.getClassName(TemplateFileEnum.ENTITY_QUERY_VO, tableInfo));
                if (globalConfig.isGenerateQuery()) {
                    importPackages.add("io.github.luminion.sqlbooster.model.api.Wrapper");
                    importPackages.add(List.class.getName());
                }
                break;
            default:
                throw new RuntimeException("未定义的运行环境");
        }
        if (mapperAnnotationClass != null) {
            data.put("mapperAnnotationClass", ClassUtils.getSimpleName(mapperAnnotationClass));
            importPackages.add(mapperAnnotationClass);
        }
        if (superClass != null) {
            importPackages.add(superClass);
            data.put("mapperSuperClass", ClassUtils.getSimpleName(this.superClass));
        }


        Collection<String> frameworkPackages = importPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> javaPackages = importPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        data.put("mapperFrameworkPkg", frameworkPackages);
        data.put("mapperJavaPkg", javaPackages);


        return data;
    }

}