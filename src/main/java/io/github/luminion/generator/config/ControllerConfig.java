package io.github.luminion.generator.config;

import io.github.luminion.generator.SFunc;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface ControllerConfig<C extends Switchable<C>> extends Switchable<C> {
    /**
     * 父类控制器
     *
     * @param clazz 父类控制器
     * @return this
     */
    ControllerConfig<C> superClass(Class<?> clazz);

    /**
     * 父类控制器
     *
     * @param superClass 父类控制器类名
     * @return this
     */
    ControllerConfig<C> superClass(String superClass);

    /**
     * 关闭@RestController控制器
     *
     * @return this
     * @since 3.5.0
     */
    ControllerConfig<C> disableRestController();

    /**
     * 关闭驼峰转连字符
     *
     * @return this
     * @since 3.5.0
     */
    ControllerConfig<C> disableHyphenStyle();

    /**
     * controller请求前缀
     *
     * @param url url
     * @return this
     */
    ControllerConfig<C> baseUrl(String url);

    /**
     * 跨域注解
     *
     * @return this
     */
    ControllerConfig<C> enableCrossOrigin();
    /**
     * 禁止批量数据查询使用post请求
     *
     * @return this
     */
    ControllerConfig<C> disableBatchQueryPost();

    /**
     * 批量查询body参数是否必填
     *
     * @return this
     */
    ControllerConfig<C> disableBatchQueryRequiredBody();

    /**
     * 增删查改使用restful风格
     *
     * @return this
     */
    ControllerConfig<C> enableRestful();

    /**
     * 禁用路径变量
     *
     * @return this
     */
    ControllerConfig<C> disablePathVariable();

    /**
     * 禁用消息体接收数据
     *
     * @return this
     */
    ControllerConfig<C> disableRequestBody();

    /**
     * 指定controller的返回结果包装类及方法
     *
     * @param methodReference 方法引用
     * @return this
     */
    <R> ControllerConfig<C> returnMethod(SFunc<Object, R> methodReference);

    /**
     * 指定controller返回的分页包装类及方法
     *
     * @param methodReference 方法参考
     * @return this
     */
    <T, R> ControllerConfig<C> pageMethod(SFunc<T, R> methodReference);
    
}
