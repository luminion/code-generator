package io.github.luminion.generator.core.suppport;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.builder.custom.MybatisPlusBuilder;
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
public class MyBatisPlusGenerator extends AbstractGenerator<MybatisPlusConfig, MybatisPlusBuilder> {
    public MyBatisPlusGenerator(Configurer<MybatisPlusConfig> configurer) {
        super(configurer);
        configurer.getGlobalConfig().setRuntimeEnv(RuntimeEnv.MYBATIS_PLUS);
        ControllerConfig controllerConfig = configurer.getControllerConfig();
        ClassMethodPayload classMethodPayload = new ClassMethodPayload(RuntimeClass.MYBATIS_PLUS_I_PAGE.getClassName(), 1);
        controllerConfig.setPageMethod(classMethodPayload);
        
        configurer.getEntityConfig().getTemplateFile().setTemplatePath("/templates/mybatis_plus/entity.java");

        InitializeUtils.initializeMybatisPlus(configurer);
    }

    @Override
    public LambdaGenerator<MybatisPlusBuilder> custom(Consumer<MybatisPlusBuilder> consumer) {
        consumer.accept(new MybatisPlusBuilder(this.configurer));
        return this;
    }
}
