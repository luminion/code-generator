package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.enums.DbType;
import io.github.luminion.generator.util.DatasourceUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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