package io.github.luminion.generator.builder.special;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.luminion.generator.common.MethodReference;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.special.MybatisPlusConfig;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.util.ReflectUtils;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MybatisPlusBuilder extends AbstractMpBuilder<MybatisPlusBuilder> {

    public MybatisPlusBuilder(ConfigCollector<MybatisPlusConfig> configCollector) {
        super(configCollector);
    }

    /**
     * 指定controller返回的分页包装类及方法
     *
     * @param methodReference 包装方法, 方法的入参数类型必须为IPage
     * @return this
     */
    public <T, R> MybatisPlusBuilder pageMethod(MethodReference<IPage<T>, R> methodReference) {
        ClassMethodPayload payload = ReflectUtils.lambdaMethodInfo(methodReference, IPage.class);
        this.configCollector.getControllerConfig().setPageMethod(payload);
        return this;
    }


}
