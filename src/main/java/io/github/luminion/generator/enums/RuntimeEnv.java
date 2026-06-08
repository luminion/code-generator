package io.github.luminion.generator.enums;

/**
 * 环境
 * @author luminion
 * @since 1.0.0
 */
public enum RuntimeEnv {

    MYBATIS,
    MYBATIS_PAGE_HELPER,
    MYBATIS_PLUS,
    MYBATIS_BOOSTER,
    MP_BOOSTER,
    MYBATIS_SQL_BOOSTER,
    MYBATIS_PAGE_HELPER_SQL_BOOSTER,
    MP_SQL_BOOSTER,
    ;

    public boolean isMybatisPlusBased() {
        return this == MYBATIS_PLUS || this == MP_BOOSTER || this == MP_SQL_BOOSTER;
    }

    public boolean isPlainMybatisBased() {
        return this == MYBATIS
                || this == MYBATIS_PAGE_HELPER
                || this == MYBATIS_BOOSTER
                || this == MYBATIS_SQL_BOOSTER
                || this == MYBATIS_PAGE_HELPER_SQL_BOOSTER;
    }

    public boolean isSqlBooster() {
        return isBoosterIntegrated() || isSqlBoosterContext();
    }

    public boolean isBoosterIntegrated() {
        return this == MYBATIS_BOOSTER || this == MP_BOOSTER;
    }

    public boolean isSqlBoosterContext() {
        return this == MYBATIS_SQL_BOOSTER
                || this == MYBATIS_PAGE_HELPER_SQL_BOOSTER
                || this == MP_SQL_BOOSTER;
    }

    public boolean isPageHelperBased() {
        return this == MYBATIS_PAGE_HELPER || this == MYBATIS_PAGE_HELPER_SQL_BOOSTER;
    }

    public boolean isRowBoundsBased() {
        return isPlainMybatisBased() && !isPageHelperBased() && !isBoosterIntegrated();
    }
}
