package io.github.luminion.generator.builder.v2;

import io.github.luminion.generator.common.MethodReference;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.po.InvokeInfo;
import io.github.luminion.generator.util.ReflectUtils;
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
      * @param CanonicalName 父类规范名称
     * @return this
     */
    public ControllerBuilder superClass(String CanonicalName) {
        configurer.getControllerConfig().setControllerSuperClass(CanonicalName);
        return this;
    }

    /**
     * controller父类
     *
     * @param clazz 父类
     * @return this
     */
    public ControllerBuilder superClass(Class<?> clazz) {
        configurer.getControllerConfig().setControllerSuperClass(clazz.getCanonicalName());
        return this;
    }

    /**
     * 禁用@RestController，使用@Controller
     *
     * @return this
     */
    public ControllerBuilder disableRestController() {
        configurer.getControllerConfig().setRestController(false);
        return this;
    }

    /**
     * 禁用驼峰转连字符
     *
     * @return this
     */
    public ControllerBuilder disableHyphenStyle() {
        configurer.getControllerConfig().setHyphenStyle(false);
        return this;
    }

    /**
     * controller请求前缀
     *
     * @param url url
     * @return this
     */
    public ControllerBuilder baseUrl(String url) {
        configurer.getControllerConfig().setBaseUrl(url);
        return this;
    }

    /**
     * 启用跨域注解@CrossOrigin
     *
     * @return this
     */
    public ControllerBuilder enableCrossOrigin() {
        configurer.getControllerConfig().setCrossOrigin(true);
        return this;
    }

    /**
     * 启用restful风格
     *
     * @return this
     */
    public ControllerBuilder enableRestful() {
        configurer.getControllerConfig().setRestful(true);
        return this;
    }

    /**
     * 禁用请求路径参数@PathVariable
     *
     * @return this
     */
    public ControllerBuilder disablePathVariable() {
        configurer.getControllerConfig().setPathVariable(false);
        return this;
    }

    /**
     * 禁用请求体参数@RequestBody
     *
     * @return this
     */
    public ControllerBuilder disableRequestBody() {
        configurer.getControllerConfig().setRequestBody(false);
        return this;
    }

    /**
     * 列表/分页查询使用post请求
     *
     * @return this
     */
    public ControllerBuilder enableQueryViaPost() {
        configurer.getControllerConfig().setQueryViaPost(false);
        return this;
    }

    /**
     * 返回值包装方法
     *
     * @param methodReference 方法引用
     * @return Controller配置构建器
     */
    public <R> ControllerBuilder returnMethod(MethodReference<Object, R> methodReference) {
        InvokeInfo invokeInfo = ReflectUtils.lambdaMethodInvokeInfo(methodReference, Object.class);
        configurer.getControllerConfig().setReturnType(invokeInfo);
        return this;
    }

    /**
     * 返回值包装方法
     *
     * @param classCanonicalName    类规范名称
     * @param declarationTypeFormat 声明类型格式
     * @param methodInvokeFormat    方法调用格式
     * @return Controller配置构建器
     */
    public <R> ControllerBuilder returnMethod(String classCanonicalName, String declarationTypeFormat, String methodInvokeFormat) {
        configurer.getControllerConfig().setReturnType(new InvokeInfo(classCanonicalName, declarationTypeFormat, methodInvokeFormat));
        return this;
    }

    /**
     * 分页包装方法
     *
     * @param methodReference 方法引用
     * @param pageClass       方法参考
     * @return Controller配置构建器
     */
    public <T, R> ControllerBuilder pageMethod(MethodReference<T, R> methodReference, Class<T> pageClass) {
        InvokeInfo invokeInfo = ReflectUtils.lambdaMethodInvokeInfo(methodReference, pageClass);
        configurer.getControllerConfig().setReturnType(invokeInfo);
        return this;
    }

    /**
     * 分页包装方法
     *
     * @param classCanonicalName    类规范名称
     * @param declarationTypeFormat 声明类型格式
     * @param methodInvokeFormat    方法调用格式
     * @return Controller配置构建器
     */
    public ControllerBuilder pageMethod(String classCanonicalName, String declarationTypeFormat, String methodInvokeFormat) {
        configurer.getControllerConfig().setReturnType(new InvokeInfo(classCanonicalName, declarationTypeFormat, methodInvokeFormat));
        return this;
    }

}
