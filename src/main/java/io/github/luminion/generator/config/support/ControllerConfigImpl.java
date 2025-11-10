package io.github.luminion.generator.config.support;

import io.github.luminion.generator.SFunc;
import io.github.luminion.generator.config.Base.ControllerConfigBase;
import io.github.luminion.generator.config.ConfigSwitcher;
import io.github.luminion.generator.config.ControllerConfig;
import io.github.luminion.generator.config.Switchable;
import io.github.luminion.generator.util.ReflectUtil;

/**
 * @author luminion
 * @since 1.0.0
 */
public class ControllerConfigImpl<C extends Switchable<C>> extends ControllerConfigBase<C> {

    public ControllerConfigImpl(ConfigSwitcher<C> configSwitcher) {
        super(configSwitcher);
        configSwitcher.setControllerConfig(this);
    }
    
    @Override
    public ControllerConfig<C> superClass(Class<?> clazz) {
        this.superClass = clazz.getName();
        return this;
    }

    @Override
    public ControllerConfig<C> superClass(String superClass) {
        this.superClass = superClass;
        return this;
    }

    @Override
    public ControllerConfig<C> disableRestController() {
        this.restController = false;
        return this;
    }

    @Override
    public ControllerConfig<C> disableHyphenStyle() {
        this.hyphenStyle = false;
        return this;
    }

    @Override
    public ControllerConfig<C> baseUrl(String url) {
        if (url == null || url.isEmpty()) {
            this.baseUrl = null;
            return this;
        }
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        this.baseUrl = url;
        return this;
    }

    @Override
    public ControllerConfig<C> enableCrossOrigin() {
        this.crossOrigin = true;
        return this;
    }

    @Override
    public ControllerConfig<C> disableBatchQueryPost() {
        this.batchQueryPost = false;
        return this;
    }

    @Override
    public ControllerConfig<C> disableBatchQueryRequiredBody() {
        this.batchQueryRequiredBody = false;
        return this;
    }

    @Override
    public ControllerConfig<C> enableRestful() {
        this.restful = true;
        return this;
    }

    @Override
    public ControllerConfig<C> disablePathVariable() {
        this.pathVariable = false;
        return this;
    }

    @Override
    public ControllerConfig<C> disableRequestBody() {
        this.requestBody = false;
        return this;
    }

    @Override
    public <R> ControllerConfig<C> returnMethod(SFunc<Object, R> methodReference) {
        this.returnMethod = ReflectUtil.lambdaMethodInfo(methodReference, Object.class);
        return this;
    }

    @Override
    public <T, R> ControllerConfig<C> pageMethod(SFunc<T, R> methodReference) {
        this.pageMethod = ReflectUtil.lambdaMethodInfo(methodReference, Object.class);
        return this;
    }
}
