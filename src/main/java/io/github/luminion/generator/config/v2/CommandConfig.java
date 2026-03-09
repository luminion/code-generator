package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.po.TableInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author luminion
 * @since 1.0.0
 */
public class CommandConfig implements TemplateRender {

    /**
     * 生成参数校验相关注解
     */
    protected boolean validated;
    /**
     * 生成新增方法及配套类
     */
    protected boolean generateCreate = true;

    /**
     * 生成更新方法及配套类
     */
    protected boolean generateUpdate = true;

    /**
     * 生成删除方法及配套类
     */
    protected boolean generateDelete = true;


    /**
     * 新增和修改需要需要排除的字段
     */
    protected final Set<String> editExcludeColumns = new HashSet<>();

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("validated", this.validated);

        data.put("generateCreate", this.generateCreate);
        data.put("generateUpdate", this.generateUpdate);
        data.put("generateDelete", this.generateDelete);
        data.put("editExcludeColumns", this.editExcludeColumns);


        return data;
    }
    
    public void disable() {
        this.generateCreate = false;
        this.generateUpdate = false;
        this.generateDelete = false;
    }
    
}
