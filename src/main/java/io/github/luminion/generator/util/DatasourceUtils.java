package io.github.luminion.generator.util;

import io.github.luminion.generator.enums.DbType;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class DatasourceUtils {
    /**
     * 判断数据库类型
     *
     * @param str url
     * @return 类型枚举值，如果没找到，则返回 null
     */
    public static DbType getDbType(String str) {
        str= str.toLowerCase();
        if (str.contains(":mysql:") || str.contains(":cobar:")) {
            return DbType.MYSQL;
        } else if (str.contains(":oracle:")) {
            return DbType.ORACLE;
        } else if (str.contains(":postgresql:")) {
            return DbType.POSTGRE_SQL;
        } else if (str.contains(":sqlserver:")) {
            return DbType.SQL_SERVER;
        } else if (str.contains(":db2:")) {
            return DbType.DB2;
        } else if (str.contains(":mariadb:")) {
            return DbType.MARIADB;
        } else if (str.contains(":sqlite:")) {
            return DbType.SQLITE;
        } else if (str.contains(":h2:")) {
            return DbType.H2;
//        } else if (str.contains(":lealone:")) {
//            return DbType.LEALONE;
        } else if (str.contains(":kingbase:") || str.contains(":kingbase8:")) {
            return DbType.KINGBASE_ES;
        } else if (str.contains(":dm:")) {
            return DbType.DM;
        } else if (str.contains(":zenith:")) {
            return DbType.GAUSS;
        } else if (str.contains(":oscar:")) {
            return DbType.OSCAR;
        } else if (str.contains(":firebird:")) {
            return DbType.FIREBIRD;
        } else if (str.contains(":xugu:")) {
            return DbType.XU_GU;
        } else if (str.contains(":clickhouse:")) {
            return DbType.CLICK_HOUSE;
        } else if (str.contains(":sybase:")) {
            return DbType.SYBASE;
        } else {
            return DbType.OTHER;
        }
    }
}
