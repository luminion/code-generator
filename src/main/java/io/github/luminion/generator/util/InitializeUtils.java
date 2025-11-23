package io.github.luminion.generator.util;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.core.StrategyConfig;
import io.github.luminion.generator.config.custom.MybatisPlusConfig;
import io.github.luminion.generator.config.model.MapperXmlConfig;
import io.github.luminion.generator.enums.JavaFieldType;
import io.github.luminion.generator.enums.JdbcType;
import io.github.luminion.generator.enums.SqlKeyword;

import java.util.Map;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class InitializeUtils {


    public static void initJdbcTypeConverter(Configurer<?> configurer) {
        StrategyConfig strategyConfig = configurer.getStrategyConfig();
        strategyConfig.setJavaFieldProvider(metaInfo -> {
            if (JdbcType.TINYINT == metaInfo.getJdbcType()) {
                return JavaFieldType.INTEGER;
            }
            if (JdbcType.SMALLINT == metaInfo.getJdbcType()) {
                return JavaFieldType.INTEGER;
            }
            return null;
        });
    }
    
    public static void initializeMapperSortColumn(Configurer<?> configurer) {
        MapperXmlConfig mapperXmlConfig = configurer.getMapperXmlConfig();
        Map<String, Boolean> sortColumnMap = mapperXmlConfig.getSortColumnMap();
        sortColumnMap.put("order", false);
        sortColumnMap.put("rank", false);
        sortColumnMap.put("sort", false);
        sortColumnMap.put("seq", false);
        sortColumnMap.put("sequence", false);
        sortColumnMap.put("create_time", true);
        sortColumnMap.put("id", true);
    }


    public static void initializeExtraFieldSuffix(Configurer<?> configurer) {
        StrategyConfig strategyConfig = configurer.getStrategyConfig();
        Map<String, String> extraFieldSuffixMap = strategyConfig.getExtraFieldSuffixMap();
    
        extraFieldSuffixMap.put("Ne", "!=");
        extraFieldSuffixMap.put("Lt", "<");
        extraFieldSuffixMap.put("Le", "<=");
        extraFieldSuffixMap.put("Gt", ">");
        extraFieldSuffixMap.put("Ge", ">=");
        
        extraFieldSuffixMap.put("Like", "LIKE");
        extraFieldSuffixMap.put("NotLike", "NOT LIKE");
        
        extraFieldSuffixMap.put("In", "IN");
        extraFieldSuffixMap.put("NotIn", "NOT IN");
        
        extraFieldSuffixMap.put("IsNull", "IS NULL");
        extraFieldSuffixMap.put("IsNotNull", "IS NOT NULL");
        
        extraFieldSuffixMap.put("BitIn", "&");
        extraFieldSuffixMap.put("BitNotIn", "!&");
        
    }
    
    
    public static void initializeMybatisPlus(Configurer<MybatisPlusConfig> configurer) {
        StrategyConfig strategyConfig = configurer.getStrategyConfig();
        //strategyConfig.setIdType(IdType.ASSIGN_ID);
    }

    

}
