package io.github.luminion.generator;

import io.github.luminion.generator.config.Configurer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LambdaGeneratorTest {

    @Test
    void executeTableFilterOverwritesPreviousIncludeTables() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        LambdaGenerator generator = new LambdaGenerator(configurer);
        configurer.getDataSourceConfig().getIncludeTables().add("legacy_table");
        configurer.getDataSourceConfig().getExcludeTables().add("sys_log");

        generator.replaceIncludeTables("sys_user", "sys_role");

        assertEquals(new LinkedHashSet<>(Arrays.asList("sys_user", "sys_role")),
                configurer.getDataSourceConfig().getIncludeTables());
        assertTrue(configurer.getDataSourceConfig().getExcludeTables().isEmpty());

        generator.replaceIncludeTables("audit_log");

        assertEquals(new LinkedHashSet<>(Arrays.asList("audit_log")),
                configurer.getDataSourceConfig().getIncludeTables());
    }
}
