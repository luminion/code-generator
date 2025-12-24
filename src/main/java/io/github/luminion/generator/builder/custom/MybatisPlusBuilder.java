package io.github.luminion.generator.builder.custom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.luminion.generator.common.MethodReference;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.custom.MybatisPlusConfig;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.util.ReflectUtils;
import lombok.NonNull;

/**
 * @author luminion
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class MybatisPlusBuilder extends AbstractMybatisPlusBuilder<MybatisPlusBuilder> {

    public MybatisPlusBuilder(Configurer<MybatisPlusConfig> configurer) {
        super(configurer);
    }

    /**
     * 指定controller返回的分页包装类及方法
     *
     * @param methodReference 包装方法, 方法的入参数类型必须为IPage
     * @return this
     */
    public <T, R> MybatisPlusBuilder pageMethod(@NonNull MethodReference<IPage<T>, R> methodReference) {
        ClassMethodPayload payload = ReflectUtils.lambdaMethodInfo(methodReference, IPage.class);
        this.configurer.getControllerConfig().setPageMethod(payload);
        return this;
    }


}
