package io.github.luminion.generator.config.builder.custom;

import io.github.luminion.generator.common.MethodReference;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.custom.MybatisPlusConfig;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.util.ReflectUtils;
import io.github.luminion.sqlbooster.core.BoosterPage;
import lombok.NonNull;

/**
 * @author luminion
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class MybatisPlusSqlBoosterBuilder extends AbstractMybatisPlusBuilder<MybatisPlusSqlBoosterBuilder> {

    public MybatisPlusSqlBoosterBuilder(Configurer<MybatisPlusConfig> configurer) {
        super(configurer);
    }

    /**
     * 指定controller返回的分页包装类及方法
     *
     * @param methodReference 分页返回的包装方法
     * @return this
     */
    public <T, R> MybatisPlusSqlBoosterBuilder pageMethod(@NonNull MethodReference<BoosterPage<T>, R> methodReference) {
        ClassMethodPayload payload = ReflectUtils.lambdaMethodInfo(methodReference, BoosterPage.class);
        this.configurer.getControllerConfig().setPageMethod(payload);
        return this;
    }


}
