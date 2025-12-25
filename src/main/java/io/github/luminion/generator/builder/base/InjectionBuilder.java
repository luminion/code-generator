package io.github.luminion.generator.builder.base;

import io.github.luminion.generator.config.base.InjectionConfig;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class InjectionBuilder {
    private final InjectionConfig config;
    
    /**
     * 生成之前需要执行的操作, 可通过此方法添加一些自定义操作
     * <p> 
     * 其中 tableInfo是数据库表信息
     * Map<String, Object> 是最终模板使用的参数集合
     * 
     * @param consumer 消费者
     * @return this
     */
    public InjectionBuilder beforeGenerate(BiConsumer<TableInfo, Map<String, Object>> consumer) {
        this.config.setBeforeGenerate(consumer);
        return this;
    }
    
    /**
     * 添加自定义参数
     * <p>
     * 
     * @param customMap 自定义参数
     * @return this
     */
    public InjectionBuilder customMap(Map<String, Object> customMap){
        this.config.setCustomMap(customMap);
        return this;
    }
    
    /**
     * 添加自定义模板文件
     * 
     * @param templateFile 自定义模板文件
     * @return this
     */
    public InjectionBuilder addCustomFiles(TemplateFile... templateFile){
        this.config.getTemplateFiles().addAll(Arrays.asList(templateFile));
        return this;
    }
}
