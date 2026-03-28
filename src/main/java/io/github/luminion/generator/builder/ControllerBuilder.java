package io.github.luminion.generator.builder;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.metadata.InvokeInfo;
import io.github.luminion.generator.metadata.MethodReference;
import io.github.luminion.generator.util.LambdaUtils;
import lombok.RequiredArgsConstructor;

/**
 * Controller配置构建器
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class ControllerBuilder {
    private final Configurer configurer;

    /**
     * controller父类
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder superClass(String fullyQualifiedClassName) {
        configurer.getControllerConfig().setControllerSuperClass(fullyQualifiedClassName);
        return this;
    }

    /**
     * controller父类
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder superClass(Class<?> superClassType) {
        configurer.getControllerConfig().setControllerSuperClass(superClassType.getCanonicalName());
        return this;
    }

    /**
     * 禁用@RestController，使用@Controller
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder disableRestController() {
        configurer.getControllerConfig().setRestController(false);
        return this;
    }

    /**
     * 禁用驼峰转连字符
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder disableHyphenStyle() {
        configurer.getControllerConfig().setHyphenStyle(false);
        return this;
    }

    /**
     * controller请求前缀
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder baseUrl(String url) {
        configurer.getControllerConfig().setBaseUrl(url);
        return this;
    }

    /**
     * 启用跨域注解@CrossOrigin
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder enableCrossOrigin() {
        configurer.getControllerConfig().setCrossOrigin(true);
        return this;
    }

    /**
     * 启用restful风格
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder enableRestful() {
        configurer.getControllerConfig().setRestful(true);
        return this;
    }

    /**
     * 禁用请求路径参数@PathVariable
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder disablePathVariable() {
        configurer.getControllerConfig().setPathVariable(false);
        return this;
    }

    /**
     * 禁用请求体参数@RequestBody
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder disableRequestBody() {
        configurer.getControllerConfig().setRequestBody(false);
        return this;
    }

    /**
     * 列表/分页查询使用post请求
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder enableQueryViaPost() {
        configurer.getControllerConfig().setQueryViaPost(true);
        return this;
    }

    /**
     * 返回值包装方法
     *
     * @return Controller配置构建器
     */
    public <R> ControllerBuilder returnMethod(MethodReference<Object, R> methodReference) {
        InvokeInfo invokeInfo = LambdaUtils.resolveInvokeInfo(methodReference, Object.class);
        configurer.getControllerConfig().setReturnType(invokeInfo);
        return this;
    }

    /**
     * 返回值包装方法
     *
     * @return Controller配置构建器
     */
    public <R> ControllerBuilder returnMethod(String fullyQualifiedClassName, String declarationTypeFormat, String methodInvokeFormat) {
        configurer.getControllerConfig().setReturnType(new InvokeInfo(fullyQualifiedClassName, declarationTypeFormat, methodInvokeFormat));
        return this;
    }

    /**
     * 分页包装方法
     *
     * @return Controller配置构建器
     */
    public <T, R> ControllerBuilder pageMethod(MethodReference<T, R> methodReference, Class<T> pageType) {
        InvokeInfo invokeInfo = LambdaUtils.resolveInvokeInfo(methodReference, pageType);
        configurer.getControllerConfig().setPageType(invokeInfo);
        return this;
    }

    /**
     * 分页包装方法
     *
     * @return Controller配置构建器
     */
    public ControllerBuilder pageMethod(String fullyQualifiedClassName, String declarationTypeFormat, String methodInvokeFormat) {
        configurer.getControllerConfig().setPageType(new InvokeInfo(fullyQualifiedClassName, declarationTypeFormat, methodInvokeFormat));
        return this;
    }
}