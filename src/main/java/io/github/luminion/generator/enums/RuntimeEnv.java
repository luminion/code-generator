package io.github.luminion.generator.enums;

/**
 * 环境
 * @author luminion
 * @since 1.0.0
 */
public enum RuntimeEnv {
    
//    MYBATIS,
    MYBATIS_PLUS,
//    SQL_BOOSTER_MY_BATIS,
    MY_BATIS_PLUS_SQL_BOOSTER,
    ;
    
    
    public static boolean isSqlBooster(RuntimeEnv env){
        return MY_BATIS_PLUS_SQL_BOOSTER.equals(env);
    }
    
    public static boolean isMyBatisPlus(RuntimeEnv env){
        return MYBATIS_PLUS.equals(env);
    }
    
}
