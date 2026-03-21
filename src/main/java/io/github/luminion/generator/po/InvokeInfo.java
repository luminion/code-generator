package io.github.luminion.generator.po;

import io.github.luminion.generator.util.ClassUtils;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Function;

/**
 * 方法调用信息
 * 用于生成代码模板中的类型字符串和方法调用字符串
 *
 * @author luminion
 * @since 1.0.0
 */
@Getter
public class InvokeInfo {
    private final String classSimpleName;
    private final String classCanonicalName;
    private final Function<String, String> typeFormatter;
    private final Function<String, String> invokeFormatter;

    public InvokeInfo(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        this.classSimpleName = declaringClass.getSimpleName();
        this.classCanonicalName = declaringClass.getName();
        int genericTypeCount = declaringClass.getTypeParameters().length;
        String methodName = method.getName();
        boolean isStatic = Modifier.isStatic(method.getModifiers());

        // typeFormatter
        if (genericTypeCount == 0) {
            this.typeFormatter = genericTypeString -> classSimpleName;
        } else if (genericTypeCount == 1) {
            this.typeFormatter = genericTypeString -> String.format("%s<%s>", classSimpleName, genericTypeString);
        } else {
            this.typeFormatter = genericTypeString -> String.format("%s<%s>", classSimpleName, "?");
        }

        // invokeFormatter
        if (isStatic) {
            this.invokeFormatter = params -> String.format("%s.%s(%s)", classSimpleName, methodName, params);
        } else {
            String generic = genericTypeCount == 1 ? "<>" : "";
            this.invokeFormatter = params -> String.format("new %s%s().%s(%s)", classSimpleName, generic, methodName, params);
        }
    }

    public InvokeInfo(Constructor<?> constructor) {
        Class<?> declaringClass = constructor.getDeclaringClass();
        this.classSimpleName = declaringClass.getSimpleName();
        this.classCanonicalName = declaringClass.getName();
        int genericTypeCount = declaringClass.getTypeParameters().length;

        // typeFormatter
        if (genericTypeCount == 0) {
            this.typeFormatter = genericTypeString -> classSimpleName;
        } else if (genericTypeCount == 1) {
            this.typeFormatter = genericTypeString -> String.format("%s<%s>", classSimpleName, genericTypeString);
        } else {
            this.typeFormatter = genericTypeString -> String.format("%s<%s>", classSimpleName, "?");
        }
        // typeFormatter
        String generic = genericTypeCount == 1 ? "<>" : "";
        this.invokeFormatter = params -> String.format("new %s%s(%s)", classSimpleName, generic, params);
    }

    /**
     * 调用信息
     *
     * @param classCanonicalName    类规范名称
     * @param declarationTypeFormat 声明类型格式
     * @param methodInvokeFormat    方法调用格式
     */
    public InvokeInfo(String classCanonicalName, String declarationTypeFormat, String methodInvokeFormat) {
        this.classSimpleName = ClassUtils.getSimpleName(classCanonicalName);
        this.classCanonicalName = classCanonicalName;
        this.typeFormatter = genericTypeString -> String.format(declarationTypeFormat, genericTypeString);
        this.invokeFormatter = params -> String.format(methodInvokeFormat, params);
    }


    /**
     * 声明字符串
     *
     * @param genericTypeString 泛型类型字符串
     * @return 字符串
     */
    public String toTypeString(String genericTypeString) {
        return typeFormatter.apply(genericTypeString);
    }

    /**
     * 调用字符串
     *
     * @param params 参数
     * @return 方法参数字符串
     */
    public String toInvokeString(String params) {
        return invokeFormatter.apply(params);
    }

}