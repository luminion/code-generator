package io.github.luminion.generator.internal.render;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.enums.TemplateEnum;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TableSuffixField;
import io.github.luminion.generator.metadata.TemplateClassFile;
import io.github.luminion.generator.metadata.TemplateFile;
import io.github.luminion.generator.naming.ExtraFieldStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TemplateGenerationPlanner {
    private final Configurer configurer;

    public RenderContext plan(TableInfo tableInfo) {
        populateExtraQueryFields(tableInfo);
        Map<String, TemplateClassFile> templateFileMap = configurer.getTemplateConfig().resolveTemplateFileMap(tableInfo);
        applyTemplateGenerationPolicy(tableInfo, templateFileMap);
        logTableGenerationHints(tableInfo);
        return new RenderContext(tableInfo, templateFileMap);
    }

    private void populateExtraQueryFields(TableInfo tableInfo) {
        Set<String> existingPropertyNames = tableInfo.getFields().stream()
                .map(TableField::getPropertyName)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<TableSuffixField> extraFields = tableInfo.getExtraFields();
        extraFields.clear();

        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            appendExtraQueryFields(field, existingPropertyNames, extraFields);
        }
    }

    private void appendExtraQueryFields(TableField field, Set<String> existingPropertyNames, List<TableSuffixField> extraFields) {
        ExtraFieldStrategy extraFieldStrategy = configurer.getQueryConfig().getExtraFieldStrategy();
        for (Map.Entry<String, String> entry : configurer.getQueryConfig().getExtraFieldSuffixMap().entrySet()) {
            String suffix = entry.getKey();
            String sqlOperator = entry.getValue();
            if (!extraFieldStrategy.generateExtraField(sqlOperator, field)) {
                continue;
            }
            String suffixPropertyName = field.getPropertyName() + suffix;
            if (!existingPropertyNames.add(suffixPropertyName)) {
                continue;
            }
            TableSuffixField extraField = new TableSuffixField();
            extraField.setSqlOperator(sqlOperator);
            extraField.setPropertyType(field.getJavaTypeSimpleName());
            extraField.setPropertyName(suffixPropertyName);
            extraField.setCapitalName(field.getCapitalName() + suffix);
            extraField.setColumnName(field.getColumnName());
            extraField.setComment(field.getComment());
            extraField.setSuffix(suffix);
            extraFields.add(extraField.normalize());
        }
    }

    private void applyTemplateGenerationPolicy(TableInfo tableInfo, Map<String, TemplateClassFile> templateFileMap) {
        boolean hasPrimaryKey = tableInfo.getPrimaryKeyField() != null;
        boolean generateQueryById = configurer.getGlobalConfig().isGenerateQueryById() && hasPrimaryKey;
        boolean generateQueryList = configurer.getGlobalConfig().isGenerateQueryList();
        boolean generateQueryPage = configurer.getGlobalConfig().isGenerateQueryPage();
        boolean generateExcelExport = configurer.getGlobalConfig().isGenerateExcelExport();

        setTemplateGeneration(templateFileMap.get(TemplateEnum.CREATE_PARAM.getKey()),
                configurer.getGlobalConfig().isGenerateCreate(),
                "Skipped because create generation is disabled");

        setTemplateGeneration(templateFileMap.get(TemplateEnum.UPDATE_PARAM.getKey()),
                configurer.getGlobalConfig().isGenerateUpdate() && hasPrimaryKey,
                hasPrimaryKey ? "Skipped because update generation is disabled" : "Skipped because the table has no primary key");

        boolean requiresQueryParam = generateQueryById || generateQueryList || generateQueryPage || generateExcelExport;
        setTemplateGeneration(templateFileMap.get(TemplateEnum.QUERY_PARAM.getKey()),
                requiresQueryParam,
                "Skipped because no query/list/page/export feature requires it");

        boolean requiresQueryResult = requiresQueryParam || RuntimeEnv.MP_BOOSTER.equals(configurer.getGlobalConfig().getRuntimeEnv());
        setTemplateGeneration(templateFileMap.get(TemplateEnum.QUERY_RESULT.getKey()),
                requiresQueryResult,
                "Skipped because no query/list/page/export feature requires it");

        setTemplateGeneration(templateFileMap.get(TemplateEnum.EXCEL_IMPORT_PARAM.getKey()),
                configurer.getGlobalConfig().isGenerateExcelImport(),
                "Skipped because excel import generation is disabled");

        setTemplateGeneration(templateFileMap.get(TemplateEnum.EXCEL_EXPORT_PARAM.getKey()),
                configurer.getGlobalConfig().isGenerateExcelExport(),
                "Skipped because excel export generation is disabled");
    }

    private void setTemplateGeneration(TemplateFile templateFile, boolean shouldGenerate, String skipReason) {
        if (templateFile == null) {
            return;
        }
        if (!templateFile.isGenerate()) {
            if (templateFile.getSkipReason() == null) {
                templateFile.setSkipReason("Skipped because template generation is disabled");
            }
            return;
        }
        templateFile.setGenerate(shouldGenerate);
        templateFile.setSkipReason(shouldGenerate ? null : skipReason);
    }

    private void logTableGenerationHints(TableInfo tableInfo) {
        if (tableInfo.getPrimaryKeyField() != null) {
            return;
        }
        String tableName = tableInfo.getTableName();
        if (configurer.getGlobalConfig().isGenerateCreate()) {
            log.info("Table [{}] has no primary key. Create method will still be generated, but it will return Boolean instead of a primary key value.", tableName);
        }
        if (configurer.getGlobalConfig().isGenerateUpdate()) {
            log.info("Table [{}] has no primary key. Update-related code will be skipped.", tableName);
        }
        if (configurer.getGlobalConfig().isGenerateDelete()) {
            log.info("Table [{}] has no primary key. Delete-by-id code will be skipped.", tableName);
        }
        if (configurer.getGlobalConfig().isGenerateQueryById()) {
            log.info("Table [{}] has no primary key. Query-by-id code will be skipped.", tableName);
        }
    }
}
