package io.github.luminion.generator.builder.model;

import io.github.luminion.generator.common.MethodReference;
import io.github.luminion.generator.config.model.ControllerConfig;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.util.ReflectUtils;

/**
 * @author luminion
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ControllerBuilder extends AbstractModelBuilder<ControllerConfig, ControllerBuilder> {

    public ControllerBuilder(ControllerConfig render) {
        super(render);
    }

    /**
     * 父类控制器
     *
     * @param className 父类控制器类名
     * @return this
     */
    public ControllerBuilder superClass(String className) {
        this.config.setSuperClass(className);
        return this;
    }

    /**
     * 禁用@RestController，使用@Controller（默认启用）
     *
     * @return this
     * @since 3.5.0
     */
    public ControllerBuilder restControllerDisable() {
        this.config.setRestController(false);
        return this;
    }

    /**
     * 禁用驼峰转连字符（默认启用）
     * <p>
     * 例如：managerUserActionHistory -> manager-user-action-history
     *
     * @return this
     * @since 3.5.0
     */
    public ControllerBuilder hyphenStyleDisable() {
        this.config.setHyphenStyle(false);
        return this;
    }

    /**
     * controller请求前缀
     *
     * @param url url
     * @return this
     */
    public ControllerBuilder baseUrl(String url) {
        if (url.isEmpty()) {
            this.config.setBaseUrl(url);
            return this;
        }
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        this.config.setBaseUrl(url);
        return this;
    }

    /**
     * 启用跨域注解@CrossOrigin（默认关闭）
     *
     * @return this
     */
    public ControllerBuilder crossOriginEnable() {
        this.config.setCrossOrigin(true);
        return this;
    }


    /**
     * 启用restful风格（默认关闭）
     *
     * @return this
     */
    public ControllerBuilder restfulEnable() {
        this.config.setRestful(true);
        return this;
    }

    /**
     * 禁用请求路径参数@PathVariable（默认启用）
     *
     * @return this
     */
    public ControllerBuilder pathVariableDisable() {
        this.config.setPathVariable(false);
        return this;
    }

    /**
     * 禁用请求体参数@RequestBody（默认启用）
     *
     * @return this
     */
    public ControllerBuilder requestBodyDisable() {
        this.config.setRequestBody(false);
        return this;
    }

    /**
     * 禁用批量查询使用post请求（默认启用）
     * <p>
     * 批量数据查询(list/page/excelExport)将使用GET请求
     *
     * @return this
     */
    public ControllerBuilder batchQueryPostDisable() {
        this.config.setBatchQueryPost(false);
        return this;
    }

    /**
     * 指定controller的返回结果包装类及方法
     *
     * @param methodReference 包装方法, 方法的入参数类型必须为Object类型或泛型
     * @return this
     */
    public <R> ControllerBuilder returnMethod(MethodReference<Object, R> methodReference) {
        ClassMethodPayload methodPayload = ReflectUtils.lambdaMethodInvokeInfo(methodReference, Object.class);
        this.config.setReturnMethod(methodPayload);
        return this;
    }

    ///**
    // * 指定controller返回的分页包装类及方法
    // *
    // * @param methodReference 包装方法, 方法的入参数类型必须为Service返回的分页对象类型
    // * @param pageClass Service分页返回值的类
    // * @return this
    // */
    //public <T, R> ControllerBuilder pageMethod(@NonNull MethodReference<T, R> methodReference, Class<T> pageClass) {
    //    ClassMethodPayload payload = ReflectUtils.lambdaMethodInfo(methodReference, pageClass);
    //    this.config.setPageMethod(payload);
    //    return this;
    //}

}