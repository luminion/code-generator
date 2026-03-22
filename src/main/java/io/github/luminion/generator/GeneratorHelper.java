package io.github.luminion.generator;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.util.InitializeUtils;

/**
 * 代码生成器助手
 *
 * @author luminion
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class GeneratorHelper {

    /**
     * 创建一个MyBatis-Plus代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return MyBatis-Plus代码生成器实例
     */
    public static LambdaGenerator mybatisPlus(String url, String username, String password) {
        Configurer configurer = new Configurer(url, username, password);
        InitializeUtils.initJdbcTypeConverter(configurer);
        InitializeUtils.initializeMapperOrderColumn(configurer);
        InitializeUtils.initializeCommandExcludeColumn(configurer);
        InitializeUtils.initializeSuggestedExtraFieldSuffix(configurer);// 建议后缀
        InitializeUtils.initializeMybatisPlus(configurer);
        return new LambdaGenerator(configurer);
    }

    /**
     * 创建一个MyBatis-Plus-SqlBooster代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return SQL-Booster代码生成器实例
     */
    public static LambdaGenerator mpBooster(String url, String username, String password) {
        Configurer configurer = new Configurer(url, username, password);
        InitializeUtils.initJdbcTypeConverter(configurer);
        InitializeUtils.initializeMapperOrderColumn(configurer);
        InitializeUtils.initializeCommandExcludeColumn(configurer);
        InitializeUtils.initializeSupportedExtraFieldSuffix(configurer);// 所有后缀
        InitializeUtils.initializeMpBooster(configurer);
        return new LambdaGenerator(configurer);
    }


    public static void main(String[] args) {
        GeneratorHelper.mpBooster("", "", "")
                .global(g->g.docAuthor("aaa"))
                .dataSource(d->d.includeTables("a"))
                .template(t->t.controller(c->c.getClass()))
        ;
    }

}
