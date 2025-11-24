package com.example;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.example.common.P;
import com.example.common.R;
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
public class MyBatisPlusSqlBoosterGeneratorTest {

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
        generator = GeneratorHelper.mybatisPlusSqlBoosterGenerator(url, username, password)
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
    public void test() {
        generator.global(g -> g
                        .lombok(false)
                        .chainModel(false)
                        .serializableUID(true)
                        .serializableAnnotation(true)
                        .docType(DocType.SWAGGER_V2)
                        .docLink(true)
                        //.author("author3")
                        //.date("yyyy/MM/dd HH:mm:ss")
                        .excelApi(ExcelApi.EASY_EXCEL)
                        //.openOutputDir(false)
                        //.fileOverride(false)
                        //.parentPackageModule("module2")
                        .validated(true)
                        //.generateQuery(false)
                        //.generateCreate(false)
                        //.generateUpdate(false)
                        //.generateDelete(false)
                )
                .controller(c -> c
                        .returnMethod(R::of)
                        //.pageMethod(P::of, PageInfo.class)
                        .pathVariable(true)
                        .crossOrigin(true)
                        .restful(true)
                        .requestBody(true)
                        .batchQueryPost(true)
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
                        .pageMethod(P::of)
                )
                .queryDTO(q -> q
                        .extendsEntity(false)        
                )
                .queryVO(q -> q
                        .extendsEntity(false)
                )

        ;
    }
}
