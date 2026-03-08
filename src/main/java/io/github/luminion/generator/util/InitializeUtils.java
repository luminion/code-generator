package io.github.luminion.generator.util;

import com.baomidou.mybatisplus.annotation.IdType;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.base.StrategyConfig;
import io.github.luminion.generator.config.special.MybatisPlusConfig;
import io.github.luminion.generator.config.model.MapperXmlConfig;
import io.github.luminion.generator.enums.JavaFieldType;
import io.github.luminion.generator.enums.JdbcType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class InitializeUtils {


    public static void initJdbcTypeConverter(ConfigCollector<?> configCollector) {
        StrategyConfig strategyConfig = configCollector.getStrategyConfig();
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

    public static void initializeMapperSortColumn(ConfigCollector<?> configCollector) {
        MapperXmlConfig mapperXmlConfig = configCollector.getMapperXmlConfig();
        Map<String, Boolean> sortColumnMap = mapperXmlConfig.getSortColumnMap();
        sortColumnMap.put("order" , false);
        sortColumnMap.put("rank" , false);
        sortColumnMap.put("sort" , false);
        sortColumnMap.put("seq" , false);
        sortColumnMap.put("sequence" , false);
        sortColumnMap.put("create_time" , true);
        sortColumnMap.put("id" , true);
    }


    public static void initializeDtoExcludeColumn(ConfigCollector<?> configCollector) {
        Set<String> editExcludeColumns = configCollector.getStrategyConfig().getEditExcludeColumns();
        // 1. 创建时间类
        editExcludeColumns.addAll(Arrays.asList(
                "create_time",      // 您的规范
                "created_time",
                "create_at",
                "created_at",
                "create_date",
                "gmt_create"        // 阿里系
        ));

        // 2. 创建人类
        editExcludeColumns.addAll(Arrays.asList(
                "create_by",        // 您的规范
                "created_by",
                "creator",
                "creator_id",
                "create_id"
        ));

        // 3. 更新时间类
        editExcludeColumns.addAll(Arrays.asList(
                "update_time",      // 您的规范
                "updated_time",
                "update_at",
                "updated_at",
                "update_date",
                "modify_time",
                "gmt_modified"      // 阿里系
        ));

        // 4. 更新人类
        editExcludeColumns.addAll(Arrays.asList(
                "update_by",        // 您的规范
                "updated_by",
                "updater",
                "updater_id",
                "update_id",
                "modify_by"
        ));
        //
        //// 5. 逻辑删除类 (通常不需要前端编辑)
        //editExcludeColumns.addAll(Arrays.asList(
        //        "deleted",          // 您的规范
        //        "is_deleted",
        //        "del_flag",
        //        "delete_flag",
        //        "remove_flag"
        //));
        //
        //// 6. 乐观锁/版本类 (通常由后端自动处理)
        //editExcludeColumns.addAll(Arrays.asList(
        //        "version",          // 您的规范
        //        "lock_version",
        //        "opt_lock",
        //        "revision"
        //));
        //
        //// 7. 租户隔离类 (严禁前端篡改)
        //editExcludeColumns.addAll(Arrays.asList(
        //        "tenant_id",
        //        "corp_id",
        //        "company_id"
        //));
    }

    public static void initializeExtraFieldSuffix(ConfigCollector<?> configCollector) {
        StrategyConfig strategyConfig = configCollector.getStrategyConfig();
        Map<String, String> extraFieldSuffixMap = strategyConfig.getExtraFieldSuffixMap();

        //extraFieldSuffixMap.put("Ne" , "!=");

        extraFieldSuffixMap.put("Lt" , "<");
        extraFieldSuffixMap.put("Gt" , ">");

        extraFieldSuffixMap.put("Lte" , "<=");
        extraFieldSuffixMap.put("Gte" , ">=");

        extraFieldSuffixMap.put("Like" , "LIKE");
        //extraFieldSuffixMap.put("NotLike" , "NOT LIKE");

        extraFieldSuffixMap.put("In" , "IN");
        //extraFieldSuffixMap.put("NotIn" , "NOT IN");
        //
        //extraFieldSuffixMap.put("IsNull" , "IS NULL");
        //extraFieldSuffixMap.put("IsNotNull" , "IS NOT NULL");
        //
        //extraFieldSuffixMap.put("BitAny" , "BIT ANY");
        //extraFieldSuffixMap.put("BitAll" , "BIT ALL");
        //extraFieldSuffixMap.put("BitNone" , "BIT NONE");

    }
    
    public static Map<String,String> getFullExtraFieldSuffixMap() {
        Map<String, String> extraFieldSuffixMap = new HashMap<>();

        extraFieldSuffixMap.put("Ne", "!=");

        extraFieldSuffixMap.put("Lt", "<");
        extraFieldSuffixMap.put("Gt", ">");

        extraFieldSuffixMap.put("Lte", "<=");
        extraFieldSuffixMap.put("Gte", ">=");

        extraFieldSuffixMap.put("Like", "LIKE");
        extraFieldSuffixMap.put("NotLike", "NOT LIKE");

        extraFieldSuffixMap.put("In", "IN");
        extraFieldSuffixMap.put("NotIn", "NOT IN");

        extraFieldSuffixMap.put("IsNull", "IS NULL");
        extraFieldSuffixMap.put("IsNotNull", "IS NOT NULL");

        extraFieldSuffixMap.put("BitAny", "BIT ANY");
        extraFieldSuffixMap.put("BitAll", "BIT ALL");
        extraFieldSuffixMap.put("BitNone", "BIT NONE");
        return extraFieldSuffixMap;
    }


    public static void initializeMybatisPlus(ConfigCollector<MybatisPlusConfig> configCollector) {
        MybatisPlusConfig customConfig = configCollector.getSpecialConfig();
        customConfig.setIdType(IdType.ASSIGN_ID);
        customConfig.setVersionColumnName("version");
        customConfig.setLogicDeleteColumnName("deleted");
    }


}
