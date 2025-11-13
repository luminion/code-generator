package io.github.luminion.generator.po;

import io.github.luminion.generator.util.ClassUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 方法承载信息
 * @author luminion
 * @since 1.0.0
 */
@Getter
@NoArgsConstructor
public class ClassMethodPayload extends ClassPayload {
    protected String methodName;
    protected int methodGenericTypeCount;
    protected boolean isStatic;
    protected boolean isConstructor;
    protected boolean isGenericMethod;
    
    @SneakyThrows
    public ClassMethodPayload(String className, String methodName, Class<?> methodParameterClass){
        super(className);
        this.methodName = methodName;
        Class<?> aClass = Class.forName(className);
        Method method = aClass.getMethod(methodName, methodParameterClass);
        this.methodGenericTypeCount = method.getGenericParameterTypes().length;
        this.isStatic = Modifier.isStatic(method.getModifiers());
        this.isConstructor = method.getName().startsWith("<init>");
    }

    public ClassMethodPayload(Method method) {
        super(method.getDeclaringClass());
        this.methodName = method.getName();
        this.methodGenericTypeCount = method.getTypeParameters().length;
        this.isStatic = Modifier.isStatic(method.getModifiers());
        this.isConstructor = method.getName().startsWith("<init>");
    }

    public ClassMethodPayload(Constructor<?> method) {
        super(method.getDeclaringClass());
        this.methodName = method.getName();
        this.methodGenericTypeCount = method.getTypeParameters().length;
        this.isStatic = Modifier.isStatic(method.getModifiers());
        this.isConstructor = true;
    }

    public boolean isMethodReady(){
        return methodName != null;
    }

    public String invokeMethodStr(String parametersStr) {
        if (methodName == null) {
            return parametersStr;
        }
        if (isStatic) {
            return String.format("%s.%s(%s)" , classSimpleName, methodName, parametersStr);
        }
        if (isConstructor) {
            return String.format("new %s%s(%s)" , classSimpleName, classGenericTypeCount == 1 ? "<>" : "" , parametersStr);
        }
        if (isGenericMethod) {
            return String.format("new %s<>().%s(%s)" , classSimpleName, methodName, parametersStr);
        }
//        if (classGenericTypeCount == 1) {
//            return String.format("new %s<%s>().%s(%s)" , classSimpleName, returnGenericTypeStr, methodName, parametersStr);
//        }
        return String.format("new %s().%s(%s)" , classSimpleName, methodName, parametersStr);
    }

}
