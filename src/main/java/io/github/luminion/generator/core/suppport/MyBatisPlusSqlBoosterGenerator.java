package io.github.luminion.generator.core.suppport;

import io.github.luminion.generator.builder.custom.MybatisPlusSqlBoosterBuilder;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.custom.MybatisPlusConfig;
import io.github.luminion.generator.config.model.ControllerConfig;
import io.github.luminion.generator.core.AbstractGenerator;
import io.github.luminion.generator.core.LambdaGenerator;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.util.InitializeUtils;

import java.util.function.Function;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MyBatisPlusSqlBoosterGenerator extends AbstractGenerator<MybatisPlusConfig, MybatisPlusSqlBoosterBuilder> {
    public MyBatisPlusSqlBoosterGenerator(ConfigCollector<MybatisPlusConfig> configCollector) {
        super(configCollector);
        configCollector.getGlobalConfig().setRuntimeEnv(RuntimeEnv.MY_BATIS_PLUS_SQL_BOOSTER);
        ControllerConfig controllerConfig = configCollector.getControllerConfig();
        ClassMethodPayload classMethodPayload = new ClassMethodPayload(RuntimeClass.SQL_BOOSTER_BOOSTER_PAGE.getClassName(), 1);
        controllerConfig.setPageMethod(classMethodPayload);
        configCollector.getEntityConfig().renderTemplateFile().setTemplatePath("/templates/mybatis_plus/entity.java");
        configCollector.getServiceConfig().renderTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/service.java");
        configCollector.getServiceImplConfig().renderTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/serviceImpl.java");
        configCollector.getMapperConfig().renderTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/mapper.java");
        configCollector.getMapperXmlConfig().renderTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/mapper.xml");
        InitializeUtils.initializeMybatisPlus(configCollector);
    }

    @Override
    public LambdaGenerator<MybatisPlusSqlBoosterBuilder> custom(Function<MybatisPlusSqlBoosterBuilder, MybatisPlusSqlBoosterBuilder> func) {
        func.apply(new MybatisPlusSqlBoosterBuilder(this.configCollector));
        return this;
    }
}
