package io.github.luminion.generator.internal.template;

import io.github.luminion.generator.config.TemplateConfig;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
import io.github.luminion.generator.metadata.TemplateFile;
import io.github.luminion.generator.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateFileResolver {
    private final TemplateConfig templateConfig;

    public TemplateFileResolver(TemplateConfig templateConfig) {
        this.templateConfig = templateConfig;
    }

    public Map<String, TemplateClassFile> resolve(TableInfo tableInfo, List<TemplateFile> templateFiles) {
        return templateFiles.stream().collect(Collectors.toMap(
                TemplateFile::getKey,
                templateFile -> buildTemplateClassFile(templateFile, tableInfo)
        ));
    }

    private TemplateClassFile buildTemplateClassFile(TemplateFile templateFile, TableInfo tableInfo) {
        templateFile.validate();
        String joinedPackage = joinPackage(templateFile.getSubPackage());
        String resolvedOutputDir = templateFile.getFileOutputDir();
        if (resolvedOutputDir == null) {
            resolvedOutputDir = joinPath(templateConfig.getOutputDir(), joinedPackage);
        }
        TemplateClassFile templateClassFile = new TemplateClassFile(templateFile);
        templateClassFile.setFileOutputDir(resolvedOutputDir);
        templateClassFile.setPackageName(joinedPackage);
        String simpleClassName = templateFile.convertFormatName(tableInfo.getEntityName());
        templateClassFile.setSimpleClassName(simpleClassName);
        templateClassFile.setFullyQualifiedClassName(
                StringUtils.isBlank(joinedPackage) ? simpleClassName : joinedPackage + "." + simpleClassName
        );
        return templateClassFile;
    }

    private String joinPackage(String subPackage) {
        String parentPackage = resolveParentPackage();
        String normalizedSubPackage = normalizePackageSegment(subPackage);
        if (StringUtils.isBlank(parentPackage)) {
            return normalizedSubPackage;
        }
        if (StringUtils.isBlank(normalizedSubPackage)) {
            return parentPackage;
        }
        return parentPackage + "." + normalizedSubPackage;
    }

    private String resolveParentPackage() {
        String parentPackage = normalizePackageSegment(templateConfig.getParentPackage());
        String parentModule = normalizePackageSegment(templateConfig.getParentModule());
        if (StringUtils.isBlank(parentPackage)) {
            return parentModule;
        }
        if (StringUtils.isBlank(parentModule)) {
            return parentPackage;
        }
        return parentPackage + "." + parentModule;
    }

    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty("java.io.tmpdir");
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        if (StringUtils.isBlank(packageName)) {
            return parentDir.substring(0, parentDir.length() - File.separator.length());
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    private String normalizePackageSegment(String packageName) {
        if (StringUtils.isBlank(packageName)) {
            return "";
        }
        String normalized = packageName.trim();
        while (normalized.startsWith(".")) {
            normalized = normalized.substring(1);
        }
        while (normalized.endsWith(".")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
