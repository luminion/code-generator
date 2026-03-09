package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.DatabaseKeywordsHandler;
import io.github.luminion.generator.common.FieldTypeConverter;
import io.github.luminion.generator.common.NamingConverter;
import io.github.luminion.generator.common.support.DefaultNamingConverter;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.DateType;
import io.github.luminion.generator.enums.DbType;
import io.github.luminion.generator.util.DatasourceUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
@Slf4j
public class DataSourceConfig {
    private final Configurer configurer;
    /**
     * 驱动连接的URL
     */
    protected final String url;
    /**
     * 数据库连接用户名
     */
    protected final String username;
    /**
     * 数据库连接密码
     */
    protected final String password;
    /**
     * 数据库类型
     */
    protected DbType dbType;

    /**
     * 数据库schema名
     */
    protected String schema;

    /**
     * java日期类型
     */
    private DateType dateType = DateType.TIME_PACK;


    /**
     * 数据库表明/字段名转化到实体类名/属性名的转化器
     */
    protected NamingConverter namingConverter = new DefaultNamingConverter();
    /**
     * 数据库字段类型转化为java字段类型的方式
     */
    protected FieldTypeConverter FieldTypeConverter;
    /**
     * 数据库关键字处理器
     */
    protected DatabaseKeywordsHandler keyWordsHandler;

    /**
     * 是否跳过视图（默认 false）
     */
    protected boolean skipView;

    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    protected boolean booleanColumnRemoveIsPrefix;
    /**
     * 模糊查询包含的表名, 需要自行拼接(%)
     */
    protected String tableNamePattern;
    /**
     * 自定义基础的Entity类，公共字段
     */
    protected final Set<String> superEntityColumns = new HashSet<>();
    /**
     * 自定义忽略字段
     */
    protected final Set<String> ignoreColumns = new HashSet<>();

    /**
     * 过滤表前缀
     * example: addTablePrefix("t_")
     * result: t_simple -> Simple
     */
    protected final Set<String> tablePrefix = new HashSet<>();

    /**
     * 过滤表后缀
     * example: addTableSuffix("_0")
     * result: t_simple_0 -> Simple
     */
    protected final Set<String> tableSuffix = new HashSet<>();

    /**
     * 过滤字段前缀
     * example: addFieldPrefix("is_")
     * result: is_deleted -> deleted
     */
    protected final Set<String> fieldPrefix = new HashSet<>();

    /**
     * 过滤字段后缀
     * example: addFieldSuffix("_flag")
     * result: deleted_flag -> deleted
     */
    protected final Set<String> fieldSuffix = new HashSet<>();

    /**
     * 需要包含的表名（与exclude二选一配置）
     */
    protected final Set<String> include = new HashSet<>();

    /**
     * 需要排除的表名
     */
    protected final Set<String> exclude = new HashSet<>();


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
     * 表名称匹配过滤表前缀
     *
     * @param tableName 表名称
     */
    public boolean startsWithTablePrefix(String tableName) {
        return this.tablePrefix.stream().anyMatch(tableName::startsWith);
    }


    /**
     * 排除表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     */
    public boolean matchExcludeTable(String tableName) {
        return matchTable(tableName, this.exclude);
    }

    /**
     * 包含表名匹配
     *
     * @param tableName 表名
     * @return 是否匹配
     */
    public boolean matchIncludeTable(String tableName) {
        return matchTable(tableName, this.include);
    }


    /**
     * 表名匹配
     *
     * @param tableName   表名
     * @param matchTables 匹配集合
     * @return 是否匹配
     */
    protected boolean matchTable(String tableName, Set<String> matchTables) {
        return matchTables.stream().anyMatch(t -> tableNameMatches(t, tableName));
    }

    /**
     * 表名匹配
     *
     * @param matchTableName 匹配表名
     * @param dbTableName    数据库表名
     * @return 是否匹配
     */
    protected boolean tableNameMatches(String matchTableName, String dbTableName) {
        return matchTableName.equalsIgnoreCase(dbTableName) || StringUtils.matches(matchTableName, dbTableName);
    }

    /**
     * 匹配父类字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     */
    public boolean matchSuperEntityColumns(String fieldName) {
        // 公共字段判断忽略大小写【 部分数据库大小写不敏感 】
        return superEntityColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
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