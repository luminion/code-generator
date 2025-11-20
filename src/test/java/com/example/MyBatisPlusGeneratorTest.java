package com.example;

import io.github.luminion.generator.GeneratorHelper;
import io.github.luminion.generator.config.builder.special.MybatisPlusBuilder;
import io.github.luminion.generator.core.LambdaGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MyBatisPlusGeneratorTest {

    String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8";
    String username = "root";
    String password = "123456";
    // 所有测试用例将输出到此单一目录
    String outputDir = System.getProperty("user.dir") + "\\src\\test\\java";

    private LambdaGenerator<MybatisPlusBuilder> generator;

    @BeforeEach
    void setup() {
        generator = GeneratorHelper.mybatisPlusGenerator(url, username, password)
                .global(g -> g
                        .outputDir(outputDir)
                        .fileOverride(true)
                        .parentPackage("com.example.test")
                )
        ;
    }

    @AfterEach
    void excute() {
        generator.execute("sys_user");
    }


    /**
     * 基础测试: 最简单的配置, 用于验证基本功能.
     */
    @Test
    public void testBasic() {

    }


    @Test
    public void testGlobal1() {
        generator.global(g -> g
                .author("test-author")
                .parentPackageModule("usr")
        );
    }

    @Test
    public void testGlobal2() {
        generator.global(g -> g
                .author("test-author2")
                .parentPackageModule("usr2")
        );
    }


}
