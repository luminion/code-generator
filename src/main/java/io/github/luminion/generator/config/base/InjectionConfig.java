package io.github.luminion.generator.config.base;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class InjectionConfig implements TemplateRender {

    /**
     * 生成文件之前的操作
     */
    protected BiConsumer<TableInfo, Map<String, Object>> beforeGenerate;

    /**
     * 自定义配置 Map 对象
     */
    protected Map<String, Object> customMap = new HashMap<>();

    /**
     * 自定义模板文件列表
     *
     * @since 3.5.3
     */
    protected final List<TemplateFile> templateFiles = new ArrayList<>();

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        return customMap;
    }

}
