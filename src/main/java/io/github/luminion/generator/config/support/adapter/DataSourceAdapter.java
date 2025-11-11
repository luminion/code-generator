package io.github.luminion.generator.config.support.adapter;

import io.github.luminion.generator.config.common.IDbQuery;
import io.github.luminion.generator.config.common.IKeyWordsHandler;
import io.github.luminion.generator.config.common.ITypeConvert;
import io.github.luminion.generator.config.support.DataSourceConfig;
import io.github.luminion.generator.query.AbstractDatabaseQuery;
import io.github.luminion.generator.type.ITypeConvertHandler;
import lombok.SneakyThrows;

/**
 * @author luminion
 * @since 1.0.0
 */
public class DataSourceAdapter {
    private final DataSourceConfig dataSourceConfig;

    public DataSourceAdapter(DataSourceConfig config) {
        this.dataSourceConfig = config;
    }

    /**
     * 设置数据库查询实现
     *
     * @param dbQuery 数据库查询实现
     * @return this
     */
    public DataSourceAdapter dbQuery(IDbQuery dbQuery) {
        this.dataSourceConfig.setDbQuery(dbQuery);
        return this;
    }

    /**
     * 设置数据库schema
     *
     * @param schemaName 数据库schema
     * @return this
     */
    public DataSourceAdapter schema(String schemaName) {
        this.dataSourceConfig.setSchemaName(schemaName);
        return this;
    }

    /**
     * 设置类型转换器
     *
     * @param typeConvert 类型转换器
     * @return this
     */
    public DataSourceAdapter typeConvert(ITypeConvert typeConvert) {
        this.dataSourceConfig.setTypeConvert(typeConvert);
        return this;
    }

    /**
     * 设置数据库关键字处理器
     *
     * @param keyWordsHandler 关键字处理器
     * @return this
     */
    public DataSourceAdapter keyWordsHandler(IKeyWordsHandler keyWordsHandler) {
        this.dataSourceConfig.setKeyWordsHandler(keyWordsHandler);
        return this;
    }

    /**
     * 指定数据库查询方式
     *
     * @param databaseQueryClass 查询类
     * @return this
     * @since 3.5.3
     */
    public DataSourceAdapter databaseQueryClass(Class<? extends AbstractDatabaseQuery> databaseQueryClass) {
        this.dataSourceConfig.setDatabaseQueryClass(databaseQueryClass);
        return this;
    }

    /**
     * 指定类型转换器
     *
     * @param typeConvertHandler 类型转换器
     * @return this
     * @since 3.5.3
     */
    public DataSourceAdapter typeConvertHandler(ITypeConvertHandler typeConvertHandler) {
        this.dataSourceConfig.setTypeConvertHandler(typeConvertHandler);
        return this;
    }

    /**
     * 增加数据库连接属性
     *
     * @param key   属性名
     * @param value 属性值
     * @return this
     * @since 3.5.3
     */
    public DataSourceAdapter addConnectionProperty(String key, String value) {
        this.dataSourceConfig.getConnectionProperties().put(key, value);
        return this;
    }

    /**
     * 指定连接驱动
     * <li>对于一些老驱动(低于4.0规范)没有实现SPI不能自动加载的,手动指定加载让其初始化注册到驱动列表去.</li>
     *
     * @param className 驱动全类名
     * @return this
     * @since 3.5.8
     */
    @SneakyThrows
    public DataSourceAdapter driverClassName(String className) {
        Class.forName(className);
        this.dataSourceConfig.setDriverClassName(className);
        return this;
    }

}
