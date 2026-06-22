package io.github.luminion.generator.internal.schema;

import io.github.luminion.generator.config.DataSourceConfig;
import io.github.luminion.generator.datasource.DatabaseKeywordsHandler;
import io.github.luminion.generator.datasource.FieldTypeConverter;
import io.github.luminion.generator.datasource.FieldTypeProvider;
import io.github.luminion.generator.datasource.support.JdbcDatabaseMetaDataWrapper;
import io.github.luminion.generator.enums.ColumnFillStrategy;
import io.github.luminion.generator.enums.NameConvertType;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.naming.DefaultNamingConverter;
import io.github.luminion.generator.util.FieldTypeConvertUtils;
import io.github.luminion.generator.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * Maps JDBC column metadata into generator table fields.
 */
public class JdbcTableFieldMapper {
    private final DataSourceConfig dataSourceConfig;

    public JdbcTableFieldMapper(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public void mapTableFields(TableInfo tableInfo, Map<String, JdbcDatabaseMetaDataWrapper.Column> columnMetadataMap) {
        Map<String, ColumnFillStrategy> columnFillMap = dataSourceConfig.getColumnFillMap();
        columnMetadataMap.forEach((ignored, columnInfo) -> {
            TableField tableField = createTableField(tableInfo, columnInfo);
            String rawColumnName = tableField.getRawColumnName();
            if (dataSourceConfig.matchIgnoreColumns(rawColumnName)) {
                return;
            }
            applyColumnStrategy(tableField, rawColumnName, columnFillMap);
            addTableField(tableInfo, tableField, rawColumnName);
        });
    }

    private TableField createTableField(TableInfo tableInfo, JdbcDatabaseMetaDataWrapper.Column columnInfo) {
        TableField tableField = new TableField();
        if (columnInfo.isPrimaryKey()) {
            registerPrimaryKey(tableInfo, tableField, columnInfo);
        }
        populateColumnNames(tableField, columnInfo);
        TableField.MetaInfo metaInfo = buildMetaInfo(tableInfo, columnInfo);
        tableField.setMetaInfo(metaInfo);
        applyJavaType(tableField, metaInfo);
        applyComment(tableField, columnInfo);
        applyPropertyName(tableField, columnInfo.getName());
        return tableField;
    }

    private void registerPrimaryKey(TableInfo tableInfo, TableField tableField, JdbcDatabaseMetaDataWrapper.Column columnInfo) {
        tableField.setPrimaryKey(true);
        tableField.setAutoIncrementPrimaryKey(columnInfo.isAutoIncrement());
        tableInfo.setHasPrimaryKey(true);
        tableInfo.setPrimaryKeyField(tableField);
    }

    private void populateColumnNames(TableField tableField, JdbcDatabaseMetaDataWrapper.Column columnInfo) {
        String rawColumnName = columnInfo.getName();
        tableField.setRawColumnName(rawColumnName);
        tableField.setColumnName(rawColumnName);
        DatabaseKeywordsHandler keyWordsHandler = dataSourceConfig.getKeyWordsHandler();
        if (keyWordsHandler != null && keyWordsHandler.isKeyWords(rawColumnName)) {
            tableField.setKeywordColumn(true);
            tableField.setColumnName(keyWordsHandler.formatColumn(rawColumnName));
        }
    }

    private TableField.MetaInfo buildMetaInfo(TableInfo tableInfo, JdbcDatabaseMetaDataWrapper.Column columnInfo) {
        TableField.MetaInfo metaInfo = new TableField.MetaInfo();
        metaInfo.setTableName(tableInfo.getTableName());
        metaInfo.setColumnName(columnInfo.getName());
        metaInfo.setLength(columnInfo.getLength());
        metaInfo.setNullable(columnInfo.isNullable());
        metaInfo.setRemarks(columnInfo.getRemarks());
        metaInfo.setDefaultValue(columnInfo.getDefaultValue());
        metaInfo.setScale(columnInfo.getScale());
        metaInfo.setJdbcType(columnInfo.getJdbcType());
        metaInfo.setTypeName(columnInfo.getTypeName());
        return metaInfo;
    }

    private void applyJavaType(TableField tableField, TableField.MetaInfo metaInfo) {
        FieldTypeProvider fieldTypeProvider = null;
        FieldTypeConverter fieldTypeConverter = dataSourceConfig.getFieldTypeConverter();
        if (fieldTypeConverter != null) {
            fieldTypeProvider = fieldTypeConverter.convert(metaInfo);
        }
        if (fieldTypeProvider == null) {
            fieldTypeProvider = FieldTypeConvertUtils.getJavaFieldType(metaInfo, dataSourceConfig.getDateType());
        }
        tableField.setJavaTypeSimpleName(fieldTypeProvider.getClassSimpleName());
        tableField.setJavaTypeCanonicalName(fieldTypeProvider.getClassCanonicalName());
    }

    private void applyComment(TableField tableField, JdbcDatabaseMetaDataWrapper.Column columnInfo) {
        if (columnInfo.getRemarks() != null) {
            tableField.setComment(columnInfo.getRemarks().replace("\"", "'"));
        }
    }

    private void applyPropertyName(TableField tableField, String rawColumnName) {
        Set<String> columnPrefixes = dataSourceConfig.getColumnPrefixes();
        Set<String> columnSuffixes = dataSourceConfig.getColumnSuffixes();
        String strippedColumnName = NameConvertType.removePrefixAndSuffix(rawColumnName, columnPrefixes, columnSuffixes);
        String propertyName = dataSourceConfig.getNamingConverter().convertFieldName(strippedColumnName);
        tableField.setPropertyName(propertyName);

        boolean removeIsPrefix = dataSourceConfig.isBooleanColumnRemoveIsPrefix();
        boolean isBoolean = "boolean".equalsIgnoreCase(tableField.getJavaTypeSimpleName());
        if (removeIsPrefix && isBoolean && propertyName.startsWith("is")) {
            propertyName = StringUtils.removePrefixAfterPrefixToLower(propertyName, 2);
            tableField.setPropertyName(propertyName);
            tableField.setRequiresColumnAnnotation(true);
        }
        tableField.setRequiresColumnAnnotation(requiresColumnAnnotation(tableField, propertyName));
        if (tableField.isPrimaryKey()) {
            tableField.setRequiresColumnAnnotation(!"id".equals(propertyName));
        }
    }

    private boolean requiresColumnAnnotation(TableField tableField, String propertyName) {
        if (DefaultNamingConverter.class.equals(dataSourceConfig.getNamingConverter().getClass())) {
            return !propertyName.equalsIgnoreCase(NameConvertType.underlineToCamel(tableField.getColumnName()));
        }
        return !propertyName.equalsIgnoreCase(tableField.getColumnName());
    }

    private void applyColumnStrategy(TableField tableField, String rawColumnName, Map<String, ColumnFillStrategy> columnFillMap) {
        ColumnFillStrategy columnFillStrategy = columnFillMap.get(rawColumnName);
        if (columnFillStrategy != null) {
            tableField.setFill(columnFillStrategy.name());
        }
        String logicDeleteColumnName = dataSourceConfig.getLogicDeleteColumnName();
        if (rawColumnName.equals(logicDeleteColumnName)) {
            tableField.setLogicDeleteField(true);
        }
        String versionColumnName = dataSourceConfig.getVersionColumnName();
        if (rawColumnName.equals(versionColumnName)) {
            tableField.setVersionField(true);
        }
    }

    private void addTableField(TableInfo tableInfo, TableField tableField, String rawColumnName) {
        if (dataSourceConfig.matchCommonColumns(rawColumnName)) {
            tableInfo.getCommonFields().add(tableField);
        } else {
            tableInfo.getFields().add(tableField);
        }
    }
}