package io.github.luminion.generator.internal.render;

import io.github.luminion.generator.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Shared helpers for template import package assembly.
 *
 * @author luminion
 * @since 1.0.0
 */
public final class ImportPackageSupport {

    private ImportPackageSupport() {
    }

    public static void addIfPresent(Set<String> importPackages, String packageName) {
        if (StringUtils.isNotBlank(packageName)) {
            importPackages.add(packageName);
        }
    }

    public static Map<String, Object> splitImportPackages(Set<String> importPackages, String frameworkKey, String javaKey) {
        Map<String, Object> data = new HashMap<>(2);
        data.put(frameworkKey, importPackages.stream()
                .filter(StringUtils::isNotBlank)
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new)));
        data.put(javaKey, importPackages.stream()
                .filter(StringUtils::isNotBlank)
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new)));
        return data;
    }
}