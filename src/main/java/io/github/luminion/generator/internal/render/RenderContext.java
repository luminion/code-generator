package io.github.luminion.generator.internal.render;

import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Single-table render context.
 */
public class RenderContext {
    private final TableInfo tableInfo;
    private final Map<String, TemplateClassFile> templateFiles;

    public RenderContext(TableInfo tableInfo, Map<String, TemplateClassFile> templateFiles) {
        this.tableInfo = tableInfo;
        this.templateFiles = Collections.unmodifiableMap(new LinkedHashMap<>(templateFiles));
    }

    public static RenderContext simple(TableInfo tableInfo) {
        return new RenderContext(tableInfo, Collections.emptyMap());
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public Map<String, TemplateClassFile> getTemplateFiles() {
        return templateFiles;
    }

    public TemplateClassFile getTemplateFile(String key) {
        return templateFiles.get(key);
    }
}