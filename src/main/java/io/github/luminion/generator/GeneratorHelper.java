package io.github.luminion.generator;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.builder.special.MybatisPlusSqlBoosterBuilder;
import io.github.luminion.generator.core.suppport.MyBatisPlusGenerator;

/**
 * 代码生成器辅助类
 * <p>
 * 这个类提供了一系列静态方法，用于快速创建和配置不同类型的代码生成器。
 * 它简化了生成器的实例化过程，隐藏了底层的配置细节，使得用户可以更方便地
 * 使用生成器功能。
 *
 * <p>主要功能包括：
 * <ul>
 *     <li>创建基于MyBatis-Plus的代码生成器。</li>
 *     <li>创建基于MyBatis-Plus和SQL-Booster的代码生成器。</li>
 *     <li>支持通过数据库连接信息（URL、用户名、密码）和可选的schema名称来初始化生成器。</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>{@code
 * // 创建一个MyBatis-Plus代码生成器
 * MyBatisPlusGenerator generator = GeneratorHelper.mybatisPlusGenerator("jdbc:mysql://localhost:3306/test", "root", "password");
 *
 * // 配置并执行生成
 * generator.global(global -> global.outputDir("/path/to/output"))
 *          .strategy(strategy -> strategy.addInclude("user", "order"))
 *          .execute();
 * }</pre>
 *
 * @author luminion
 * @since 1.0.0
 */
public class GeneratorHelper {

    /**
     * 创建一个MyBatis-Plus代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return MyBatis-Plus代码生成器实例
     */
    public static MyBatisPlusGenerator mybatisPlusGenerator(String url, String username, String password) {
        return new MyBatisPlusGenerator(new Configurer(url, username, password));
    }

    /**
     * 创建一个带schema的MyBatis-Plus代码生成器。
     *
     * @param url        数据库连接URL
     * @param username   数据库用户名
     * @param password   数据库密码
     * @param schemaName 数据库schema名称
     * @return MyBatis-Plus代码生成器实例
     */
    public static MyBatisPlusGenerator mybatisPlusGenerator(String url, String username, String password, String schemaName) {
        return new MyBatisPlusGenerator(new Configurer(url, username, password, schemaName));
    }

    /**
     * 创建一个MyBatis-Plus和SQL-Booster结合的代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return MybatisPlusSqlBoosterBuilder实例，用于进一步配置
     */
    public static MybatisPlusSqlBoosterBuilder mybatisPlusSqlBoosterGenerator(String url, String username, String password) {
        return new MybatisPlusSqlBoosterBuilder(new Configurer(url, username, password));
    }

    /**
     * 创建一个带schema的MyBatis-Plus和SQL-Booster结合的代码生成器。
     *
     * @param url        数据库连接URL
     * @param username   数据库用户名
     * @param password   数据库密码
     * @param schemaName 数据库schema名称
     * @return MybatisPlusSqlBoosterBuilder实例，用于进一步配置
     */
    public static MybatisPlusSqlBoosterBuilder mybatisPlusSqlBoosterGenerator(String url, String username, String password, String schemaName) {
        return new MybatisPlusSqlBoosterBuilder(new Configurer(url, username, password, schemaName));
    }

}
