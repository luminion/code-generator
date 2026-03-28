package io.github.luminion.generator.internal.render;

import io.github.luminion.generator.annotation.RenderField;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class RenderDataCollector {
    private RenderDataCollector() {
    }

    @SneakyThrows
    public static Map<String, Object> collect(Object source) {
        Map<String, Object> data = new HashMap<>();
        for (Field declaredField : source.getClass().getDeclaredFields()) {
            RenderField annotation = declaredField.getAnnotation(RenderField.class);
            if (annotation == null) {
                continue;
            }
            declaredField.setAccessible(true);
            data.put(declaredField.getName(), declaredField.get(source));
        }
        return data;
    }
}