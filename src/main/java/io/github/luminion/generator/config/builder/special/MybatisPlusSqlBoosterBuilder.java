package io.github.luminion.generator.config.builder.special;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.IdType;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.fill.IFill;
import lombok.NonNull;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MybatisPlusSqlBoosterBuilder extends AbstractSpecialBuilder<MybatisPlusSqlBoosterBuilder> {
    public MybatisPlusSqlBoosterBuilder(Configurer configurer) {
        super(configurer);
        configurer.getGlobalConfig().setRuntimeEnv(RuntimeEnv.MY_BATIS_PLUS_SQL_BOOSTER);
        configurer.getControllerConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/controller.java");
        configurer.getServiceConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/controller.java");
        configurer.getServiceImplConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/controller.java");
        configurer.getMapperConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/controller.java");
        configurer.getMapperXmlConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/controller.java");
        
    }
    
    @Override
    public MybatisPlusSqlBoosterBuilder entityActiveRecord(boolean enable) {
        return super.entityActiveRecord(enable);
    }

    @Override
    public MybatisPlusSqlBoosterBuilder entityTableFieldAnnotation(boolean enable) {
        return super.entityTableFieldAnnotation(enable);
    }

    @Override
    public MybatisPlusSqlBoosterBuilder strategyIdType(@NonNull IdType idType) {
        return super.strategyIdType(idType);
    }

    @Override
    public MybatisPlusSqlBoosterBuilder strategyLogicDeleteColumnName(@NonNull String logicDeleteColumnName) {
        return super.strategyLogicDeleteColumnName(logicDeleteColumnName);
    }
    
    @Override
    public MybatisPlusSqlBoosterBuilder strategyVersionColumnName(@NonNull String versionColumnName) {
        return super.strategyVersionColumnName(versionColumnName);
    }

    @Override
    public MybatisPlusSqlBoosterBuilder strategyTableFills(IFill... tableFills) {
        return super.strategyTableFills(tableFills);
    }

 

}
