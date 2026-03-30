package io.github.luminion.generator.util;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.*;
import io.github.luminion.generator.metadata.InvokeInfo;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author luminion
 * @since 1.0.0
 */
public abstract class InitializeUtils {


    public static void initJdbcTypeConverter(Configurer configurer) {
        configurer.getDataSourceConfig().setFieldTypeConverter(metaInfo -> {
            if (JdbcType.TINYINT == metaInfo.getJdbcType()) {
                return FieldTypeEnum.INTEGER;
            }
            if (JdbcType.SMALLINT == metaInfo.getJdbcType()) {
                return FieldTypeEnum.INTEGER;
            }
            return null;
        });
    }

    public static void initializeMapperOrderColumn(Configurer configurer) {
        Map<String, Boolean> orderColumnMap = configurer.getMapperConfig().getXmlOrderColumnMap();
        orderColumnMap.put("order" , false);
        orderColumnMap.put("rank" , false);
        orderColumnMap.put("sort" , false);
        orderColumnMap.put("seq" , false);
        orderColumnMap.put("sequence" , false);
        orderColumnMap.put("create_time" , true);
        orderColumnMap.put("id" , true);
    }


    public static void initializeCommandExcludeColumn(Configurer configurer) {
        Set<String> commandExcludeColumns = configurer.getCommandConfig().getCommandExcludeColumns();
        // 1. 创建时间类
        commandExcludeColumns.addAll(Arrays.asList(
                "create_time",
                "created_time",
                "create_at",
                "created_at",
                "create_date"
        ));

        // 2. 创建人类
        commandExcludeColumns.addAll(Arrays.asList(
                "create_by",
                "created_by",
                "creator",
                "creator_id",
                "create_id"
        ));

        // 3. 更新时间类
        commandExcludeColumns.addAll(Arrays.asList(
                "update_time",
                "updated_time",
                "update_at",
                "updated_at",
                "update_date",
                "modify_time"
        ));

        // 4. 更新人类
        commandExcludeColumns.addAll(Arrays.asList(
                "update_by",
                "updated_by",
                "updater",
                "updater_id",
                "update_id",
                "modify_by"
        ));
    }

    public static void initializeSuggestedExtraFieldSuffix(Configurer configurer) {
        Map<String, String> extraFieldSuffixMap = configurer.getQueryConfig().getExtraFieldSuffixMap();

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
        extraFieldSuffixMap.put("HasAnyBits" , "HAS ANY BITS");
        extraFieldSuffixMap.put("HasAllBits" , "HAS ALL BITS");
        extraFieldSuffixMap.put("HasNoBits" , "HAS NO BITS");

    }
    
    public static void initializeSupportedExtraFieldSuffix(Configurer configurer) {
        Map<String, String> extraFieldSuffixMap = configurer.getQueryConfig().getExtraFieldSuffixMap();

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

        extraFieldSuffixMap.put("HasAnyBits" , "HAS ANY BITS");
        extraFieldSuffixMap.put("HasAllBits" , "HAS ALL BITS");
        extraFieldSuffixMap.put("HasNoBits" , "HAS NO BITS");
    }

    public static void initializeMybatisPlus(Configurer configurer) {
        configurer.getGlobalConfig().setRuntimeEnv(RuntimeEnv.MYBATIS_PLUS);
        configurer.getEntityConfig().setIdType(IdType.ASSIGN_ID);
        configurer.getDataSourceConfig().setVersionColumnName("version");
        configurer.getDataSourceConfig().setLogicDeleteColumnName("deleted");
        configurer.getServiceConfig().setServiceSuperClass(RuntimeClass.MYBATIS_PLUS_I_SERVICE.getCanonicalName());
        configurer.getServiceConfig().setServiceImplSuperClass(RuntimeClass.MYBATIS_PLUS_SERVICE_IMPL.getCanonicalName());
        configurer.getServiceConfig().setPageType(RuntimeClass.MYBATIS_PLUS_I_PAGE.getCanonicalName());
        configurer.getMapperConfig().setMapperSuperClass(RuntimeClass.MYBATIS_PLUS_BASE_MAPPER.getCanonicalName());
        configurer.getMapperConfig().setMapperAnnotationClass(RuntimeClass.MYBATIS_MAPPER_ANNOTATION.getCanonicalName());
        InvokeInfo pageType = new InvokeInfo(RuntimeClass.MYBATIS_PLUS_I_PAGE.getCanonicalName(), "IPage<%s>", "%s");
        configurer.getControllerConfig().setPageType(pageType);
    }


    public static void initializeMpBooster(Configurer configurer) {
        initializeMybatisPlus(configurer);
        configurer.getGlobalConfig().setRuntimeEnv(RuntimeEnv.MP_BOOSTER);
        configurer.getServiceConfig().setServiceSuperClass(RuntimeClass.SQL_BOOSTER_MP_SERVICE.getCanonicalName());
        configurer.getServiceConfig().setServiceImplSuperClass(RuntimeClass.SQL_BOOSTER_MP_SERVICE_IMPL.getCanonicalName());
        configurer.getServiceConfig().setPageType(RuntimeClass.SQL_BOOSTER_BOOSTER_PAGE.getCanonicalName());
        configurer.getMapperConfig().setMapperSuperClass(RuntimeClass.SQL_BOOSTER_MP_MAPPER.getCanonicalName());
        InvokeInfo pageType = new InvokeInfo(RuntimeClass.SQL_BOOSTER_BOOSTER_PAGE.getCanonicalName(), "BPage<%s>", "%s");
        configurer.getControllerConfig().setPageType(pageType);
    }


}
