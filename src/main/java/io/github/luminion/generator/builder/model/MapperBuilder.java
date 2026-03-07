package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.config.model.MapperConfig;

/**
 * Mapper接口配置构建器
 * <p>
 * 用于配置Mapper接口的生成选项：
 * <ul>
 *   <li>继承父类</li>
 *   <li>Mapper注解类</li>
 * </ul>
 * 继承自AbstractModelBuilder，提供通用配置方法
 *
 * @author luminion
 * @since 1.0.0
 */
public class MapperBuilder extends AbstractModelBuilder<MapperConfig, MapperBuilder> {


    public MapperBuilder(MapperConfig render) {
        super(render);
    }

    /**
     * 自定义继承的Mapper类全称，带包名
     *
     * @param superClass 父类
     * @return this
     */
    public MapperBuilder superClass(String superClass) {
        this.config.setSuperClass(superClass);
        return this;
    }

    /**
     * Mapper标记注解
     *
     * @param mapperAnnotationClass 注解类
     * @return this
     */
    public MapperBuilder mapperAnnotationClass(String mapperAnnotationClass) {
        this.config.setMapperAnnotationClass(mapperAnnotationClass);
        return this;
    }
}
