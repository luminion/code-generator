package io.github.luminion.generator;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.GenerationMode;
import io.github.luminion.generator.util.InitializeUtils;

/**
 * 代码生成器助手
 *
 * @author luminion
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class GeneratorHelper {

    /**
     * 创建一个MyBatis代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return MyBatis代码生成器实例
     */
    public static LambdaGenerator mybatis(String url, String username, String password) {
        return create(url, username, password, GenerationMode.MYBATIS);
    }

    /**
     * 创建一个MyBatis-PageHelper代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return MyBatis-PageHelper代码生成器实例
     */
    public static LambdaGenerator mybatisPageHelper(String url, String username, String password) {
        return create(url, username, password, GenerationMode.MYBATIS_PAGE_HELPER);
    }

    /**
     * 创建一个MyBatis-Plus代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return MyBatis-Plus代码生成器实例
     */
    public static LambdaGenerator mybatisPlus(String url, String username, String password) {
        return create(url, username, password, GenerationMode.MYBATIS_PLUS);
    }

    /**
     * 创建一个MyBatis-SqlBooster增强封装代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return SQL-Booster代码生成器实例
     */
    public static LambdaGenerator mybatisSqlBooster(String url, String username, String password) {
        return create(url, username, password, GenerationMode.MYBATIS_SQL_BOOSTER);
    }

    /**
     * 创建一个MyBatis-SqlBooster解耦代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return SQL-Booster代码生成器实例
     */
    public static LambdaGenerator mybatisSqlBoosterContext(String url, String username, String password) {
        return create(url, username, password, GenerationMode.MYBATIS_SQL_BOOSTER_CONTEXT);
    }

    /**
     * 创建一个MyBatis-PageHelper-SqlBooster解耦代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return SQL-Booster代码生成器实例
     */
    public static LambdaGenerator mybatisPageHelperSqlBoosterContext(String url, String username, String password) {
        return create(url, username, password, GenerationMode.MYBATIS_PAGE_HELPER_SQL_BOOSTER_CONTEXT);
    }

    /**
     * 创建一个MyBatis-Plus-SqlBooster代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return SQL-Booster代码生成器实例
     */
    public static LambdaGenerator mybatisPlusSqlBooster(String url, String username, String password) {
        return create(url, username, password, GenerationMode.MYBATIS_PLUS_SQL_BOOSTER);
    }

    /**
     * 创建一个MyBatis-Plus-SqlBooster解耦代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return SQL-Booster代码生成器实例
     */
    public static LambdaGenerator mybatisPlusSqlBoosterContext(String url, String username, String password) {
        return create(url, username, password, GenerationMode.MYBATIS_PLUS_SQL_BOOSTER_CONTEXT);
    }

    /**
     * 根据一键生成模式创建代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @param mode     一键生成模式
     * @return 代码生成器实例
     */
    public static LambdaGenerator create(String url, String username, String password, GenerationMode mode) {
        Configurer configurer = createConfigurer(url, username, password);
        mode.initialize(configurer);
        return new LambdaGenerator(configurer);
    }

    private static Configurer createConfigurer(String url, String username, String password) {
        Configurer configurer = new Configurer(url, username, password);
        InitializeUtils.initJdbcTypeConverter(configurer);
        InitializeUtils.initializeMapperOrderColumn(configurer);
        InitializeUtils.initializeCommandExcludeColumn(configurer);
        return configurer;
    }

}
