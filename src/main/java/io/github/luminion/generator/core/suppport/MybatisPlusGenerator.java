package io.github.luminion.generator.core.suppport;

import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.builder.custom.MpBuilder;
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
public class MybatisPlusGenerator extends AbstractGenerator<MybatisPlusConfig, MpBuilder> {
    public MybatisPlusGenerator(ConfigCollector<MybatisPlusConfig> configCollector) {
        super(configCollector);
        configCollector.getGlobalConfig().setRuntimeEnv(RuntimeEnv.MYBATIS_PLUS);
        ControllerConfig controllerConfig = configCollector.getControllerConfig();
        ClassMethodPayload classMethodPayload = new ClassMethodPayload(RuntimeClass.MYBATIS_PLUS_I_PAGE.getClassName(), 1);
        controllerConfig.setPageMethod(classMethodPayload);
        
        configCollector.getEntityConfig().renderTemplateFile().setTemplatePath("/templates/mybatis_plus/entity.java");

        InitializeUtils.initializeMybatisPlus(configCollector);
    }

    @Override
    public LambdaGenerator<MpBuilder> custom(Function<MpBuilder, MpBuilder> func) {
        func.apply(new MpBuilder(this.configCollector));
        return this;
    }
}
