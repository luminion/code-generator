package com.example;

import io.github.luminion.generator.GeneratorHelper;
import io.github.luminion.generator.config.builder.special.MybatisPlusBuilder;
import io.github.luminion.generator.core.LambdaGenerator;
import io.github.luminion.generator.enums.DocType;
import io.github.luminion.generator.enums.ExcelApi;
import io.github.luminion.generator.enums.JavaEEApi;
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
                        .generateExport(true)
                        .generateImport(true)
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
                .lombok(true)
                .chainModel(true)
                .serializableUID(true)
                .serializableAnnotation(false)
                .docType(DocType.SPRING_DOC)
                .docLink(true)
                .author("author1")
                .date("yyyy-MM-dd")
                .javaEEApi(JavaEEApi.JAKARTA)
                .excelApi(ExcelApi.FAST_EXCEL)
                .outputDir(outputDir)
                .openOutputDir(true)
                //.fileOverride(true)
                //.parentPackage("com.example.test1")
                .parentPackageModule("module1")
                .validated(true)
        );
    }

    @Test
    public void testGlobal2() {
        generator.global(g -> g
                .lombok(false)
                .chainModel(false)
                .serializableUID(false)
                .serializableAnnotation(true)
                .docType(DocType.SWAGGER)
                .docLink(false)
                .author("author2")
                .date("yyyy/MM/dd HH:mm:ss")
                .javaEEApi(JavaEEApi.JAVAX)
                .excelApi(ExcelApi.EASY_EXCEL)
                .openOutputDir(false)
                //.fileOverride(false)
                .parentPackageModule("module2")
                .validated(false)
        );
    }
}
