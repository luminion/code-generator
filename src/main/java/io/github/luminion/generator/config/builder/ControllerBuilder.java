package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.common.SFunc;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.util.ReflectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class ControllerBuilder {
    private final Configurer configurer;

    /**
     * 父类控制器
     *
     * @param className 父类控制器类名
     * @return this
     */
    public ControllerBuilder superClass(String className) {
        this.configurer.getControllerConfig().setSuperClass(className);
        return this;
    }

    /**
     * 关闭@RestController控制器
     *
     * @return this
     * @since 3.5.0
     */
    public ControllerBuilder disableRestController() {
        this.configurer.getControllerConfig().setRestController(false);
        return this;
    }

    /**
     * 关闭驼峰转连字符
     *
     * @return this
     * @since 3.5.0
     */
    public ControllerBuilder disableHyphenStyle() {
        this.configurer.getControllerConfig().setHyphenStyle(false);
        return this;
    }

    /**
     * controller请求前缀
     *
     * @param url url
     * @return this
     */
    public ControllerBuilder baseUrl(@NonNull String url) {
        if (url.isEmpty()) {
            this.configurer.getControllerConfig().setBaseUrl(url);
            return this;
        }
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        this.configurer.getControllerConfig().setBaseUrl(url);
        return this;
    }

    /**
     * 跨域注解
     *
     * @return this
     */
    public ControllerBuilder enableCrossOrigin() {
        this.configurer.getControllerConfig().setCrossOrigin(true);
        return this;
    }




    /**
     * 增删查改使用restful风格
     *
     * @return this
     */
    public ControllerBuilder enableRestful() {
        this.configurer.getControllerConfig().setRestful(true);
        return this;
    }

    /**
     * 禁用路径变量
     *
     * @return this
     */
    public ControllerBuilder disablePathVariable() {
        this.configurer.getControllerConfig().setPathVariable(false);
        return this;
    }

    /**
     * 禁用消息体接收数据
     *
     * @return this
     */
    public ControllerBuilder disableRequestBody() {
        this.configurer.getControllerConfig().setRequestBody(false);
        return this;
    }

    /**
     * 禁止批量数据查询使用post请求
     *
     * @return this
     */
    public ControllerBuilder disableBatchQueryPost() {
        this.configurer.getControllerConfig().setBatchQueryPost(false);
        return this;
    }

    /**
     * 指定controller的返回结果包装类及方法
     *
     * @param methodReference 方法引用
     * @return this
     */
    public <R> ControllerBuilder returnMethod(SFunc<Object, R> methodReference) {
        ClassMethodPayload methodPayload = ReflectUtils.lambdaMethodInfo(methodReference, Object.class);
        this.configurer.getControllerConfig().setReturnMethod(methodPayload);
        return this;
    }

//    /**
//     * 指定controller返回的分页包装类及方法
//     *
//     * @param methodReference 包装方法, 接手的其中一个参数参数类型需要为分页对象
//     * @param pageClass 返回的分页类
//     * @return this
//     */
//    public <T, R> ControllerBuilder pageMethod(SFunc<T, R> methodReference, Class<T> pageClass) {
//        ClassMethodPayload payload = ReflectUtils.lambdaMethodInfo(methodReference, pageClass);
//        this.configurer.getControllerConfig().setPageMethod(payload);
//        return this;
//    }

}
