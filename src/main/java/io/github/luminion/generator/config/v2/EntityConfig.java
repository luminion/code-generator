package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.po.TableInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luminion
 * @since 1.0.0
 */
public class EntityConfig implements TemplateRender {
    

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();

        return data;
    }
    
    
}
