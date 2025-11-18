package io.github.luminion.generator.config.builder.core;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.po.TemplateFile;
import lombok.NonNull;
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
    private final Configurer configurer;

    /**
     * 注入自定义参数
     *
     * @param beforeOutputFileBiConsumer 回调消费
     * @return this
     */
    public InjectionBuilder beforeOutputFile(@NonNull BiConsumer<TableInfo, Map<String, Object>> beforeOutputFileBiConsumer) {
        this.configurer.getInjectionConfig().setBeforeOutputFileBiConsumer(beforeOutputFileBiConsumer);
        return this;
    }

    /**
     * 添加自定义模板文件
     *
     * @param customFile 自定义文件
     * @return this
     */
    public InjectionBuilder addCustomFile(@NonNull TemplateFile customFile) {
        this.configurer.getInjectionConfig().getCustomFiles().add(customFile);
        return this;
    }

    /**
     * 添加自定义模板文件
     *
     * @param customFiles 自定义文件列表
     * @return this
     */
    public InjectionBuilder addCustomFiles(@NonNull List<TemplateFile> customFiles) {
        this.configurer.getInjectionConfig().getCustomFiles().addAll(customFiles);
        return this;
    }
}
