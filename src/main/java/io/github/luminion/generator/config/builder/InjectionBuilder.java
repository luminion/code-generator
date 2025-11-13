package io.github.luminion.generator.config.builder;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.po.CustomFile;
import io.github.luminion.generator.po.TableInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class InjectionBuilder {
    private Configurer configurer;

    /**
     * 输出文件之前消费者
     *
     * @param biConsumer 消费者
     * @return this
     */
    public InjectionBuilder beforeOutputFile(BiConsumer<TableInfo, Map<String, Object>> biConsumer) {
        this.configurer.getInjectionConfig().setBeforeOutputFileBiConsumer(biConsumer);
        return this;
    }

    /**
     * 自定义配置 Map 对象
     *
     * @param customMap Map 对象
     * @return this
     */
    public InjectionBuilder customMap(Map<String, Object> customMap) {
        this.configurer.getInjectionConfig().setCustomMap(customMap);
        return this;
    }

    /**
     * 添加自定义文件
     *
     * @param customFile 自定义文件
     * @return this
     */
    public InjectionBuilder customFile(CustomFile customFile) {
        this.configurer.getInjectionConfig().getCustomFiles().add(customFile);
        return this;
    }

    /**
     * 添加自定义文件列表
     *
     * @param customFiles 自定义文件列表
     * @return this
     */
    public InjectionBuilder customFile(List<CustomFile> customFiles) {
        this.configurer.getInjectionConfig().getCustomFiles().addAll(customFiles);
        return this;
    }
}
