package io.github.luminion.generator;

import io.github.luminion.generator.config.Configurer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LambdaGeneratorTest {

    @Test
    void executeTableFilterOverwritesPreviousIncludeTables() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        LambdaGenerator generator = new LambdaGenerator(configurer);
        configurer.getDataSourceConfig().getIncludeTables().add("legacy_table");

        assertEquals(new LinkedHashSet<>(Arrays.asList("sys_user", "sys_role")),
                configurer.getDataSourceConfig().getIncludeTables());

        assertEquals(new LinkedHashSet<>(Arrays.asList("audit_log")),
                configurer.getDataSourceConfig().getIncludeTables());
    }
}
