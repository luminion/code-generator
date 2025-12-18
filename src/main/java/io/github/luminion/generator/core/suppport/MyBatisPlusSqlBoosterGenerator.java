package io.github.luminion.generator.core.suppport;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.builder.custom.MybatisPlusSqlBoosterBuilder;
import io.github.luminion.generator.config.custom.MybatisPlusConfig;
import io.github.luminion.generator.config.model.ControllerConfig;
import io.github.luminion.generator.core.AbstractGenerator;
import io.github.luminion.generator.core.LambdaGenerator;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.util.InitializeUtils;

import java.util.function.Consumer;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MyBatisPlusSqlBoosterGenerator extends AbstractGenerator<MybatisPlusConfig, MybatisPlusSqlBoosterBuilder> {
    public MyBatisPlusSqlBoosterGenerator(Configurer<MybatisPlusConfig> configurer) {
        super(configurer);
        configurer.getGlobalConfig().setRuntimeEnv(RuntimeEnv.MY_BATIS_PLUS_SQL_BOOSTER);
        ControllerConfig controllerConfig = configurer.getControllerConfig();
        ClassMethodPayload classMethodPayload = new ClassMethodPayload(RuntimeClass.SQL_BOOSTER_BOOSTER_PAGE.getClassName(), 1);
        controllerConfig.setPageMethod(classMethodPayload);
        configurer.getEntityConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus/entity.java");
        configurer.getServiceConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/service.java");
        configurer.getServiceImplConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/serviceImpl.java");
        configurer.getMapperConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/mapper.java");
        configurer.getMapperXmlConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/mapper.xml");
        InitializeUtils.initializeMybatisPlus(configurer);
    }

    @Override
    public LambdaGenerator<MybatisPlusSqlBoosterBuilder> custom(Consumer<MybatisPlusSqlBoosterBuilder> consumer) {
        consumer.accept(new MybatisPlusSqlBoosterBuilder(this.configurer));
        return this;
    }
}
