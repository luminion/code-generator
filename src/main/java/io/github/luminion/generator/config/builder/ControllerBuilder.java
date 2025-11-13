package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.common.MethodReference;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.po.ClassPayload;
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
     * 名称格式化
     * <p> 
     * 会使用{@link String#format(String, Object...)}进行转化, 其中会传入当前表转化后的名称,
     * 例如: 表名为user,  %sEntity  -> UserEntity
     * @param formatPattern 模板文件名
     * @return this
     */
    public ControllerBuilder nameFormat(@NonNull String nameFormat) {
        this.configurer.getControllerConfig().getTemplateFile().setNameFormat(nameFormat);
        return this;
    }

    /**
     * 模板文件子包名
     * <p> 
     * 父包名通过全局配置
     * @param subPackage 子包名
     * @return this
     */
    public ControllerBuilder subPackage(@NonNull String subPackage) {
        this.configurer.getControllerConfig().getTemplateFile().setSubPackage(subPackage);
        return this;
    }

    /**
     * 模板文件路径(从classpath开始的相对路径, 不带后缀)
     * <p> 
     * /templates/entity.java.vm -> /templates/entity.java
     *
     * @param templatePath 模板文件路径
     * @return this
     */
    public ControllerBuilder templatePath(@NonNull String templatePath) {
        this.configurer.getControllerConfig().getTemplateFile().setTemplatePath(templatePath);
        return this;
    }
    
    /**
     * 输出文件路径(全路径)
     * <p> 
     * 默认为: {global.outputDir}/{package}/{module}/{subPackage}
     * @param outputDir 输出文件路径
     * @return this
     */
    public ControllerBuilder outputDir(@NonNull String outputDir) {
        this.configurer.getControllerConfig().getTemplateFile().setOutputDir(outputDir);
        return this;
    }

    /**
     * 生成时覆盖已存在的文件
     *
     * @return this
     */
    public ControllerBuilder fileOverrideOn() {
        this.configurer.getControllerConfig().getTemplateFile().setFileOverride(true);
        return this;
    }


    /**
     * 父类控制器
     *
     * @param className 父类控制器类名
     * @return this
     */
    public ControllerBuilder superClass(@NonNull String className) {
        this.configurer.getControllerConfig().setSuperClass(className);
        return this;
    }

    /**
     * 使用@RestController
     *
     * @return this
     * @since 3.5.0
     */
    public ControllerBuilder restControllerOff() {
        this.configurer.getControllerConfig().setRestController(false);
        return this;
    }

    /**
     * 驼峰转连字符
     *
     * @return this
     * @since 3.5.0
     */
    public ControllerBuilder hyphenStyleOff() {
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
     * 跨域注解@CrossOrigin
     *
     * @return this
     */
    public ControllerBuilder crossOriginOn() {
        this.configurer.getControllerConfig().setCrossOrigin(true);
        return this;
    }


    /**
     * 增删查改使用restful风格
     *
     * @return this
     */
    public ControllerBuilder restfulOn() {
        this.configurer.getControllerConfig().setRestful(true);
        return this;
    }

    /**
     * 请求路径参数@PathVariable
     *
     * @return this
     */
    public ControllerBuilder pathVariableOff() {
        this.configurer.getControllerConfig().setPathVariable(false);
        return this;
    }

    /**
     * 请求体参数@RequestBody
     *
     * @return this
     */
    public ControllerBuilder requestBodyOff() {
        this.configurer.getControllerConfig().setRequestBody(false);
        return this;
    }

    /**
     * 批量数据查询使用post请求(list/page/excelExport)
     *
     * @return this
     */
    public ControllerBuilder batchQueryPostOff() {
        this.configurer.getControllerConfig().setBatchQueryPost(false);
        return this;
    }

    /**
     * 指定controller的返回结果包装类及方法
     *
     * @param methodReference 包装方法, 方法的入参数类型必须为Object类型或泛型
     * @return this
     */
    public <R> ControllerBuilder returnMethod(@NonNull MethodReference<Object, R> methodReference) {
        ClassMethodPayload methodPayload = ReflectUtils.lambdaMethodInfo(methodReference, Object.class);
        this.configurer.getControllerConfig().setReturnMethod(methodPayload);
        return this;
    }

    /**
     * 指定controller返回的分页包装类及方法
     *
     * @param methodReference 包装方法, 方法的入参数类型必须为Service返回的分页对象类型
     * @return this
     */
    public <T, R> ControllerBuilder pageMethod(@NonNull MethodReference<T, R> methodReference) {
        ClassPayload pageClassPayload = this.configurer.getGlobalConfig().getPageClassPayload();
        ClassMethodPayload payload = ReflectUtils.lambdaMethodInfo(methodReference, pageClassPayload.getClazz());
        this.configurer.getControllerConfig().setPageMethod(payload);
        return this;
    }

}
