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
     * 使用@RestController
     *
     * @param enable 是否启用
     * @return this
     * @since 3.5.0
     */
    public ControllerBuilder restController(boolean enable) {
        this.config.setRestController(enable);
        return this;
    }

    /**
     * 驼峰转连字符
     *
     * @param enable 是否启用
     * @return this
     * @since 3.5.0
     */
    public ControllerBuilder hyphenStyle(boolean enable) {
        this.config.setHyphenStyle(enable);
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
     * 跨域注解@CrossOrigin
     *
     * @param enable 是否启用
     * @return this
     */
    public ControllerBuilder crossOrigin(boolean enable) {
        this.config.setCrossOrigin(enable);
        return this;
    }


    /**
     * 增删查改使用restful风格
     *
     * @param enable 是否启用
     * @return this
     */
    public ControllerBuilder restful(boolean enable) {
        this.config.setRestful(enable);
        return this;
    }

    /**
     * 请求路径参数@PathVariable
     *
     * @param enable 是否启用
     * @return this
     */
    public ControllerBuilder pathVariable(boolean enable) {
        this.config.setPathVariable(enable);
        return this;
    }

    /**
     * 请求体参数@RequestBody
     *
     * @param enable 是否启用
     * @return this
     */
    public ControllerBuilder requestBody(boolean enable) {
        this.config.setRequestBody(enable);
        return this;
    }

    /**
     * 批量数据查询使用post请求(list/page/excelExport)
     *
     * @param enable 是否启用
     * @return this
     */
    public ControllerBuilder batchQueryPost(boolean enable) {
        this.config.setBatchQueryPost(enable);
        return this;
    }

    /**
     * 指定controller的返回结果包装类及方法
     *
     * @param methodReference 包装方法, 方法的入参数类型必须为Object类型或泛型
     * @return this
     */
    public <R> ControllerBuilder returnMethod(MethodReference<Object, R> methodReference) {
        ClassMethodPayload methodPayload = ReflectUtils.lambdaMethodInfo(methodReference, Object.class);
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