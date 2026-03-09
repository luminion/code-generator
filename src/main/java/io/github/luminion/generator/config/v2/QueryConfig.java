package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.ExtraFieldStrategy;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.common.support.DefaultExtraFieldStrategy;
import io.github.luminion.generator.po.TableInfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author luminion
 * @since 1.0.0
 */
public class QueryConfig implements TemplateRender {
    /**
     * 生成id查询方法
     */
    protected boolean generateVoById = true;
    /**
     * 批量查询相关方法及配套类
     */
    protected boolean generateVoList = true;

    /**
     * 批量查询分页相关方法及配套类
     */
    protected boolean generateVoPage = true;

    /**
     * 额外字段后缀
     */
    protected final Map<String, String> extraFieldSuffixMap = new LinkedHashMap<>();

    /**
     * 额外字段策略
     */
    protected ExtraFieldStrategy extraFieldStrategy = new DefaultExtraFieldStrategy();
    

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("generateVoById", this.generateVoById);
        data.put("generateVoList", this.generateVoList);
        data.put("generateVoPage", this.generateVoPage);


        data.put("extraFieldSuffixMap", extraFieldSuffixMap);
        data.put("extraFieldStrategy", extraFieldStrategy);

        return data;
    }
}
