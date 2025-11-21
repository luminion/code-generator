package io.github.luminion.generator.po;

import io.github.luminion.generator.util.ClassUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;


/**
 * 类承载信息
 *
 * @author luminion
 * @since 1.0.0
 */
@Getter
@NoArgsConstructor
public class ClassPayload {
    protected Class<?> clazz;
    protected String classPackage;
    protected String classSimpleName;
    protected String className;
    protected int classGenericTypeCount;

    public ClassPayload(String className) {
        this.classPackage = ClassUtils.getPackage(className);
        this.classSimpleName = ClassUtils.getSimpleName(className);
        this.className = className;
    }
    public ClassPayload(String className, int classGenericTypeCount) {
        this.classPackage = ClassUtils.getPackage(className);
        this.classSimpleName = ClassUtils.getSimpleName(className);
        this.className = className;
        this.classGenericTypeCount = classGenericTypeCount;
    }

    public ClassPayload(Class<?> clazz) {
        this.clazz = clazz;
        this.classPackage = clazz.getPackage().getName();
        this.classSimpleName = clazz.getSimpleName();
        this.className = clazz.getName();
        this.classGenericTypeCount = clazz.getTypeParameters().length;
    }

    public boolean isClassReady() {
        if (classPackage == null || classSimpleName == null || className == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取返回类型的 字符串表达形式(如果有反省则匹配泛型类型)
     * <p>
     * 例: 当类为List时, {@code returnGenericTypeStr("String") => List<String>}
     * @param genericTypeStr 返回泛型的字符串表达形式
     * @return 返回的泛型类型字符串
     */
    public String returnGenericTypeStr(String... genericTypeStr) {
        if (classSimpleName == null) {
            return genericTypeStr != null && genericTypeStr.length == 1 ? genericTypeStr[0] : "Object";
        }
        if (classGenericTypeCount == 0) {
            return classSimpleName;
        }
        if (genericTypeStr == null || genericTypeStr.length == 0) {
            return String.format("%s<%s>", classSimpleName, String.join(", ", Collections.nCopies(classGenericTypeCount, "?")));
        }
        if (classGenericTypeCount == genericTypeStr.length) {
            return String.format("%s<%s>", classSimpleName, String.join(", ", genericTypeStr));
        }
        return String.format("%s<%s>", classSimpleName, String.join(", ", Collections.nCopies(classGenericTypeCount, "?")));
    }


}
