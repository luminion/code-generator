package io.github.luminion.generator.core.suppport;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.builder.custom.MybatisPlusBuilder;
import io.github.luminion.generator.config.custom.MybatisPlusConfig;
import io.github.luminion.generator.core.AbstractGenerator;
import io.github.luminion.generator.core.LambdaGenerator;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.util.InitializeUtils;

import java.util.function.Consumer;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MyBatisPlusSqlBoosterGenerator extends AbstractGenerator<MybatisPlusConfig, MybatisPlusBuilder> {
    public MyBatisPlusSqlBoosterGenerator(Configurer<MybatisPlusConfig> configurer) {
        super(configurer);
        configurer.getGlobalConfig().setRuntimeEnv(RuntimeEnv.MY_BATIS_PLUS_SQL_BOOSTER);
        InitializeUtils.initializeExtraFieldSuffix(configurer);
        InitializeUtils.initializeMapperSortColumn(configurer);
        InitializeUtils.initJdbcTypeConverter(configurer);
        InitializeUtils.initializeMybatisPlus(configurer);
        configurer.getControllerConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/controller.java");
        configurer.getServiceConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/service.java");
        configurer.getServiceImplConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/serviceImpl.java");
        configurer.getMapperConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/mapper.java");
        configurer.getMapperXmlConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus_sql_booster/mapperXml.java");
    }

    @Override
    public LambdaGenerator<MybatisPlusBuilder> custom(Consumer<MybatisPlusBuilder> consumer) {
        consumer.accept(new MybatisPlusBuilder(this.configurer));
        return this;
    }
}
