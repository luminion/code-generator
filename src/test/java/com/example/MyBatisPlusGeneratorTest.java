package com.example;

import io.github.luminion.generator.GeneratorHelper;
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

    /**
     * 基础测试: 最简单的配置, 用于验证基本功能.
     */
    @Test
    public void testBasic() {
        GeneratorHelper.mybatisPlusGenerator(url, username, password)
                .global(g -> g.outputDir(outputDir).fileOverride(true).parentPackage("com.example.test"))
                .execute("sys_user");
    }

    ///**
    // * 全局配置测试: 重点测试包名、作者、文档和Lombok等全局设置.
    // */
    //@Test
    //public void testGlobalConfig() {
    //    GeneratorHelper.mybatisPlusGenerator(url, username, password)
    //            .global(g -> g.outputDir(outputDir)
    //                    .fileOverride(true)
    //                    .author("TestAuthor")
    //                    .docType(DocType.SWAGGER)
    //                    .parentPackage("com.example.project")
    //                    .parentPackageModule("user")
    //                    .lombok(true)
    //                    .chainModel(true))
    //            .strategy(s -> s.include("sys_user"))
    //            .execute();
    //}
    //
    ///**
    // * 命名策略测试: 测试表前缀移除和命名转换策略.
    // */
    //@Test
    //public void testNamingStrategy() {
    //    GeneratorHelper.mybatisPlusGenerator(url, username, password)
    //            .global(g -> g.outputDir(outputDir).fileOverride(true))
    //            .strategy(s -> s.include("sys_user", "t_order")
    //                    .tablePrefix("t_") // 移除 't_' 前缀
    //                    .fieldPrefix("f_") // 移除 'f_' 字段前缀
    //            )
    //            .execute();
    //}
    //
    ///**
    // * 实体与MP特性测试: 测试逻辑删除、乐观锁和主键策略.
    // */
    //@Test
    //public void testEntityFeatures() {
    //    MyBatisPlusGenerator generator = GeneratorHelper.mybatisPlusGenerator(url, username, password);
    //    generator.global(g -> g.outputDir(outputDir).fileOverride(true))
    //            .strategy(s -> s.include("sys_user"))
    //            .special(mp -> mp
    //                    .strategyIdType(IdType.ASSIGN_ID) // 雪花算法ID
    //                    .strategyLogicDeleteColumnName("deleted") // 逻辑删除字段
    //                    .strategyVersionColumnName("version") // 乐观锁字段
    //                    .entityActiveRecord(true) // 开启ActiveRecord模式
    //            )
    //            .execute();
    //}
    //
    ///**
    // * 模型自定义测试: 测试自定义父类、包名和Controller风格.
    // */
    //@Test
    //public void testModelCustomization() {
    //    GeneratorHelper.mybatisPlusGenerator(url, username, password)
    //            .global(g -> g.outputDir(outputDir)
    //                    .fileOverride(true)
    //                    .parentPackage("com.example.custom"))
    //            .strategy(s -> s.include("sys_user"))
    //            .controller(c -> c.superClass("com.example.custom.BaseController")
    //                    .restful(true) // 使用 @GetMapping 等
    //                    .nameFormat("%sApi")) // 将 Controller 重命名为 Api
    //            .service(s -> s.superClass("com.example.custom.BaseService"))
    //            .entity(e -> e.superClass("com.example.custom.BaseEntity"))
    //            .execute();
    //}
    //
    ///**
    // * Spring Boot 3+ (Jakarta) 兼容性测试.
    // */
    //@Test
    //public void testSpringBoot3Plus() {
    //    GeneratorHelper.mybatisPlusGenerator(url, username, password)
    //            .global(g -> g.outputDir(outputDir)
    //                    .fileOverride(true)
    //                    .javaEEApi(JavaEEApi.JAKARTA)) // 使用 jakarta.*
    //            .strategy(s -> s.include("sys_user"))
    //            .execute();
    //}

}
