package io.github.luminion.generator.config;

import io.github.luminion.generator.internal.render.RenderContext;
import io.github.luminion.generator.internal.render.RenderDataCollector;
import io.github.luminion.generator.metadata.TableInfo;

import java.util.Map;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface TemplateRender {

    default Map<String, Object> renderData(RenderContext context) {
        return renderData(context.getTableInfo());
    }

    /**
     * 渲染数据
     *
     * @param tableInfo 表信息
     * @return map
     * @since 1.0.0
     */
    default Map<String, Object> renderData(TableInfo tableInfo) {
        return RenderDataCollector.collect(this);
    }
}