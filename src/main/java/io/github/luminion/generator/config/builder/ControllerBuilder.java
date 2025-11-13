package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.common.SFunc;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.config.base.ControllerConfig;
import io.github.luminion.generator.util.ReflectUtils;

/**
 * @author luminion
 * @since 1.0.0
 */
public class ControllerBuilder {
    private final ControllerConfig controllerConfig;

    public ControllerBuilder(ControllerConfig config) {
        this.controllerConfig = config;
    }

    /**
     * 父类控制器
     *
     * @param clazz 父类控制器
     * @return this
     */
    public ControllerBuilder superClass(Class<?> clazz) {
        return superClass(clazz.getName());
    }

    /**
     * 父类控制器
     *
     * @param superClass 父类控制器类名
     * @return this
     */
    public ControllerBuilder superClass(String superClass) {
        this.controllerConfig.setSuperClass(superClass);
        return this;
    }

    /**
     * 关闭@RestController控制器
     *
     * @return this
     * @since 3.5.0
     */
    public ControllerBuilder disableRestController() {
        this.controllerConfig.setRestController(false);
        return this;
    }

    /**
     * 关闭驼峰转连字符
     *
     * @return this
     * @since 3.5.0
     */
    public ControllerBuilder disableHyphenStyle() {
        this.controllerConfig.setHyphenStyle(false);
        return this;
    }

    /**
     * controller请求前缀
     *
     * @param url url
     * @return this
     */
    public ControllerBuilder baseUrl(String url) {
        if (url == null || url.isEmpty()) {
            this.controllerConfig.setBaseUrl(url);
            return this;
        }
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        this.controllerConfig.setBaseUrl(url); 
        return this;
    }

    /**
     * 跨域注解
     *
     * @return this
     */
    public ControllerBuilder enableCrossOrigin() {
        this.controllerConfig.setCrossOrigin(true);
        return this;
    }

    /**
     * 禁止批量数据查询使用post请求
     *
     * @return this
     */
    public ControllerBuilder disableBatchQueryPost() {
        this.controllerConfig.setBatchQueryPost(false);
        return this;
    }


    /**
     * 增删查改使用restful风格
     *
     * @return this
     */
    public ControllerBuilder enableRestful() {
        this.controllerConfig.setRestful(true);
        return this;
    }

    /**
     * 禁用路径变量
     *
     * @return this
     */
    public ControllerBuilder disablePathVariable() {
        this.controllerConfig.setPathVariable(false);
        return this;
    }

    /**
     * 禁用消息体接收数据
     *
     * @return this
     */
    public ControllerBuilder disableRequestBody() {
        this.controllerConfig.setRequestBody(false);
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
        this.controllerConfig.setReturnMethod(methodPayload);
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
//        this.controllerConfig.setPageMethod(payload);
//        return this;
//    }
    
}
