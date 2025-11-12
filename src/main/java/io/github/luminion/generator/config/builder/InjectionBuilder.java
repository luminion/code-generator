package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.po.CustomFile;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.config.base.InjectionConfig;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author luminion
 * @since 1.0.0
 */
public class InjectionBuilder {

    private final InjectionConfig injectionConfig;

    public InjectionBuilder(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
    }

    /**
     * 输出文件之前消费者
     *
     * @param biConsumer 消费者
     * @return this
     */
    public InjectionBuilder beforeOutputFile(BiConsumer<TableInfo, Map<String, Object>> biConsumer) {
        this.injectionConfig.setBeforeOutputFileBiConsumer(biConsumer);
        return this;
    }

    /**
     * 自定义配置 Map 对象
     *
     * @param customMap Map 对象
     * @return this
     */
    public InjectionBuilder customMap(Map<String, Object> customMap) {
        this.injectionConfig.setCustomMap(customMap);
        return this;
    }

    /**
     * 添加自定义文件
     *
     * @param customFile 自定义文件
     * @return this
     */
    public InjectionBuilder customFile(CustomFile customFile) {
        this.injectionConfig.getCustomFiles().add(customFile);
        return this;
    }

    /**
     * 添加自定义文件列表
     *
     * @param customFiles 自定义文件列表
     * @return this
     */
    public InjectionBuilder customFile(List<CustomFile> customFiles) {
        this.injectionConfig.getCustomFiles().addAll(customFiles);
        return this;
    }
}
