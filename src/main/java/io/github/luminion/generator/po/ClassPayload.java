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

    public ClassPayload(String classPackage, String classSimpleName) {
        this.classPackage = classPackage;
        this.classSimpleName = classSimpleName;
        this.className = classPackage + "." + classSimpleName;
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
