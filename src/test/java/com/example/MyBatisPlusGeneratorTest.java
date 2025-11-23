package com.example;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import io.github.luminion.generator.GeneratorHelper;
import io.github.luminion.generator.config.builder.custom.MybatisPlusBuilder;
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

    //String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8";
    //String username = "root";
    //String password = "123456";
    String url = "jdbc:postgresql://localhost:5432/test?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
    String username = "postgres";
    String password = "123456";
    // 所有测试用例将输出到此单一目录
    String outputDir = System.getProperty("user.dir") + "\\src\\test\\java";

    private LambdaGenerator<MybatisPlusBuilder> generator;

    @BeforeEach
    void setup() {
        generator = GeneratorHelper.mybatisPlusGenerator(url, username, password)
                //.initialize()
                .global(g -> g
                        .outputDir(outputDir)
                        .fileOverride(true)
                        .javaEEApi(JavaEEApi.JAVAX)
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

    @Test
    public void testBasic() {

    }


    @Test
    public void testJakarta() {
        generator.global(g -> g
                .javaEEApi(JavaEEApi.JAKARTA)
                .openOutputDir(true)
        );
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
                .excelApi(ExcelApi.FAST_EXCEL)
                .outputDir(outputDir)
                //.openOutputDir(true)
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
                .excelApi(ExcelApi.EASY_EXCEL)
                //.openOutputDir(false)
                //.fileOverride(false)
                .parentPackageModule("module2")
                .validated(false)
        );
    }

    @Test
    public void testGlobalMyBatisPlus3() {
        generator.global(g -> g
                        .lombok(false)
                        .chainModel(true)
                        .serializableUID(false)
                        .serializableAnnotation(true)
                        .docType(DocType.SWAGGER)
                        .docLink(true)
                        .author("author3")
                        .date("yyyy/MM/dd HH:mm:ss")
                        .excelApi(ExcelApi.EASY_EXCEL)
                        //.openOutputDir(false)
                        //.fileOverride(false)
                        //.parentPackageModule("module2")
                        //.validated(false)
                        .validated(true)
                
                )
                .custom(c -> c
                        .idType(IdType.ASSIGN_ID)
                        .logicDeleteColumnName("deleted")
                        .versionColumnName("version")
                        .activeRecord(true)
                        .tableFieldAnnotation(true)
                        .tableFill("create_time", FieldFill.INSERT)
                        .tableFill("update_time", FieldFill.UPDATE)
                        .tableFill("age", FieldFill.INSERT_UPDATE)
                )

        ;
    }
}
