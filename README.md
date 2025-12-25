# code-generator

[![Maven Central](https://img.shields.io/maven-central/v/io.github.luminion/code-generator)](https://mvnrepository.com/artifact/io.github.luminion/code-generator)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

一个代码生成器, 用于快速生成代码, 提升开发效率

旧版地址: https://github.com/bootystar/mybatis-plus-generator

## 功能特性

- **基础代码生成**：生成实体类、Mapper、Service、Controller 等基础代码
- **领域模型生成**：支持生成`新增DTO`、`修改DTO`、`查询DTO`、`查询VO`、`EXCEL导入DTO`、`EXCEL导出DTO` 等领域模型
- **CRUD方法生成**：生成增删查改方法
- **参数校验**：生成参数校验相关注解
- **快捷查询** xml默认生成绝大多数场景的sql查询, 并封装了dto供前端调用, 无需手动编写
- **Excel导入导出**：支持生成Excel导入导出相关代码，支持`EasyExcel`/`FastExcel`
- **多配置集成**：提供多种配置, 适配各种场景格式
- **多种数据库支持**：支持 MySQL、PostgreSQL、Oracle等主流数据库
- **模板引擎支持**：使用 Velocity 模板引擎，支持自定义模板
- **链式调用**：提供链式调用的配置方式
- **自定义扩展**:支持自定义模板及参数扩展
- **SQL-Booster 集成**: 集成SQL-Booster，提供更丰富的动态SQL查询能力

---

## 准备工作

### 1. 添加生成器依赖

首先, 在您的项目中添加 `code-generator` 的依赖。由于它是一个开发工具, 通常建议将其放在 `test` 或 `provided` 作用域下。

[![Maven Central](https://img.shields.io/maven-central/v/io.github.luminion/code-generator)](https://mvnrepository.com/artifact/io.github.luminion/code-generator)

```xml
<dependency>
    <groupId>io.github.luminion</groupId>
    <artifactId>code-generator</artifactId>
    <version>latest</version>
    <scope>test</scope> <!-- Or <scope>provided</scope> -->
</dependency>
```

目前暂未release, 可添加maven中央快照仓库(可能需网络代理)获取快照版本


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


### 2. 添加数据库驱动

接下来, 请确保您的项目中包含了所需要的数据库驱动。

```xml

<!-- MySQL Connector (请根据您的数据库选择) -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>latest</version>
</dependency>
```

---

## 快速开始

完成上述准备工作后, 您可以在一个测试类或 `main` 方法中使用 `GeneratorHelper` 来生成代码,
代码默认会生成到运行所在项目的`src/main/java`目录下, 可通过以下控制台输出确认生成文件路径

```java
    public static void main(String[] args) {
        GeneratorHelper.mybatisPlus("jdbc:mysql://localhost:3306/your_database",
                        "username",
                        "password")
                .execute("user", "role");
    }
```
```text
Connected to the target VM, address: '127.0.0.1:49687', transport: 'socket'
  _________                                        
 /   _____/__ __   ____  ____  ____   ______ ______
 \_____  \|  |  \_/ ___\/ ___\/ __ \ /  ___//  ___/
 /        \  |  /\  \__\  \__\  ___/ \___ \ \___ \ 
/_______  /____/  \___  >___  >___  >____  >____  >
        \/            \/    \/    \/     \/     \/ 
(ﾉ>ω<)ﾉ  Code generation complete! Let's start coding ~

generated file output dir:
D:\Project\github\luminion\demo-test\generator-test-sp3\src\main\java
Disconnected from the target VM, address: '127.0.0.1:49687', transport: 'socket'
```

可以通过链式表达, 自定义配置内容, 具体可用配置方法见
[配置说明](#配置说明)

```java
    public static void main(String[] args) {
        GeneratorHelper.mybatisPlus("jdbc:mysql://localhost:3306/your_database",
                        "username",
                        "password") // 创建mybatisPlus的生成器
                .global(e -> e.outputDir("D:\\Project\\src\\main\\java")//输出目录
                                .author("luminion") // 作者
                                .docType(DocType.SWAGGER_V3) // 文档类型(支持javadoc, swaggerV2, swaggerV3)
                        )// 全局配置
                .injection(e->e) // 注入配置
                .strategy(e->e)// 策略配置
                .special(e->e)// 特殊配置(配置内容具体取决于创建的生成器, 比如mybatisPlus相关配置)
                .controller(e->e)// controller配置
                .service(e->e)// service配置
                .serviceImpl(e->e)// serviceImpl配置
                .mapper(e->e)// mapper配置
                .mapperXml(e->e)// mapper.xml配置
                .createDTO(e->e)// 创建入参DTO配置
                .updateDTO(e->e)// 修改入参DTO配置
                .queryDTO(e->e)// 查询入参DTO配置
                .queryVO(e->e)// 查询返回值VO配置
                .importDTO(e->e)// Excel导入入参DTO配置
                .exportDTO(e->e)// Excel导出返回值VO配置
                .execute("user", "role"); // 需要生成的表
    }
```

---

## SQL-Booster 集成

`code-generator` 同样支持与 `SQL-Booster` 集成, 以生成更强大的 SQL 查询能力。

如果使用此功能, 请确保在项目中添加 `sql-booster` 依赖:

[![Maven Central](https://img.shields.io/maven-central/v/io.github.luminion/sql-booster)](https://mvnrepository.com/artifact/io.github.luminion/sql-booster)

```xml
<dependency>
    <groupId>io.github.luminion</groupId>
    <artifactId>sql-booster</artifactId>
    <version>latest</version>
</dependency>
```

**使用示例:**
```java
public class GeneratorTest {
    public static void main(String[] args) {
        // 使用mybatisPlusBooster()方法, 创建一个MyBatis-Plus和SQL-Booster结合的代码生成器
        GeneratorHelper.mybatisPlusBooster("jdbc:mysql://localhost:3306/your_database",
                        "username", 
                        "password")
                .execute("user", "role");
    }
}
```

---

## 配置说明

### `global()`全局配置

| 配置方法                              | 参数类型 | 详细说明 |
|-----------------------------------|---|---|
| `lombok(boolean)`                 | `boolean` | 启用/禁用 lombok 模型 |
| `chainModel(boolean)`             | `boolean` | 启用/禁用链式 setter |
| `serializableUID(boolean)`        | `boolean` | 添加序列化 UID |
| `serializableAnnotation(boolean)` | `boolean` | 添加 `@Serial` 注解 (需要 JDK 14+) |
| `docType(DocType)`                | `DocType` | 配置文档类型 (例如: `DocType.SWAGGER`) |
| `docLink(boolean)`                | `boolean` | 在文档注释中添加相关类链接 |
| `author(String)`                  | `String` | 设置文档作者 |
| `date(String)`                    | `String` | 指定注释日期格式 (例如: "yyyy-MM-dd") |
| `javaEEApi(JavaEEApi)`            | `JavaEEApi` | 设置 Java EE 框架 (例如: `JavaEEApi.JAKARTA`) |
| `excelApi(ExcelApi)`              | `ExcelApi` | 设置 Excel 框架 (例如: `ExcelApi.EASY_EXCEL`) |
| `outputDir(String)`               | `String` | 设置输出根目录 (全路径) |
| `openOutputDir(boolean)`          | `boolean` | 生成后是否打开输出目录 |
| `fileOverride(boolean)`           | `boolean` | 是否覆盖已有文件 (全局) |
| `parentPackage(String)`           | `String` | 设置父包名 |
| `parentPackageModule(String)`     | `String` | 设置父包模块名 |
| `validated(boolean)`              | `boolean` | 是否生成参数校验相关注解 |
| `generateInsert(boolean)`         | `boolean` | 是否生成新增方法 |
| `generateUpdate(boolean)`         | `boolean` | 是否生成更新方法 |
| `generateDelete(boolean)`         | `boolean` | 是否生成删除方法 |
| `generateVoById(boolean)`         | `boolean` | 是否生成查询方法 |
| `generateVoList(boolean)`         | `boolean` | 是否生成查询方法 |
| `generateVoPage(boolean)`         | `boolean` | 是否生成查询方法 |
| `generateImport(boolean)`         | `boolean` | 是否生成导入方法 |
| `generateExport(boolean)`         | `boolean` | 是否生成导出方法 |

### `strategy()`策略配置

| 配置方法 | 参数类型 | 详细说明 |
|---|---|---|
| `nameConverter(NameConverter)` | `NameConverter` | 数据库表/字段名转实体属性名策略 |
| `javaFieldProvider(JavaFieldProvider)` | `JavaFieldProvider` | 数据库类型转 Java 类型策略 |
| `keyWordsHandler(DatabaseKeywordsHandler)` | `DatabaseKeywordsHandler` | 数据库关键字处理器 |
| `dateType(DateType)` | `DateType` | 设置时间类型 |
| `booleanColumnRemoveIsPrefix(boolean)` | `boolean` | 是否移除 Boolean 类型字段的 'is' 前缀 |
| `superEntityColumns(String...)` | `String...` | 添加父类公共字段 |
| `ignoreColumns(String...)` | `String...` | 添加忽略字段 |
| `skipView(boolean)` | `boolean` | 是否跳过视图 |
| `tableNamePattern(String)` | `String` | 表名匹配 (正则表达式) |
| `tablePrefix(String...)` | `String...` | 添加表前缀 |
| `tableSuffix(String...)` | `String...` | 添加表后缀 |
| `fieldPrefix(String...)` | `String...` | 添加字段前缀 |
| `fieldSuffix(String...)` | `String...` | 添加字段后缀 |
| `include(String...)` | `String...` | 添加包含的表 |
| `exclude(String...)` | `String...` | 添加排除的表 |
| `editExcludeColumn(String...)` | `String...` | 添加编辑时排除的字段 |
| `editExcludeColumnsClear()` | - | 清空编辑时排除的字段 |
| `extraFieldSuffix(String, String)` | `String`, `String` | 添加额外字段后缀 (例如: `extraFieldSuffix("Like", "LIKE")`) |
| `extraFieldSuffix(Map<String, String>)` | `Map<String, String>` | 批量添加额外字段后缀 |
| `clearExtraFieldSuffix()` | - | 清空额外字段后缀 |
| `extraFieldProvider(ExtraFieldProvider)` | `ExtraFieldProvider` | 自定义额外字段提供者 |


### `injection()`注入配置

| 配置方法                                                 | 参数类型                                       | 详细说明                                                     |
| -------------------------------------------------------- | ---------------------------------------------- | ------------------------------------------------------------ |
| `beforeGenerate(BiConsumer<TableInfo, Map<String, Object>>)` | `BiConsumer<TableInfo, Map<String, Object>>` | 在生成文件之前的操作, 可通过此方法添加一些自定义操作<br />其中 tableInfo是数据库表信息<br />Map<String, Object> 是最终模板使用的参数集合 |
| `customMap(Map<String, Object>)`                         | `Map<String, Object>`                          | 添加自定义参数                                               |
| `addCustomFiles(TemplateFile...)`                        | `TemplateFile...`                              | 添加自定义模板文件                                           |




### 模型配置

所有模型配置都支持以下通用方法:

| 配置方法                    | 参数类型      | 详细说明                              |
|-------------------------|-----------|-----------------------------------|
| `nameFormat(String)`    | `String`  | 名称格式化 (例如: `%sEntity`)            |
| `subPackage(String)`    | `String`  | 模板文件子包名                           |
| `templatePath(String)`  | `String`  | 模板文件路径 (classpath 相对路径)           |
| `outputDir(String)`     | `String`  | 输出文件路径 (全路径), 不配置时默认值为全局路径+包名+子包名 |
| `fileOverride(boolean)` | `boolean` | 生成时覆盖已存在的文件                       |
| `generate(boolean)`     | `boolean` | 是否生成该文件                           |

#### `entity()` 配置

| 配置方法 | 参数类型 | 详细说明 |
|---|---|---|
| `superClass(String)` | `String` | 自定义继承的 Entity 类 (全限定名) |

#### `mapper()` 配置

| 配置方法 | 参数类型 | 详细说明 |
|---|---|---|
| `superClass(String)` | `String` | 自定义继承的 Mapper 类 (全限定名) |
| `mapperAnnotationClass(String)` | `String` | Mapper 标记注解 (全限定名) |

#### `service()` 配置

| 配置方法 | 参数类型 | 详细说明 |
|---|---|---|
| `superClass(String)` | `String` | 自定义继承的 Service 接口 (全限定名) |

#### `ServiceImpl` 配置

| 配置方法 | 参数类型 | 详细说明 |
|---|---|---|
| `superClass(String)` | `String` | 自定义继承的 ServiceImpl 类 (全限定名) |

#### `controller()` 配置

| 配置方法 | 参数类型 | 详细说明 |
|---|---|---|
| `superClass(String)` | `String` | 自定义继承的 Controller 类 (全限定名) |
| `restStyle(boolean)` | `boolean` | 是否生成 Restful 风格的 Controller |
| `crossOrigin(boolean)` | `boolean` | 是否添加 `@CrossOrigin` 注解 |
| `requestBody(boolean)` | `boolean` | 是否使用 `@RequestBody` 接收参数 |
| `pathVariable(boolean)` | `boolean` | 是否使用 `@PathVariable` |
| `hyphenStyle(boolean)` | `boolean` | 是否开启驼峰转连字符 |
| `baseUrl(String)` | `String` | Controller 请求前缀 |

---

### `special()`特殊配置
该配置内容根据不同生成器而异

| 配置方法 | 参数类型 | 详细说明 |
|---|---|---|
| `entityActiveRecord(boolean)` | `boolean` | 开启 ActiveRecord 模式 |
| `entityTableFieldAnnotation(boolean)` | `boolean` | 生成实体时, 生成字段注解 |
| `strategyIdType(IdType)` | `IdType` | 全局主键类型 |
| `strategyVersionColumnName(String)` | `String` | 乐观锁字段名 |
| `strategyLogicDeleteColumnName(String)` | `String` | 逻辑删除字段名 |
| `strategyTableFills(IFill...)` | `IFill...` | 添加表填充字段 |


## 注意事项

1. 开启文件覆盖后, 生成的代码会覆盖同名文件，请注意备份重要文件
2. 需要确保数据库连接信息正确
3. 根据实际需要调整配置参数
4. 可以通过自定义模板来满足特殊需求

## 声明

该项目中部分代码及实现源自[MyBatis-Plus](https://github.com/baomidou/mybatis-plus) 项目，特此感谢。