package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.po.TableInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;


/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class GlobalConfig {

    /**
     * 自定义配置渲染数据
     */
    protected Map<String, Object> customRenderData = new HashMap<>();

    /**
     * 自定义渲染逻辑
     */
    protected BiConsumer<TableInfo, Map<String, Object>> customRenderLogic;


}