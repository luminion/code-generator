package io.github.luminion.generator.config.core;

import io.github.luminion.generator.common.DatabaseKeywordsHandler;
import io.github.luminion.generator.common.FieldTypeConverter;
import io.github.luminion.generator.common.NamingConverter;
import io.github.luminion.generator.common.support.DefaultNamingConverter;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.enums.DbType;
import io.github.luminion.generator.enums.ColumnFillStrategy;
import io.github.luminion.generator.util.DatasourceUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
@Slf4j
public class DataSourceConfig {
    /**
     * 驱动连接的URL
     */
    private final String url;
    /**
     * 数据库连接用户名
     */
    private final String username;
    /**
     * 数据库连接密码
     */
    private final String password;
    /**
     * 数据库类型
     */
    private final DbType dbType;

    /**
     * 数据库schema名
     */
    private String schema;

    /**
     * java日期类型
     */
    private DateType dateType = DateType.TIME_PACK;

    /**
     * 数据库表明/字段名转化到实体类名/属性名的转化器
     */
    private NamingConverter namingConverter = new DefaultNamingConverter();

    /**
     * 数据库字段类型转化为java字段类型的方式
     */
    private FieldTypeConverter fieldTypeConverter;

    /**
     * 数据库关键字处理器
     */
    private DatabaseKeywordsHandler keyWordsHandler;

    /**
     * 是否跳过视图
     */
    private boolean skipView;

    /**
     * Boolean类型字段是否移除is前缀<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private boolean booleanColumnRemoveIsPrefix;
    /**
     * 模糊查询包含的表名, 需要自行拼接(%)
     */
    private String tableNamePattern;

    /**
     * 需要包含的表名（与exclude二选一配置）
     */
    private Set<String> includeTables = new LinkedHashSet<>();

    /**
     * 需要排除的表名
     */
    private Set<String> excludeTables = new LinkedHashSet<>();

    /**
     * 过滤表前缀
     * example: addTablePrefix("t_")
     * result: t_simple -> Simple
     */
    private Set<String> tablePrefixes = new LinkedHashSet<>();

    /**
     * 过滤表后缀
     * example: addTableSuffix("_0")
     * result: t_simple_0 -> Simple
     */
    private Set<String> tableSuffixes = new LinkedHashSet<>();

    /**
     * 自定义实体父类公共字段
     */
    private Set<String> commonColumns = new LinkedHashSet<>();
    /**
     * 自定义忽略字段
     */
    private Set<String> ignoreColumns = new LinkedHashSet<>();

    /**
     * 过滤字段前缀
     * example: addColumnPrefix("is_")
     * result: is_deleted -> deleted
     */
    private Set<String> columnPrefixes = new LinkedHashSet<>();

    /**
     * 过滤字段后缀
     * example: addColumnSuffix("_flag")
     * result: deleted_flag -> deleted
     */
    private Set<String> columnSuffixes = new LinkedHashSet<>();

    /**
     * 自动填充字段(对应mybatis-plus的FiledFill)
     */
    private Map<String, ColumnFillStrategy> columnFillMap = new HashMap<>();
    /**
     * 乐观锁字段名称(数据库字段)
     */
    protected String versionColumnName;
    /**
     * 逻辑删除字段名称(数据库字段)
     */
    protected String logicDeleteColumnName;



    public DataSourceConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.dbType = DatasourceUtils.getDbType(url);
    }

    public String getSchema() {
        DbType dbType = getDbType();
        if (this.schema != null) {
            return this.schema;
        }
        if (DbType.POSTGRE_SQL == dbType) {
            //pg 默认 schema=public
            return "public";
        } else if (DbType.KINGBASE_ES == dbType) {
            //kingbase 默认 schema=PUBLIC
            return "PUBLIC";
        } else if (DbType.DB2 == dbType) {
            //db2 默认 schema=current schema
            return "current schema";
        } else if (DbType.ORACLE == dbType) {
            //oracle 默认 schema=username
            return this.username.toUpperCase();
        }
        return null;
    }

    /**
     * 排除表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     */
    public boolean matchExcludeTable(String tableName) {
        return matchTable(tableName, this.excludeTables);
    }

    /**
     * 包含表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     */
    public boolean matchIncludeTable(String tableName) {
        return matchTable(tableName, this.includeTables);
    }


    /**
     * 表名匹配
     *
     * @param tableName   表名
     * @param matchTables 匹配集合
     * @return 是否匹配
     */
    private boolean matchTable(String tableName, Set<String> matchTables) {
        return matchTables.stream().anyMatch(t -> tableNameMatches(t, tableName));
    }

    /**
     * 表名匹配
     *
     * @param matchTableName 匹配表名
     * @param dbTableName    数据库表名
     * @return 是否匹配
     */
    private boolean tableNameMatches(String matchTableName, String dbTableName) {
        return matchTableName.equalsIgnoreCase(dbTableName) || StringUtils.matches(matchTableName, dbTableName);
    }

    /**
     * 匹配父类字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     */
    public boolean matchCommonColumns(String fieldName) {
        // 公共字段判断忽略大小写【 部分数据库大小写不敏感 】
        return commonColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    /**
     * 匹配忽略字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     */
    public boolean matchIgnoreColumns(String fieldName) {
        return ignoreColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }



    /**
     * 创建数据库连接对象
     * 这方法建议只调用一次，毕竟只是代码生成，用一个连接就行。
     *
     * @return Connection
     */
    public Connection createConnection() {
        try {
            Properties properties = new Properties();
            properties.put("user", username);
            properties.put("password", password);
            // 使用元数据查询方式时，有些数据库需要增加属性才能读取注释
            switch (this.getDbType()) {
                case MYSQL:
                    properties.put("remarks", "true");
                    properties.put("useInformationSchema", "true");
                    break;
                case ORACLE:
                    properties.put("remarks", "true");
                    properties.put("remarksReporting", "true");
                    break;
                default:
                    break;
            }
            Connection connection = DriverManager.getConnection(url, properties);
            if (StringUtils.isBlank(this.schema)) {
                try {
                    this.schema = connection.getSchema();
                } catch (Exception exception) {
                    // ignore 老古董1.7以下的驱动不支持.
                    log.debug("获取schemaName失败", exception);
                }
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}