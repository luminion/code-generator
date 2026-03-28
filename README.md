# code-generator

[![Maven Central](https://img.shields.io/maven-central/v/io.github.luminion/code-generator)](https://mvnrepository.com/artifact/io.github.luminion/code-generator)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

一个基于 Lambda 链式配置风格的后端代码生成器，面向 MyBatis-Plus / SQL-Booster 场景，支持生成实体、Mapper、Service、Controller、命令参数、查询参数、查询结果、Excel 导入导出模型等常见代码。

旧版地址: https://github.com/bootystar/mybatis-plus-generator

## 功能特性

- 生成 `Entity`、`Mapper`、`Service`、`Controller` 等基础代码
- 生成 `CreateDTO`、`UpdateDTO`、`QueryDTO`、`VO`、Excel 导入导出 DTO
- 支持 CRUD、分页查询、列表查询、Excel 导入导出
- 支持参数校验、文档注释、命名转换、字段自动填充
- 支持 Velocity 模板扩展和自定义渲染数据
- 保持用户入口稳定，内部职责按配置、元数据、渲染、模板解析拆分

## Maven

正式版本：

```xml
<dependency>
    <groupId>io.github.luminion</groupId>
    <artifactId>code-generator</artifactId>
    <version>latest</version>
    <scope>test</scope>
</dependency>
```

如需快照版本，可额外添加 Sonatype snapshot 仓库：

```xml
<repositories>
    <repository>
        <name>Central Portal Snapshots</name>
        <id>central-portal-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

## 快速开始

### 1. 最小示例

```java
import io.github.luminion.generator.GeneratorHelper;

public class CodegenApp {
    public static void main(String[] args) {
        GeneratorHelper.mybatisPlus(
                        "jdbc:mysql://localhost:3306/your_database",
                        "username",
                        "password"
                )
                .execute("user", "role");
    }
}
```

默认输出目录为当前项目的 `src/main/java`。

### 2. SQL-Booster 场景

```java
import io.github.luminion.generator.GeneratorHelper;

public class SqlBoosterCodegenApp {
    public static void main(String[] args) {
        GeneratorHelper.mybatisPlusSqlBooster(
                        "jdbc:mysql://localhost:3306/your_database",
                        "username",
                        "password"
                )
                .execute("user", "role");
    }
}
```

### 3. 常用完整配置

```java
GeneratorHelper.mybatisPlus(
                "jdbc:mysql://localhost:3306/your_database",
                "username",
                "password"
        )
        .global(g -> g
                .author("luminion")
                .date("yyyy-MM-dd")
                .docType(DocType.SPRING_DOC)
                .javaEEApi(JavaEEApi.JAKARTA)
                .disableLombok()
                .enableChainSetter()
                .enableToString()
                .disableSerializable()
                .skipQueryPage()
                .skipExcelImport()
        )
        .dataSource(d -> d
                .schema("public")
                .dateType(DateType.TIME_PACK)
                .namingConverter(NamingConverter.UNDERLINE_TO_CAMEL)
                .enableSkipView()
                .enableBooleanColumnRemoveIsPrefix()
                .includeTables("user", "role")
                .excludeTables("sys_log")
                .tablePrefixes("sys_", "tbl_")
                .tableSuffixes("_tb")
                .columnPrefixes("f_")
                .columnSuffixes("_flag")
                .commonColumns("create_time", "update_time")
                .ignoreColumns("deleted")
        )
        .template(t -> t
                .outputDir("D:\\Project\\src\\main\\java")
                .parentPackage("com.example.project")
                .parentModule("module-name")
                .enableFileOverride()
        )
        .entity(e -> e
                .superClass(BaseEntity.class)
                .idType(IdType.ASSIGN_ID)
                .enableActiveRecord()
                .enableTableFieldAnnotation()
                .versionColumnName("version")
                .logicDeleteColumnName("deleted")
                .columnFill("create_time", ColumnFillStrategy.INSERT)
                .columnFill("update_time", ColumnFillStrategy.INSERT_UPDATE)
        )
        .service(s -> s
                .serviceSuperClass(BaseService.class)
                .serviceImplSuperClass(BaseServiceImpl.class)
        )
        .mapper(m -> m
                .superClass(BaseMapper.class)
                .mapperAnnotationClass(org.apache.ibatis.annotations.Mapper.class)
                .enableBaseColumnList()
                .enableBaseResultMap()
                .appendOrderColumn("sort", true)
        )
        .controller(c -> c
                .superClass(BaseController.class)
                .baseUrl("/api/v1")
                .enableRestful()
                .enableCrossOrigin()
                .disablePathVariable()
                .disableRequestBody()
                .enableQueryViaPost()
                .returnMethod("com.example.common.Result", "Result<%s>", "Result::success")
                .pageMethod("com.example.common.PageResult", "PageResult<%s>", "PageResult::success")
        )
        .command(cmd -> cmd
                .disableValid()
                .createMethodName("create")
                .updateMethodName("update")
                .deleteMethodName("delete")
                .excludeColumns("create_time", "create_by")
        )
        .query(q -> q
                .queryByIdMethodName("getById")
                .queryListMethodName("list")
                .queryPageMethodName("page")
                .appendExtraFieldSuffix("Like", "LIKE")
                .appendExtraFieldSuffix("In", "IN")
                .pageParamName("page")
                .sizeParamName("size")
                .queryParamSuperClass(BaseQueryParam.class)
                .queryResultSuperClass(BaseQueryResult.class)
        )
        .excel(ex -> ex
                .api(ExcelApi.EASY_EXCEL)
                .excelImportMethodName("importExcel")
                .excelExportMethodName("exportExcel")
        )
        .execute("user", "role");
```

## 默认行为与约定

### 无主键表的处理

- `create` 相关代码仍会生成
- 如果表没有主键，生成的 `create` 方法返回 `Boolean`
- 依赖主键的能力会自动跳过，并输出日志说明原因
- 常见跳过项包括：`UpdateParam`、按主键更新、按主键删除、`queryById`

### 模板按需生成

- 如果没有功能会使用 `QueryDTO` / `VO` / Excel DTO，这些文件会自动跳过
- 跳过时会记录明确的 skip reason，便于用户排查为什么某个文件没有生成

### 入口保持稳定

用户层建议只关注这些入口：

- `GeneratorHelper`
- `LambdaGenerator`
- `builder/*Builder`

内部结构说明见 [ARCHITECTURE.md](ARCHITECTURE.md)。

## 配置概览

| 配置段 | 作用 | 常用方法 |
| --- | --- | --- |
| `global()` | 全局风格和开关 | `author` `docType` `javaEEApi` `skipCreate/Update/Delete/...` |
| `dataSource()` | 数据源与表字段过滤 | `schema` `dateType` `includeTables` `excludeTables` `enableSkipView` |
| `template()` | 输出目录、父包、模板文件定义 | `outputDir` `parentPackage` `parentModule` `enableFileOverride` |
| `entity()` | 实体生成配置 | `superClass` `idType` `enableActiveRecord` `logicDeleteColumnName` |
| `service()` | Service / ServiceImpl 继承关系 | `serviceSuperClass` `serviceImplSuperClass` |
| `mapper()` | Mapper 配置 | `superClass` `mapperAnnotationClass` `enableBaseResultMap` |
| `controller()` | API 风格、返回包装、分页包装 | `superClass` `baseUrl` `returnMethod` `pageMethod` |
| `command()` | Create / Update / Delete 命名与校验 | `createMethodName` `updateMethodName` `excludeColumns` |
| `query()` | QueryDTO / VO 与额外查询字段 | `appendExtraFieldSuffix` `pageParamName` `queryParamSuperClass` |
| `excel()` | Excel 相关模型与方法命名 | `api` `excelImportMethodName` `excelExportMethodName` |

## 模板文件配置

`template()` 下可以单独覆盖每个模板文件的名称格式、子包、模板路径和输出目录，例如：

```java
.template(t -> t
    .entity(e -> e
        .nameFormat("%sEntity")
        .subPackage("model.entity")
        .templatePath("/templates/entity.java")
        .outputFileSuffix(".java")
        .enableFileOverride()
        .disable()
    )
)
```

常用方法：

- `nameFormat(String)`
- `subPackage(String)`
- `templatePath(String)`
- `outputFileSuffix(String)`
- `enableFileOverride()`
- `disable()`
- `fileOutputDir(String)`

## String / Class 双写法

下面这些常用配置支持直接传全限定类名字符串，也支持直接传 `Class<?>`：

- `entity().superClass(...)`
- `mapper().superClass(...)`
- `mapper().mapperAnnotationClass(...)`
- `controller().superClass(...)`
- `service().serviceSuperClass(...)`
- `service().serviceImplSuperClass(...)`
- `query().queryParamSuperClass(...)`
- `query().queryResultSuperClass(...)`

优先建议使用 `Class<?>`，可读性更高，也更不容易写错包名。

## 维护说明

- 想改默认配置：看 `config/*Config`
- 想改链式 API：看 `builder/*Builder`
- 想改文件生成开关或跳过逻辑：看 `internal/render/TemplateGenerationPlanner`
- 想改模板文件解析和输出路径：看 `internal/template/TemplateFileResolver`
- 想改数据库字段映射：看 `internal/schema/JdbcTableFieldMapper`

## 注意事项

1. 开启文件覆盖后，会覆盖同名文件，请提前确认输出目录
2. 需要保证数据库连接可用，并且当前账号有读取元数据权限
3. 自定义模板前，建议先阅读 `ARCHITECTURE.md` 了解渲染链路
4. 若某些文件没有生成，优先查看控制台日志中的 skip reason

## 声明

该项目中部分代码源自 [MyBatis-Plus](https://github.com/baomidou/mybatis-plus) 项目，相关文件已保留原始版权信息。