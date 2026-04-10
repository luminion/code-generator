package io.github.luminion.generator.metadata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenerationSummary {
    private final Map<String, TableGeneration> tableGenerations = new LinkedHashMap<>();
    private int generatedFileCount;

    public void recordMatchedTable(TableInfo tableInfo) {
        tableGenerations.computeIfAbsent(tableInfo.getTableName(),
                key -> new TableGeneration(tableInfo.getTableName(), tableInfo.getEntityName()));
    }

    public void recordGeneratedFile(TableInfo tableInfo, TemplateFile templateFile) {
        TableGeneration tableGeneration = tableGenerations.computeIfAbsent(tableInfo.getTableName(),
                key -> new TableGeneration(tableInfo.getTableName(), tableInfo.getEntityName()));
        tableGeneration.generatedTemplateKeys.add(templateFile.getKey());
        generatedFileCount++;
    }

    public int getMatchedTableCount() {
        return tableGenerations.size();
    }

    public int getGeneratedFileCount() {
        return generatedFileCount;
    }

    public List<TableGeneration> getTableGenerations() {
        return new ArrayList<>(tableGenerations.values());
    }

    public static final class TableGeneration {
        private final String tableName;
        private final String entityName;
        private final List<String> generatedTemplateKeys = new ArrayList<>();

        private TableGeneration(String tableName, String entityName) {
            this.tableName = tableName;
            this.entityName = entityName;
        }

        public String getTableName() {
            return tableName;
        }

        public String getEntityName() {
            return entityName;
        }

        public List<String> getGeneratedTemplateKeys() {
            return generatedTemplateKeys;
        }
    }
}
