package io.github.luminion.generator.config.support;

import io.github.luminion.generator.config.po.TableInfo;
import io.github.luminion.generator.fill.ITemplate;

import java.util.Map;

/**
 * @author luminion
 * @since 1.0.0
 */
public class CustomConfig implements ITemplate {

    
    
    
    
    
    
    
    

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = ITemplate.super.renderData(tableInfo);
        return data;
    }
    
}
