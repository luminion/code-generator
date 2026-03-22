package io.github.luminion.generator.common;

import io.github.luminion.generator.metadata.TableInfo;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface TemplateRender {

    /**
     * 渲染数据, 这一步应该只渲染信息, 不能再修改配置及表信息
     *
     * @param tableInfo 表信息
     * @return map
     * @since 1.0.0
     */
    @SneakyThrows
    default Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();
        for (Field declaredField : getClass().getDeclaredFields()) {
            RenderField annotation = declaredField.getAnnotation(RenderField.class);
            if (annotation != null) {
                declaredField.setAccessible(true);
                data.put(declaredField.getName(), declaredField.get(this));
            }
        }
        return data;
    }

}
