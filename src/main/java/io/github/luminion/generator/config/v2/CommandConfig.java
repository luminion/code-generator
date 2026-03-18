package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class CommandConfig implements TemplateRender {
    private final Configurer configurer;

    /**
     * 生成参数校验相关注解
     */
    private boolean valid;
    /**
     * 生成新增方法及配套类
     */
    private boolean enableCreateByDto = true;

    /**
     * 生成更新方法及配套类
     */
    private boolean enableUpdateByDto = true;

    /**
     * 生成删除方法及配套类
     */
    private boolean enableDeleteById = true;


    /**
     * 新增和修改需要需要排除的字段
     */
    private final Set<String> editExcludeColumns = new HashSet<>();

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("validated", this.valid);

        data.put("generateCreate", this.enableCreateByDto);
        data.put("generateUpdate", this.enableUpdateByDto);
        data.put("generateDelete", this.enableDeleteById);
        data.put("editExcludeColumns", this.editExcludeColumns);

        return data;
    }
    
    public void disable() {
        this.enableCreateByDto = false;
        this.enableUpdateByDto = false;
        this.enableDeleteById = false;
    }
    
}
