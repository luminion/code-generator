# code-generator

[![Maven Central](https://img.shields.io/maven-central/v/io.github.luminion/code-generator)](https://mvnrepository.com/artifact/io.github.luminion/code-generator)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

面向 MyBatis-Plus / SQL-Booster 场景的后端代码生成器。

支持生成：

- `Entity`
- `Mapper` / `Mapper.xml`
- `Service` / `ServiceImpl`
- `Controller`
- `CreateDTO` / `UpdateDTO`
- `QueryDTO` / `VO`
- `ExcelImportDTO` / `ExcelExportDTO`

## 功能特性

- Lambda 链式调用
- 同时支持 `MyBatis-Plus` 与 `SQL-Booster` 两种运行模式
- 不只生成基础三层代码，同时生成 `CreateDTO`、`UpdateDTO`、`QueryDTO`、`QueryVO`、`Excel导入DTO`、`Excel导出DTO`
- 支持统一返回体包装与分页返回包装，支持字符串方式和方法引用方式配置
- 支持查询扩展字段自动生成，例如 `Like`、`In`、`Lt`、`Gt`、`Lte`、`Gte`
- 支持无主键表降级生成：保留 `create`，自动跳过依赖主键的更新、删除、按 ID 查询
- 模板按需生成，未启用能力对应的文件会自动跳过，并输出明确的 `skip reason`
- 支持 `SpringDoc` / `Swagger`、`jakarta` / `javax`/ `Lombok` / `非 Lombok` 多种代码风格
- 支持表前缀、表后缀、字段前缀、字段后缀、公共字段、忽略字段、布尔字段 `is_` 前缀处理
- 支持单模板文件粒度覆盖：名称格式、子包、模板路径、输出目录、是否覆盖、是否禁用
- 支持新增模板文件入口，可扩展生成自定义 DTO / VO / 其他衍生文件
- 支持自定义渲染参数和自定义渲染逻辑，便于对接业务模板
- 支持扩展渲染参数
- 支持添加自定义模板文件

旧版地址：<https://github.com/bootystar/mybatis-plus-generator>

## 安装

建议放在 `test` 作用域中使用：

```xml
<dependency>
    <groupId>io.github.luminion</groupId>
    <artifactId>code-generator</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

如果使用快照版本，可额外配置 Sonatype snapshot 仓库：

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

## 入口

| API | 说明 |
| --- | --- |
| `GeneratorHelper.mybatisPlus(url, username, password)` | MyBatis-Plus 模式 |
| `GeneratorHelper.mybatisPlusSqlBooster(url, username, password)` | SQL-Booster 模式 |
| `.execute("sys_user", "sys_role")` | 只生成指定表 |
| `.execute()` | 生成当前过滤条件下的全部表 |

默认输出目录：`src/main/java`

## 示例 1：快速开始

```java
import io.github.luminion.generator.GeneratorHelper;

public class CodegenApp {
    public static void main(String[] args) {
        GeneratorHelper.mybatisPlus(
                        "jdbc:mysql://localhost:3306/demo",
                        "root",
                        "123456"
                )
                .execute("sys_user");
    }
}
```

SQL-Booster：

```java
GeneratorHelper.mybatisPlusSqlBooster(
                "jdbc:mysql://localhost:3306/demo",
                "root",
                "123456"
        )
        .execute("sys_user");
```

## 示例 2：常用配置

```java
import io.github.luminion.generator.GeneratorHelper;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.enums.DocType;
import io.github.luminion.generator.enums.JavaEEApi;
import io.github.luminion.generator.naming.NamingConverter;

public class CodegenApp {
    public static void main(String[] args) {
        GeneratorHelper.mybatisPlus(
                        "jdbc:mysql://localhost:3306/demo",
                        "root",
                        "123456"
                )
                .global(g -> g
                        .author("dev")
                        .date("yyyy-MM-dd")
                        .docType(DocType.SPRING_DOC)
                        .javaEEApi(JavaEEApi.JAKARTA)
                )
                .dataSource(d -> d
                        .dateType(DateType.TIME_PACK)
                        .namingConverter(NamingConverter.UNDERLINE_TO_CAMEL)
                        .tablePrefixes("sys_")
                        .commonColumns("create_time", "update_time")
                )
                .template(t -> t
                        .outputDir("D:/workspace/demo/src/main/java")
                        .parentPackage("com.example.demo")
                        .parentModule("system")
                        .enableFileOverride()
                )
                .mapper(m -> m
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                )
                .controller(c -> c
                        .baseUrl("/api/admin")
                        .enableRestful()
                        .enableQueryViaPost()
                        .returnMethod("com.example.common.Result", "success")
                        .pageMethod("com.example.common.PageResult", "success")
                )
                .query(q -> q
                        .queryByIdMethodName("detail")
                        .queryListMethodName("list")
                        .queryPageMethodName("page")
                        .pageParamName("page")
                        .sizeParamName("size")
                )
                .execute("sys_user");
    }
}
```

## 示例 3：完整配置

```java
import io.github.luminion.generator.GeneratorHelper;
import io.github.luminion.generator.enums.ColumnFillStrategy;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.enums.DocType;
import io.github.luminion.generator.enums.ExcelApi;
import io.github.luminion.generator.enums.ExcelExportMode;
import io.github.luminion.generator.enums.ExcelImportMode;
import io.github.luminion.generator.enums.IdType;
import io.github.luminion.generator.enums.JavaEEApi;
import io.github.luminion.generator.naming.NamingConverter;

public class CodegenApp {
    public static void main(String[] args) {
        GeneratorHelper.mybatisPlus(
                        "jdbc:mysql://localhost:3306/demo",
                        "root",
                        "123456"
                )
                .global(g -> g
                        .author("dev")
                        .date("yyyy-MM-dd HH:mm:ss")
                        .docType(DocType.SPRING_DOC)
                        .javaEEApi(JavaEEApi.JAKARTA)
                        .enableToString()
                        .enableChainSetter()
                )
                .dataSource(d -> d
                        .schema("public")
                        .dateType(DateType.TIME_PACK)
                        .namingConverter(NamingConverter.UNDERLINE_TO_CAMEL)
                        .enableSkipView()
                        .enableBooleanColumnRemoveIsPrefix()
                        .includeTables("sys_user", "sys_role")
                        .excludeTables("sys_log")
                        .tablePrefixes("sys_", "tbl_")
                        .tableSuffixes("_tb")
                        .columnPrefixes("f_")
                        .columnSuffixes("_flag")
                        .commonColumns("create_time", "update_time")
                        .ignoreColumns("deleted")
                )
                .template(t -> t
                        .outputDir("D:/workspace/demo/src/main/java")
                        .parentPackage("com.example.demo")
                        .parentModule("system")
                        .enableFileOverride()
                        .entity(e -> e
                                .nameFormat("%sEntity")
                                .subPackage("model.entity")
                        )
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
                        .pageType("com.baomidou.mybatisplus.core.metadata.IPage")
                )
                .mapper(m -> m
                        .superClass(BaseMapper.class)
                        .mapperAnnotationClass(org.apache.ibatis.annotations.Mapper.class)
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        .appendOrderColumn("sort", false)
                )
                .controller(c -> c
                        .superClass(BaseController.class)
                        .baseUrl("/api/admin")
                        .enableRestful()
                        .enableCrossOrigin()
                        .disablePathVariable()
                        .enableQueryViaPost()
                        .returnMethod("com.example.common.Result", "Result<%s>", "Result.success(%s)")
                        .pageMethod("com.example.common.PageResult", "PageResult<%s>", "PageResult.success(%s)")
                )
                .command(cmd -> cmd
                        .disableValid()
                        .createMethodName("create")
                        .updateMethodName("update")
                        .deleteMethodName("delete")
                        .excludeColumns("create_time", "create_by")
                )
                .query(q -> q
                        .queryByIdMethodName("detail")
                        .queryListMethodName("list")
                        .queryPageMethodName("page")
                        .appendExtraFieldSuffix("Like", "LIKE")
                        .appendExtraFieldSuffix("In", "IN")
                        .pageParamName("page")
                        .sizeParamName("size")
                        .queryParamSuperClass(BaseQuery.class)
                        .queryResultSuperClass(BaseVO.class)
                )
                .excel(ex -> ex
                        .api(ExcelApi.EASY_EXCEL)
                        .importMode(ExcelImportMode.BATCH)
                        .excelImportBatchSize(1000)
                        .exportMode(ExcelExportMode.PAGED)
                        .excelExportPageSize(2000)
                        .excelImportTemplateMethodName("downloadTemplate")
                        .excelImportMethodName("importExcel")
                        .excelExportMethodName("exportExcel")
                )
                .execute("sys_user", "sys_role");
    }
}
```

## 配置参考

### `global()`

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `author(String)` | JavaDoc 作者 | `author("dev")` |
| `date(String)` | JavaDoc 日期格式 | `date("yyyy-MM-dd")` |
| `docType(DocType)` | `JAVA_DOC` / `SWAGGER` / `SPRING_DOC` | `docType(DocType.SPRING_DOC)` |
| `javaEEApi(JavaEEApi)` | `JAKARTA` / `JAVAX` | `javaEEApi(JavaEEApi.JAKARTA)` |
| `disableSeeTags()` | 关闭 `@see` 标签 | `disableSeeTags()` |
| `disableLombok()` | 禁用 Lombok | `disableLombok()` |
| `enableToString()` | 生成 `toString()` | `enableToString()` |
| `enableChainSetter()` | 启用链式 setter | `enableChainSetter()` |
| `disableSerializable()` | 禁用 `Serializable` | `disableSerializable()` |
| `disableSerializableAnnotation()` | 禁用 `@Serial` | `disableSerializableAnnotation()` |
| `skipCreate()` | 跳过新增能力 | `skipCreate()` |
| `skipUpdate()` | 跳过更新能力 | `skipUpdate()` |
| `skipDelete()` | 跳过删除能力 | `skipDelete()` |
| `skipQueryById()` | 跳过按 ID 查询能力 | `skipQueryById()` |
| `skipQueryList()` | 跳过列表查询能力 | `skipQueryList()` |
| `skipQueryPage()` | 跳过分页查询能力 | `skipQueryPage()` |
| `skipExcelImport()` | 跳过 Excel 导入能力 | `skipExcelImport()` |
| `skipExcelExport()` | 跳过 Excel 导出能力 | `skipExcelExport()` |
| `customRenderData(Map<String, Object>)` | 追加固定渲染参数 | `customRenderData(renderData)` |
| `customRenderLogic(BiConsumer<TableInfo, Map<String, Object>>)` | 输出前动态修改渲染参数 | `customRenderLogic((table, data) -> {})` |

### `dataSource()`

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `schema(String)` | 数据库 schema | `schema("public")` |
| `dateType(DateType)` | 日期类型策略 | `dateType(DateType.TIME_PACK)` |
| `namingConverter(NamingConverter)` | 表名 / 字段名命名转换 | `namingConverter(NamingConverter.UNDERLINE_TO_CAMEL)` |
| `fieldTypeConverter(FieldTypeConverter)` | 自定义字段类型转换 | `fieldTypeConverter(meta -> null)` |
| `keywordsHandler(DatabaseKeywordsHandler)` | 自定义关键字处理器 | `keywordsHandler(handler)` |
| `enableSkipView()` | 跳过视图 | `enableSkipView()` |
| `enableBooleanColumnRemoveIsPrefix()` | 布尔字段去除 `is_` 前缀 | `enableBooleanColumnRemoveIsPrefix()` |
| `tableNamePattern(String)` | 表名模糊匹配 | `tableNamePattern("sys_%")` |
| `includeTables(String... / Collection<String>)` | 包含表 | `includeTables("sys_user", "sys_role")` |
| `excludeTables(String... / Collection<String>)` | 排除表 | `excludeTables("sys_log")` |
| `tablePrefixes(String... / Collection<String>)` | 去除表前缀 | `tablePrefixes("sys_")` |
| `tableSuffixes(String... / Collection<String>)` | 去除表后缀 | `tableSuffixes("_tb")` |
| `commonColumns(String... / Collection<String>)` | 视为父类公共字段 | `commonColumns("create_time", "update_time")` |
| `ignoreColumns(String... / Collection<String>)` | 忽略字段 | `ignoreColumns("deleted")` |
| `columnPrefixes(String... / Collection<String>)` | 去除字段前缀 | `columnPrefixes("f_")` |
| `columnSuffixes(String... / Collection<String>)` | 去除字段后缀 | `columnSuffixes("_flag")` |

### `template()`

全局模板配置：

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `outputDir(String)` | 输出目录 | `outputDir("D:/workspace/demo/src/main/java")` |
| `enableOpenOutputDir()` | 生成后打开输出目录 | `enableOpenOutputDir()` |
| `enableFileOverride()` | 全局开启文件覆盖 | `enableFileOverride()` |
| `parentPackage(String)` | 基础包名 | `parentPackage("com.example.demo")` |
| `parentModule(String)` | 模块包名片段 | `parentModule("system")` |
| `controller(...)` | 覆盖 controller 模板文件配置 | `controller(c -> c.disable())` |
| `service(...)` | 覆盖 service 模板文件配置 | `service(s -> s.subPackage("service"))` |
| `serviceImpl(...)` | 覆盖 serviceImpl 模板文件配置 | `serviceImpl(s -> s.subPackage("service.impl"))` |
| `mapper(...)` | 覆盖 mapper 模板文件配置 | `mapper(m -> m.nameFormat("%sMapper"))` |
| `mapperXml(...)` | 覆盖 mapperXml 模板文件配置 | `mapperXml(m -> m.fileOutputDir("D:/xml"))` |
| `entity(...)` | 覆盖 entity 模板文件配置 | `entity(e -> e.nameFormat("%sEntity"))` |
| `queryParam(...)` | 覆盖 queryParam 模板文件配置 | `queryParam(q -> q.disable())` |
| `queryResult(...)` | 覆盖 queryResult 模板文件配置 | `queryResult(v -> v.subPackage("model.vo"))` |
| `createParam(...)` | 覆盖 createParam 模板文件配置 | `createParam(c -> c.nameFormat("%sCreateCmd"))` |
| `updateParam(...)` | 覆盖 updateParam 模板文件配置 | `updateParam(u -> u.nameFormat("%sUpdateCmd"))` |
| `excelExportParam(...)` | 覆盖 excelExportParam 模板文件配置 | `excelExportParam(e -> e.subPackage("model.excel"))` |
| `excelImportParam(...)` | 覆盖 excelImportParam 模板文件配置 | `excelImportParam(e -> e.subPackage("model.excel"))` |
| `addTemplateFile(Consumer<TemplateFileBuilder>)` | 使用 builder 新增模板文件 | `addTemplateFile(f -> f.key("queryCondition").nameFormat("%sQueryCondition").templatePath("/templates/queryParam.java").outputFileSuffix(".java"))` |

单个模板文件配置：

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `disable()` | 禁用该文件生成 | `controller(c -> c.disable())` |
| `nameFormat(String)` | 输出类名 / 文件名格式 | `entity(e -> e.nameFormat("%sEntity"))` |
| `subPackage(String)` | 输出子包 | `entity(e -> e.subPackage("model.entity"))` |
| `templatePath(String)` | 模板文件路径 | `entity(e -> e.templatePath("/templates/entity.java"))` |
| `outputFileSuffix(String)` | 输出文件后缀 | `mapperXml(m -> m.outputFileSuffix(".xml"))` |
| `enableFileOverride()` | 仅对该文件开启覆盖 | `entity(e -> e.enableFileOverride())` |
| `fileOutputDir(String)` | 仅对该文件指定输出目录 | `mapperXml(m -> m.fileOutputDir("D:/mapper-xml"))` |

### `entity()`

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `superClass(String / Class<?>)` | 实体父类 | `superClass(BaseEntity.class)` |
| `idType(IdType)` | 主键策略 | `idType(IdType.ASSIGN_ID)` |
| `enableActiveRecord()` | 启用 ActiveRecord | `enableActiveRecord()` |
| `enableTableFieldAnnotation()` | 始终输出 `@TableField` | `enableTableFieldAnnotation()` |
| `versionColumnName(String)` | 乐观锁字段名 | `versionColumnName("version")` |
| `logicDeleteColumnName(String)` | 逻辑删除字段名 | `logicDeleteColumnName("deleted")` |
| `columnFill(String, ColumnFillStrategy)` | 单个字段填充策略 | `columnFill("create_time", ColumnFillStrategy.INSERT)` |
| `columnFill(Map<String, ColumnFillStrategy>)` | 批量字段填充策略 | `columnFill(fillMap)` |

### `service()`

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `serviceSuperClass(String / Class<?>)` | Service 父接口 | `serviceSuperClass(BaseService.class)` |
| `serviceImplSuperClass(String / Class<?>)` | ServiceImpl 父类 | `serviceImplSuperClass(BaseServiceImpl.class)` |
| `pageType(String / Class<?>)` | Service 分页返回类型 | `pageType("com.baomidou.mybatisplus.core.metadata.IPage")` |
| `pageParamName(String)` | Service 分页方法页码参数名 | `pageParamName("current")` |
| `sizeParamName(String)` | Service 分页方法条数参数名 | `sizeParamName("size")` |

### `mapper()`

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `superClass(String / Class<?>)` | Mapper 父接口 | `superClass(BaseMapper.class)` |
| `mapperAnnotationClass(String / Class<?>)` | Mapper 注解类型 | `mapperAnnotationClass(org.apache.ibatis.annotations.Mapper.class)` |
| `enableBaseColumnList()` | 生成 `Base_Column_List` | `enableBaseColumnList()` |
| `enableBaseResultMap()` | 生成 `BaseResultMap` | `enableBaseResultMap()` |
| `mapperCacheClass(String / Class<?>)` | Mapper 缓存类 | `mapperCacheClass(MyCache.class)` |
| `appendOrderColumn(String, boolean)` | 追加默认排序字段 | `appendOrderColumn("sort", false)` |
| `orderColumnMap(Map<String, Boolean>)` | 批量排序配置 | `orderColumnMap(orderMap)` |

### `controller()`

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `superClass(String / Class<?>)` | Controller 父类 | `superClass(BaseController.class)` |
| `disableRestController()` | 使用 `@Controller` 替代 `@RestController` | `disableRestController()` |
| `baseUrlStyle(BaseUrlStyle)` | 设置实体路由风格，支持 `CAMEL_CASE` / `HYPHEN_CASE` / `UNDERLINE_CASE` / `SLASH_CASE` | `baseUrlStyle(BaseUrlStyle.SLASH_CASE)` |
| `disableHyphenStyle()` | 兼容旧写法，等价于 `baseUrlStyle(BaseUrlStyle.CAMEL_CASE)` | `disableHyphenStyle()` |
| `baseUrl(String)` | 路由前缀 | `baseUrl("/api/admin")` |
| `enableCrossOrigin()` | 添加 `@CrossOrigin` | `enableCrossOrigin()` |
| `enableRestful()` | 使用 RESTful 路由风格 | `enableRestful()` |
| `disablePathVariable()` | 不使用 `@PathVariable` | `disablePathVariable()` |
| `disableRequestBody()` | 不使用 `@RequestBody` | `disableRequestBody()` |
| `enableQueryViaPost()` | 列表 / 分页查询使用 POST | `enableQueryViaPost()` |
| `returnMethod(MethodReference<Object, R>)` | 方法引用配置统一返回 | `returnMethod(Result::success)` |
| `returnMethod(String, String)` | 类名 + 方法名配置统一返回 | `returnMethod("com.example.common.Result", "success")` |
| `returnMethod(String, String, String)` | 完整配置统一返回 | `returnMethod("com.example.common.Result", "Result<%s>", "Result.success(%s)")` |
| `pageMethod(MethodReference<T, R>, Class<T>)` | 方法引用配置分页包装 | `pageMethod(PageResult::success, IPage.class)` |
| `pageMethod(String, String)` | 类名 + 方法名配置分页包装 | `pageMethod("com.example.common.PageResult", "success")` |
| `pageMethod(String, String, String)` | 完整配置分页包装 | `pageMethod("com.example.common.PageResult", "PageResult<%s>", "PageResult.success(%s)")` |

### `command()`

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `disableValid()` | 禁用校验注解 | `disableValid()` |
| `createMethodName(String)` | 新增方法名 | `createMethodName("create")` |
| `updateMethodName(String)` | 更新方法名 | `updateMethodName("update")` |
| `deleteMethodName(String)` | 删除方法名 | `deleteMethodName("delete")` |
| `excludeColumns(String... / Collection<String>)` | 从命令 DTO 中排除字段 | `excludeColumns("create_time", "create_by")` |

### `query()`

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `queryByIdMethodName(String)` | 按 ID 查询方法名 | `queryByIdMethodName("detail")` |
| `queryListMethodName(String)` | 列表查询方法名 | `queryListMethodName("list")` |
| `queryPageMethodName(String)` | 分页查询方法名 | `queryPageMethodName("page")` |
| `appendExtraFieldSuffix(String, String)` | 添加一个额外查询字段后缀 | `appendExtraFieldSuffix("Like", "LIKE")` |
| `extraFieldSuffixMap(Map<String, String>)` | 覆盖后缀映射 | `extraFieldSuffixMap(suffixMap)` |
| `extraFieldStrategy(ExtraFieldStrategy)` | 自定义后缀生成策略 | `extraFieldStrategy(strategy)` |
| `pageParamName(String)` | QueryDTO 分页字段名 | `pageParamName("page")` |
| `sizeParamName(String)` | QueryDTO 每页条数字段名 | `sizeParamName("size")` |
| `pageSizeParam(MethodReference<T, Long>, MethodReference<T, Long>)` | 复用父类分页字段 | `pageSizeParam(BaseQuery::getCurrent, BaseQuery::getSize)` |
| `disableQueryParamPageFields()` | 不生成 QueryDTO 分页字段 | `disableQueryParamPageFields()` |
| `queryParamSuperClass(String / Class<?>)` | QueryDTO 父类 | `queryParamSuperClass(BaseQuery.class)` |
| `queryResultSuperClass(String / Class<?>)` | QueryResult 父类 | `queryResultSuperClass(BaseVO.class)` |

### `excel()`

| 方法 | 说明 | 示例 |
| --- | --- | --- |
| `api(ExcelApi)` | Excel 实现 | `api(ExcelApi.EASY_EXCEL)` |
| `importMode(ExcelImportMode)` | Excel 导入策略，`SIMPLE` / `BATCH` | `importMode(ExcelImportMode.BATCH)` |
| `excelImportBatchSize(int)` | `BATCH` 模式下导入分批大小 | `excelImportBatchSize(1000)` |
| `excelImportMethodName(String)` | Excel 导入方法名 | `excelImportMethodName("importExcel")` |
| `excelImportTemplateMethodName(String)` | Excel 模板下载方法名 | `excelImportTemplateMethodName("downloadTemplate")` |
| `exportMode(ExcelExportMode)` | Excel 导出策略，`SIMPLE` / `PAGED` | `exportMode(ExcelExportMode.PAGED)` |
| `excelExportPageSize(int)` | `PAGED` 模式下导出分页大小 | `excelExportPageSize(2000)` |
| `excelExportMethodName(String)` | Excel 导出方法名 | `excelExportMethodName("exportExcel")` |

默认情况下，导入导出都使用 `SIMPLE` 模式，生成结果保持简洁。

- `ExcelImportMode.SIMPLE`：同步读成 `List` 后统一入库，适合中小数据量
- `ExcelImportMode.BATCH`：使用 listener 分批消费，适合大数据量导入
- `ExcelExportMode.SIMPLE`：一次性查询后写出，适合中小数据量
- `ExcelExportMode.PAGED`：分页查询并分批写出，适合大数据量导出

## 特殊情况说明

### 1. 无主键表

当表没有主键时：

- `create` 相关代码仍然生成
- `create` 返回值退化为 `Boolean`
- 依赖主键的能力自动跳过

典型跳过项：

- `UpdateDTO`
- 按主键更新
- 按主键删除
- `queryById`

### 2. 按需生成

当某类能力未启用时，相关模板文件会自动跳过。

典型场景：
- 未启用创建时，跳过`新增DTO` 
- 未启用更新时，跳过`更新DTO` 
- 未启用列表 / 分页 / 导出时，跳过`QueryDTO` 
- 未启用列表 / 分页 / 导出时，跳过`VO` 
- 未启用 Excel 导入时，跳过`Excel导入DTO`
- 未启用 Excel 导出时，跳过`Excel导出DTO`

### 3. 模板显式禁用优先

如果通过 `template()` 显式禁用了某个模板文件，则该文件不会生成。

示例：

```java
.template(t -> t
        .queryParam(q -> q.disable())
        .queryResult(v -> v.disable())
)
```

### 4. `execute(...)` 与表过滤

`execute("sys_user", "sys_role")` 只生成本次传入的表。

`execute()` 则按照当前 `dataSource()` 中的过滤条件执行。

### 5. 覆盖行为

默认不覆盖已有文件。

以下两种方式可开启覆盖：

```java
.template(t -> t.enableFileOverride())
```

```java
.template(t -> t
        .entity(e -> e.enableFileOverride())
)
```

### 6. 分页字段复用

如果使用：

```java
.query(q -> q.pageSizeParam(BaseQuery::getCurrent, BaseQuery::getSize))
```

则行为为：

- QueryDTO 继承 `BaseQuery`
- QueryDTO 不再重复生成分页字段
- 分页字段名从方法引用中解析

### 7. 返回包装配置方式

统一返回和分页包装支持两种方式：

字符串方式：

```java
.controller(c -> c
        .returnMethod("com.example.common.Result", "success")
        .pageMethod("com.example.common.PageResult", "success")
)
```

方法引用方式：

```java
.controller(c -> c
        .returnMethod(Result::success)
        .pageMethod(PageResult::success, IPage.class)
)
```

### 8. 跳过原因

模板被跳过时，控制台会输出明确的 skip reason。

排查优先级：

1. 查看全局功能开关
2. 查看模板是否被显式禁用
3. 查看表是否缺少主键
4. 查看控制台 skip reason

## 自定义输出参数

```java
Map<String, Object> renderData = new LinkedHashMap<>();
renderData.put("projectName", "demo");
renderData.put("apiVersion", "v1");

GeneratorHelper.mybatisPlus(url, username, password)
        .global(g -> g
                .customRenderData(renderData)
                .customRenderLogic((table, data) -> {
                    data.put("entityLowerName", table.getEntityVariableName());
                    data.put("tableComment", table.getComment());
                })
        );
```

模板中可直接使用：

```velocity
$projectName
$apiVersion
$entityLowerName
$tableComment
```

## 自定义模板文件

```java
.template(t -> t
        .entity(e -> e
                .nameFormat("%sEntity")
                .subPackage("model.entity")
                .templatePath("/templates/entity.java")
                .outputFileSuffix(".java")
                .enableFileOverride()
        )
        .queryParam(q -> q.disable())
)
```

新增模板文件：

```java
.template(t -> t
        .addTemplateFile(f -> f
                .key("queryCondition")
                .nameFormat("%sQueryCondition")
                .subPackage("model.query")
                .templatePath("/templates/queryParam.java")
                .outputFileSuffix(".java")
        )
)
```

新增多个模板文件：

```java
.template(t -> t
        .addTemplateFile(f -> f
                .key("queryCondition")
                .nameFormat("%sQueryCondition")
                .subPackage("model.query")
                .templatePath("/templates/queryParam.java")
                .outputFileSuffix(".java")
                .enableFileOverride()
        )
        .addTemplateFile(f -> f
                .key("exportView")
                .nameFormat("%sExportView")
                .subPackage("model.export")
                .templatePath("/templates/queryResult.java")
                .outputFileSuffix(".java")
        )
)
```

说明：

- `key` 必须唯一
- 每次调用 `addTemplateFile(...)` 都会创建并注册一个新的模板文件
- builder 方式在 `Consumer` 执行完成后立即校验配置完整性
- 至少需要提供：`key`、`nameFormat`、`templatePath`、`outputFileSuffix`
- 新增文件会进入模板解析、输出和覆盖流程
- 新增文件会按 `key` 注入渲染上下文

例如：

- `key = "queryCondition"` 时，可在模板中通过 `$queryCondition` 访问
- 可使用 `${queryCondition.classSimpleName}`、`${queryCondition.packageName}`、`${queryCondition.classCanonicalName}`

模板中可用的常见对象：

- `table`
- `entity`
- `mapper`
- `service`
- `serviceImpl`
- `controller`
- `createParam`
- `updateParam`
- `queryParam`
- `queryResult`
- `excelImportParam`
- `excelExportParam`

再加上通过 `customRenderData(...)` / `customRenderLogic(...)` 追加的自定义参数。

## 声明

项目中部分代码源自 [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)，相关文件已保留原始版权信息。
