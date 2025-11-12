package io.github.luminion.generator.util;

import io.github.luminion.generator.common.SFunc;
import io.github.luminion.generator.po.ClassMethodPayload;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射器
 *
 * @author luminion
 * @since 1.0.0
 */
public class ReflectUtils {

    /**
     * lambda方法信息
     *
     * @param methodReference lambda方法引用
     * @param parameterClass  参数类型
     * @return {@link ClassMethodPayload }
     */
    public static ClassMethodPayload lambdaMethodInfo(SFunc<?, ?> methodReference, Class<?> parameterClass) {
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
                return new ClassMethodPayload(returnMethod);
            } catch (Exception e) {
                Constructor<?> constructor = methodClass.getConstructor(parameterClass);
                return new ClassMethodPayload(constructor);
            }
        } catch (Exception e) {
            String msg = String.format("can't find constructor or method with parameter[%s] source:%s.%s() ", parameterClass.getName(), className, methodName);
            throw new IllegalStateException(msg);
        }
    }

    /**
     * 判断是否为Java核心类
     *
     * @param clazz 类
     * @return boolean 是否为Java核心类
     */
    public static boolean isJavaCoreClass(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz.getClassLoader() == null;
    }

    /**
     * 判断是否为特殊修饰符
     *
     * @param modifiers 修饰符
     * @return boolean 是否为特殊修饰符
     */
    public static boolean isSpecialModifier(int modifiers) {
        return Modifier.isStatic(modifiers)
                || Modifier.isFinal(modifiers)
                || Modifier.isNative(modifiers)
                || Modifier.isVolatile(modifiers)
                || Modifier.isTransient(modifiers)
                ;
    }

    /**
     * 获取指定类的字段映射
     *
     * @param clazz 类
     * @return {@link Map} 字段名到字段的映射
     */
    public static Map<String, Field> fieldMap(Class<?> clazz) {
        if (isJavaCoreClass(clazz)) {
            throw new IllegalArgumentException("clazz must not be java class");
        }
        Map<String, Field> map = new HashMap<>();
        while (clazz != null && Object.class != clazz && !clazz.isInterface()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (isSpecialModifier(field.getModifiers())) {
                    continue;
                }
                map.putIfAbsent(field.getName(), field);
            }
            clazz = clazz.getSuperclass();
        }
        return map;
    }


}

