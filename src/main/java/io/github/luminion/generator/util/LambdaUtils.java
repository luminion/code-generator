package io.github.luminion.generator.util;

import io.github.luminion.generator.metadata.InvokeInfo;
import io.github.luminion.generator.metadata.MethodReference;
import lombok.SneakyThrows;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author luminion
 * @since 1.0.0
 */
public class LambdaUtils {

    /**
     * 从可序列化的方法引用中提取 SerializedLambda 信息。
     * <p>
     * 使用 lambdaClass 为 key 做一层缓存，避免每次都反射调用 writeReplace。
     *
     * @param getter 方法引用，例如 User::getName
     * @return SerializedLambda 实例
     */
    @SneakyThrows
    private static <T, R> SerializedLambda resolveSerializedLambda(MethodReference<T, R> getter) {
        Class<?> lambdaClass = getter.getClass();
        Method writeReplaceMethod = lambdaClass.getDeclaredMethod("writeReplace");
        writeReplaceMethod.setAccessible(true);
        SerializedLambda lambda = (SerializedLambda) writeReplaceMethod.invoke(getter);
        return lambda;
    }

    /**
     * 从方法引用中获取其声明所在的类的 Class 对象。
     *
     * @param getter 方法引用
     * @return 声明该方法的类
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T, R> Class<T> resolveGetterClass(MethodReference<T, R> getter) {
        SerializedLambda serializedLambda = resolveSerializedLambda(getter);
        String className = serializedLambda.getImplClass().replace("/", ".");
        return (Class<T>) Class.forName(className);
    }

    /**
     * 从 getter 方法引用中获取其对应的属性名。
     *
     * @param getter 方法引用，例如 User::getName
     * @return 属性名，例如 "name"
     */
    public static <T, R> String resolveGetterPropertyName(MethodReference<T, R> getter) {
        String implMethodName = resolveSerializedLambda(getter).getImplMethodName();
        String name = implMethodName;

        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new IllegalArgumentException("Error parsing property name '" + implMethodName +
                    "'. Didn't start with 'is', 'get' or 'set'.");
        }
        return Introspector.decapitalize(name);
    }
    

    /**
     * lambda方法信息
     *
     * @param methodReference lambda方法引用
     * @param parameterClass  参数类型
     */
    public static InvokeInfo resolveInvokeInfo(MethodReference<?, ?> methodReference, Class<?> parameterClass) {
        String methodName = "", className = "";
        try {
            Method lambdaMethod = methodReference.getClass().getDeclaredMethod("writeReplace");
            lambdaMethod.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) lambdaMethod.invoke(methodReference);
            className = serializedLambda.getImplClass().replace("/", ".");
            methodName = serializedLambda.getImplMethodName();
            Class<?> methodClass = Class.forName(className);
            try {
                Method returnMethod = methodClass.getMethod(methodName, parameterClass);
                Class<?> returnType = returnMethod.getReturnType();
                int modifiers = returnMethod.getModifiers();
                if (!returnType.equals(methodClass) || !Modifier.isPublic(modifiers)) {
                    throw new NoSuchMethodException("no public method found which return instance of class itself");
                }
                return new InvokeInfo(returnMethod);
            } catch (Exception e) {
                Constructor<?> constructor = methodClass.getConstructor(parameterClass);
                return new InvokeInfo(constructor);
            }
        } catch (Exception e) {
            String msg = String.format("can't find constructor or method with parameter[%s] source:%s.%s() ", parameterClass.getName(), className, methodName);
            throw new IllegalStateException(msg);
        }
    }
    
}
