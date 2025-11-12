//package io.github.luminion.generator.config.builder;
//
//import io.github.luminion.generator.config.base.DataSourceConfig;
//import io.github.luminion.generator.util.ClassUtils;
//import lombok.SneakyThrows;
//
///**
// * @author luminion
// * @since 1.0.0
// */
//public class DataSourceBuilder {
//    private final DataSourceConfig dataSourceConfig;
//
//    public DataSourceBuilder(DataSourceConfig config) {
//        this.dataSourceConfig = config;
//    }
//
//    /**
//     * 设置数据库schema
//     *
//     * @param schemaName 数据库schema
//     * @return this
//     */
//    public DataSourceBuilder schema(String schemaName) {
//        this.dataSourceConfig.setSchemaName(schemaName);
//        return this;
//    }
//
//    /**
//     * 增加数据库连接属性
//     *
//     * @param key   属性名
//     * @param value 属性值
//     * @return this
//     * @since 3.5.3
//     */
//    public DataSourceBuilder addConnectionProperty(String key, String value) {
//        this.dataSourceConfig.getConnectionProperties().put(key, value);
//        return this;
//    }
//
//    /**
//     * 指定连接驱动
//     * <li>对于一些老驱动(低于4.0规范)没有实现SPI不能自动加载的,手动指定加载让其初始化注册到驱动列表去.</li>
//     *
//     * @param className 驱动全类名
//     * @return this
//     * @since 3.5.8
//     */
//    @SneakyThrows
//    public DataSourceBuilder driverClassName(String className) {
//        ClassUtils.toClass(className);
//        return this;
//    }
//
//}
