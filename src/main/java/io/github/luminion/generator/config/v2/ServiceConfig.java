package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ServiceConfig implements TemplateRender {
    protected final Configurer configurer;



    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();
     
        return data;
    }
}
