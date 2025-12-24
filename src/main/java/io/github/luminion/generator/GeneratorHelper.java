package io.github.luminion.generator;

import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.custom.MybatisPlusConfig;
import io.github.luminion.generator.core.suppport.MyBatisPlusGenerator;
import io.github.luminion.generator.core.suppport.MyBatisPlusSqlBoosterGenerator;

/**
 * 代码生成器辅助类
 * <p>
 * 这个类提供了一系列静态方法，用于快速创建和配置不同类型的代码生成器。
 * 它简化了生成器的实例化过程，隐藏了底层的配置细节，使得用户可以更方便地使用生成器功能。
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
    public static MyBatisPlusGenerator mybatisPlus(String url, String username, String password) {
        return new MyBatisPlusGenerator(new ConfigCollector<>(url, username, password, new MybatisPlusConfig()));
    }
    
    /**
     * 创建一个SQL-Booster代码生成器。
     *
     * @param url      数据库连接URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return SQL-Booster代码生成器实例
     */
    public static MyBatisPlusSqlBoosterGenerator mybatisPlusBooster(String url, String username, String password) {
        return new MyBatisPlusSqlBoosterGenerator(new ConfigCollector<>(url, username, password,new MybatisPlusConfig()));
    }
    
}
