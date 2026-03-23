# code-generator

[![Maven Central](https://img.shields.io/maven-central/v/io.github.luminion/code-generator)](https://mvnrepository.com/artifact/io.github.luminion/code-generator)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

一个基于Lambda 表达式风格的代码生成器，采用链式调用配置方式，用于快速生成代码。
支持生成实体类、Mapper、Service、Controller、DTO、Query 参数等完整的后端代码。

旧版地址: https://github.com/bootystar/mybatis-plus-generator

## 功能特性

- **基础代码生成**：生成`Entity`、`Mapper`、`Service`、`Controller` 等基础代码
- **领域模型生成**：支持生成`新增DTO、`修改DTO`、`查询DTO`、`查询VO`、`EXCEL导入DTO`、`EXCEL导出DTO` 等领域模型
- **CRUD方法生成**：生成增删查改方法
- **参数校验**：生成参数校验相关注解
- **快捷查询** xml默认生成绝大多数场景的sql查询, 并封装了dto供前端调用, 无需手动编写
- **Excel导入导出**：支持生成Excel导入导出相关代码，支持`EasyExcel`/`FastExcel`/`Apache Fesod`
- **多配置集成**：提供多种配置, 适配各种场景格式
- **多种数据库支持**：支持 MySQL、PostgreSQL等主流数据库
- **模板引擎支持**：使用 Velocity 模板引擎，支持自定义模板
- **链式调用**：提供链式调用的配置方式
- **自定义扩展**:支持自定义模板及参数扩展
- **SQL-Booster 集成**: 集成SQL-Booster，提供更丰富的动态SQL查询能力

---

## maven仓库

可添加maven中央快照仓库(可能需网络代理)获取快照版本

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

<dependencies>
    <dependency>
        <groupId>io.github.luminion</groupId>
        <artifactId>code-generator</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## 快速开始

### 1. 添加依赖

[![Maven Central](https://img.shields.io/maven-central/v/io.github.luminion/code-generator)](https://mvnrepository.com/artifact/io.github.luminion/code-generator)

```xml
<dependency>
    <groupId>io.github.luminion</groupId>
    <artifactId>code-generator</artifactId>
    <version>latest</version>
    <scope>test</scope>
</dependency>

<!-- MySQL Connector (请根据您的数据库选择) -->
<dependency>
<groupId>mysql</groupId>
<artifactId>mysql-connector-java</artifactId>
<version>latest</version>
</dependency>
```

## 生成代码

```java
import io.github.luminion.generator.GeneratorHelper;

public static void main(String[] args) {
    GeneratorHelper.mybatisPlus("jdbc:mysql://localhost:3306/your_database",
                    "username",
                    "password")
            .execute("user", "role");
}
```
代码默认会生成到运行所在项目的`src/main/java`目录下, 可通过以下控制台输出确认生成文件路径



## SQL-Booster 集成
使用 `GeneratorHelper.mybatisPlusSqlBooster`, 生成sql-booster相关代码
```java
import io.github.luminion.generator.GeneratorHelper;

public static void main(String[] args) {
    GeneratorHelper.mybatisPlusSqlBooster("jdbc:mysql://localhost:3306/your_database",
                    "username",
                    "password")
            .execute("user", "role");
}
```


### 可选配置

```java
public static void main(String[] args) {
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
                    .enableSKipView()
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
                    .idType(IdType.ASSIGN_ID)
                    .enableActiveRecord()
                    .enableTableFieldAnnotation()
                    .versionColumnName("version")
                    .logicDeleteColumnName("deleted")
                    .columnFill("create_time", ColumnFillStrategy.INSERT)
                    .columnFill("update_time", ColumnFillStrategy.INSERT_UPDATE)
            )
            .service(s -> s
                    .serviceSuperClass("com.example.base.BaseService")
                    .serviceImplSuperClass("com.example.base.BaseServiceImpl")
            )
            .mapper(m -> m
                    .superClass("com.example.base.BaseMapper")
                    .mapperAnnotationClass("org.apache.ibatis.annotations.Mapper")
                    .enableBaseColumnList()
                    .enableBaseResultMap()
                    .appendOrderColumn("sort", true)
            )
            .controller(c -> c
                    .superClass("com.example.base.BaseController")
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
                    .queryParamSuperClass("com.example.common.PageQuery")
                    .queryResultSuperClass("com.example.common.PageResult")
            )
            .excel(ex -> ex
                    .api(ExcelApi.EASY_EXCEL)
                    .excelImportMethodName("importExcel")
                    .excelExportMethodName("exportExcel")
            )
            .execute("user", "role");
}
```
## 配置详解

### global() 全局配置

| 方法 | 类型 | 说明 |
|------|------|------|
| `author(String)` | String | 文档作者 |
| `date(String)` | String | 注释日期格式，默认 "yyyy-MM-dd" |
| `docType(DocType)` | DocType | 文档类型：JAVA_DOC, SWAGGER, SPRING_DOC |
| `javaEEApi(JavaEEApi)` | JavaEEApi | Java EE 框架：JAVAX (javax.*), JAKARTA (jakarta.*) |
| `disableLombok()` | - | 禁用 Lombok 模型 |
| `enableChainSetter()` | - | 启用链式 setter |
| `enableToString()` | - | 启用 toString 方法 |
| `disableSerializable()` | - | 禁用 Serializable 接口 |
| `disableSerializableAnnotation()` | - | 禁用 @Serial 注解 |
| `skipCreate()` | - | 禁用生成新增方法 |
| `skipUpdate()` | - | 禁用生成更新方法 |
| `skipDelete()` | - | 禁用生成删除方法 |
| `skipQueryById()` | - | 禁用生成 ID 查询方法 |
| `skipQueryList()` | - | 禁用生成列表查询方法 |
| `skipQueryPage()` | - | 禁用生成分页查询方法 |
| `skipExcelImport()` | - | 禁用生成 Excel 导入 |
| `skipExcelExport()` | - | 禁用生成 Excel 导出 |
| `disableSeeTags()` | - | 关闭文档注释中的 @see 链接 |
| `customRenderData(Map)` | Map<String, Object> | 自定义渲染数据 |
| `customRenderLogic(BiConsumer)` | BiConsumer<TableInfo, Map> | 自定义渲染逻辑 |

---

### dataSource() 数据源配置

| 方法 | 类型 | 说明 |
|------|------|------|
| `schema(String)` | String | 数据库 schema |
| `dateType(DateType)` | DateType | 日期类型：ONLY_DATE, SQL_PACK, TIME_PACK |
| `namingConverter(NamingConverter)` | NamingConverter | 命名转换器 |
| `fieldTypeConverter(FieldTypeConverter)` | FieldTypeConverter | 字段类型转换器 |
| `keywordsHandler(DatabaseKeywordsHandler)` | DatabaseKeywordsHandler | 数据库关键字处理器 |
| `enableSKipView()` | - | 跳过视图 |
| `enableBooleanColumnRemoveIsPrefix()` | - | 移除 Boolean 字段的 is 前缀 |
| `tableNamePattern(String)` | String | 表名匹配模式（需自行拼接 %） |
| `includeTables(String.../Collection)` | - | 指定包含的表 |
| `excludeTables(String.../Collection)` | - | 指定排除的表 |
| `tablePrefixes(String.../Collection)` | - | 表名前缀过滤 |
| `tableSuffixes(String.../Collection)` | - | 表名后缀过滤 |
| `columnPrefixes(String.../Collection)` | - | 字段前缀过滤 |
| `columnSuffixes(String.../Collection)` | - | 字段后缀过滤 |
| `commonColumns(String.../Collection)` | - | 公共字段（父类共有） |
| `ignoreColumns(String.../Collection)` | - | 忽略字段 |

---

### template() 模板配置

| 方法 | 类型 | 说明 |
|------|------|------|
| `outputDir(String)` | String | 输出目录 |
| `parentPackage(String)` | String | 父包名 |
| `parentModule(String)` | String | 父模块名 |
| `enableOpenOutputDir()` | - | 生成后打开输出目录 |
| `enableFileOverride()` | - | 启用文件覆盖 |

#### template() 子配置

配置各模板文件的通用内容, 包含以下模板文件: 
- controller
- service
- serviceImpl
- mapper
- mapperXml
- entity
- queryParam
- queryResult
- createParam
- updateParam
- excelExportParam
- excelImportParam

```java
.template(t -> t
    .entity(e -> e
        .nameFormat("%sEntity")
        .subPackage("model.entity")
        .templatePath("/templates/entity.java")
        .outputFileSuffix(".java")
        .enalbeFileOverride()
        .disable()
    )
)
```

| 方法                            | 类型     | 说明                   |
|-------------------------------|--------|----------------------|
| `nameFormat(String)`          | String | 文件名格式，如 "%sEntity"   |
| `subPackage(String)`          | String | 子包名                  |
| `templatePath(String)`        | String | 模板路径（classpath 相对路径） |
| `outputFileSuffix(String)`    | String | 输出文件后缀               |
| `enableFileOverride()` | -      | 是否覆盖已有文件             |
| `disable()`                   | -      | 禁止生成该文件              |
| `fileOutputDir(String)`       | String | 单独指定输出目录             |

---

### entity() 实体配置

| 方法 | 类型 | 说明 |
|------|------|------|
| `superClass(String)` | String | 实体父类（全限定名） |
| `idType(IdType)` | IdType | 主键生成策略：AUTO, NONE, INPUT, ASSIGN_ID, ASSIGN_UUID |
| `enableActiveRecord()` | - | 启用 ActiveRecord 模式 |
| `enableTableFieldAnnotation()` | - | 生成字段注解 |
| `versionColumnName(String)` | String | 乐观锁字段名 |
| `logicDeleteColumnName(String)` | String | 逻辑删除字段名 |
| `columnFill(String, ColumnFillStrategy)` | - | 自动填充配置 |
| `columnFill(Map)` | - | 批量自动填充配置 |

**ColumnFillStrategy**：
- `DEFAULT` - 默认不处理
- `INSERT` - 插入时填充
- `UPDATE` - 更新时填充
- `INSERT_UPDATE` - 插入和更新时填充

---

### service() 服务配置

| 方法 | 类型 | 说明 |
|------|------|------|
| `serviceSuperClass(String/Class)` | - | Service 接口父类 |
| `serviceImplSuperClass(String/Class)` | - | ServiceImpl 父类 |

---

### mapper() 配置

| 方法 | 类型 | 说明 |
|------|------|------|
| `superClass(String)` | String | Mapper 父类 |
| `mapperAnnotationClass(String)` | String | Mapper 注解类 |
| `enableBaseColumnList()` | - | 启用 Base_Column_List |
| `enableBaseResultMap()` | - | 启用 BaseResultMap |
| `mapperCacheClass(String)` | String | Mapper 缓存类 |
| `appendOrderColumn(String, boolean)` | - | 添加排序字段 |
| `orderColumnMap(Map)` | - | 批量配置排序字段 |

---

### controller() 配置

| 方法 | 类型 | 说明 |
|------|------|------|
| `superClass(String/Class)` | - | Controller 父类 |
| `baseUrl(String)` | String | 请求前缀 |
| `disableRestController()` | - | 使用 @Controller 而非 @RestController |
| `disableHyphenStyle()` | - | 禁用驼峰转连字符 |
| `enableCrossOrigin()` | - | 启用 @CrossOrigin |
| `enableRestful()` | - | 启用 RESTful 风格 |
| `disablePathVariable()` | - | 禁用 @PathVariable |
| `disableRequestBody()` | - | 禁用 @RequestBody |
| `enableQueryViaPost()` | - | 查询使用 POST |
| `returnMethod(MethodReference/String...)` | - | 返回值包装方法 |
| `pageMethod(MethodReference/String...)` | - | 分页包装方法 |

**returnMethod 示例**：
```java
.returnMethod("com.example.Result", "Result<%s>", "Result::success")
// 或使用 Lambda
.returnMethod(Result::success)
```

---

### command() 配置（增删改）

| 方法 | 类型 | 说明 |
|------|------|------|
| `disableValid()` | - | 禁用参数校验 |
| `createMethodName(String)` | String | 新增方法名 |
| `updateMethodName(String)` | String | 更新方法名 |
| `deleteMethodName(String)` | String | 删除方法名 |
| `excludeColumns(String.../Collection)` | - | 新增/更新时排除的字段 |

---

### query() 查询配置

| 方法 | 类型 | 说明 |
|------|------|------|
| `queryByIdMethodName(String)` | String | 按 ID 查询方法名 |
| `queryListMethodName(String)` | String | 列表查询方法名 |
| `queryPageMethodName(String)` | String | 分页查询方法名 |
| `appendExtraFieldSuffix(String, String)` | - | 添加额外字段后缀（如 "Like" -> "LIKE"） |
| `extraFieldSuffixMap(Map)` | - | 批量配置额外字段后缀 |
| `extraFieldStrategy(ExtraFieldStrategy)` | - | 额外字段策略 |
| `pageParamName(String)` | String | 分页参数名 |
| `sizeParamName(String)` | String | 每页大小参数名 |
| `pageSizeParam(MethodReference, MethodReference)` | - | Lambda 方式设置分页参数 |
| `disableQueryParamPageFields()` | - | 禁用 QueryParam 中的分页字段 |
| `queryParamSuperClass(String/Class)` | - | QueryParam 父类 |
| `queryResultSuperClass(String/Class)` | - | QueryResult 父类 |

---

### excel() 配置

| 方法 | 类型 | 说明 |
|------|------|------|
| `api(ExcelApi)` | ExcelApi | Excel 框架：EASY_EXCEL, FAST_EXCEL, APACHE_FESOD |
| `excelImportMethodName(String)` | String | 导入方法名 |
| `excelImportTemplateMethodName(String)` | String | 导入模板方法名 |
| `excelExportMethodName(String)` | String | 导出方法名 |

---

## 枚举说明

### DocType
| 枚举值 | 说明 |
|--------|------|
| `JAVA_DOC` | 标准 JavaDoc |
| `SWAGGER` | Spring Fox (Swagger v2) |
| `SPRING_DOC` | Spring Doc (Swagger v3) |

### JavaEEApi
| 枚举值 | 说明 |
|--------|------|
| `JAVAX` | javax.* 包（传统 Java EE） |
| `JAKARTA` | jakarta.* 包（Spring Boot 3.0+） |

### DateType
| 枚举值 | 说明 |
|--------|------|
| `ONLY_DATE` | 仅使用 java.util.Date |
| `SQL_PACK` | 使用 java.sql 包 |
| `TIME_PACK` | 使用 java.time 包（JDK8+） |

### IdType
| 枚举值 | 说明 |
|--------|------|
| `AUTO` | 数据库自增 |
| `NONE` | 未设置 |
| `INPUT` | 用户输入 |
| `ASSIGN_ID` | 雪花算法（默认） |
| `ASSIGN_UUID` | UUID |

### ExcelApi
| 枚举值 | 说明 |
|--------|------|
| `EASY_EXCEL` | Alibaba EasyExcel |
| `FAST_EXCEL` | FastExcel |
| `APACHE_FESOD` | Apache Fesod |

### NamingConverter
| 枚举值 | 说明 |
|--------|------|
| `NO_CHANGE` | 不转换 |
| `UNDERLINE_TO_CAMEL` | 下划线转驼峰 |
| `UNDERLINE_TO_PASCAL` | 下划线转帕斯卡 |

### ColumnFillStrategy
| 枚举值 | 说明 |
|--------|------|
| `DEFAULT` | 默认不处理 |
| `INSERT` | 插入时填充 |
| `UPDATE` | 更新时填充 |
| `INSERT_UPDATE` | 插入和更新时填充 |

---

## 生成的文件类型

执行后会生成以下文件（根据配置可能有所增减）：

| 文件 | 说明 |
|------|------|
| Entity | 实体类 |
| Mapper | Mapper 接口 |
| MapperXml | Mapper XML |
| Service | Service 接口 |
| ServiceImpl | Service 实现类 |
| Controller | 控制器 |
| CreateParam | 新增参数 DTO |
| UpdateParam | 修改参数 DTO |
| QueryParam | 查询参数 DTO |
| QueryResult | 查询结果 VO |
| ExcelImportParam | Excel 导入 DTO |
| ExcelExportParam | Excel 导出 DTO |

---





## 注意事项

1. 开启文件覆盖后, 生成的代码会覆盖同名文件，请注意备份重要文件
2. 需要确保数据库连接信息正确
3. 根据实际需要调整配置参数
4. 可以通过自定义模板来满足特殊需求

## 声明
该项目中部分代码源自[MyBatis-Plus](https://github.com/baomidou/mybatis-plus) 项目, 文件已保留相关版权信息。