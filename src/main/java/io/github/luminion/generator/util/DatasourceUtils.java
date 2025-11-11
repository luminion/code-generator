package io.github.luminion.generator.util;

import com.baomidou.mybatisplus.annotation.DbType;
import io.github.luminion.generator.config.common.ITypeConvert;
import io.github.luminion.generator.config.converts.*;

/**
 * @author luminion
 * @since 1.0.0
 */
public class DatasourceUtils {

    public static DbType getDbType(String url) {
        if (url.contains(":mysql:") || url.contains(":cobar:")) {
            return DbType.MYSQL;
        } else if (url.contains(":oracle:")) {
            return DbType.ORACLE;
        } else if (url.contains(":postgresql:")) {
            return DbType.POSTGRE_SQL;
        } else if (url.contains(":sqlserver:")) {
            return DbType.SQL_SERVER;
        } else if (url.contains(":db2:")) {
            return DbType.DB2;
        } else if (url.contains(":mariadb:")) {
            return DbType.MARIADB;
        } else if (url.contains(":sqlite:")) {
            return DbType.SQLITE;
        } else if (url.contains(":h2:")) {
            return DbType.H2;
//        } else if (str.contains(":lealone:")) {
//            return DbType.LEALONE;
        } else if (url.contains(":kingbase:") || url.contains(":kingbase8:")) {
            return DbType.KINGBASE_ES;
        } else if (url.contains(":dm:")) {
            return DbType.DM;
        } else if (url.contains(":zenith:")) {
            return DbType.GAUSS;
        } else if (url.contains(":oscar:")) {
            return DbType.OSCAR;
        } else if (url.contains(":firebird:")) {
            return DbType.FIREBIRD;
        } else if (url.contains(":xugu:")) {
            return DbType.XU_GU;
        } else if (url.contains(":clickhouse:")) {
            return DbType.CLICK_HOUSE;
        } else if (url.contains(":sybase:")) {
            return DbType.SYBASE;
        } else {
            return DbType.OTHER;
        }
    }


    /**
     * 查询数据库类型对应的类型转换器
     *
     * @param dbType 数据库类型
     * @return 返回转换器
     */
    public static ITypeConvert getTypeConvert(DbType dbType) {
        switch (dbType) {
            case ORACLE:
                return OracleTypeConvert.INSTANCE;
            case DB2:
                return DB2TypeConvert.INSTANCE;
            case DM:
            case GAUSS:
                return DmTypeConvert.INSTANCE;
            case KINGBASE_ES:
                return KingbaseESTypeConvert.INSTANCE;
            case OSCAR:
                return OscarTypeConvert.INSTANCE;
            case MYSQL:
            case MARIADB:
                return MySqlTypeConvert.INSTANCE;
            case POSTGRE_SQL:
                return PostgreSqlTypeConvert.INSTANCE;
            case SQLITE:
                return SqliteTypeConvert.INSTANCE;
            case SQL_SERVER:
                return SqlServerTypeConvert.INSTANCE;
            case FIREBIRD:
                return FirebirdTypeConvert.INSTANCE;
            case CLICK_HOUSE:
                return ClickHouseTypeConvert.INSTANCE;
            default:
                return null;
        }
    }
    
    
    
}
