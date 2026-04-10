package io.github.luminion.generator.engine;

import io.github.luminion.generator.config.Configurer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VelocityTemplateEngineTest {

    @Test
    void openDoesNotWarnWhenOpeningOutputDirectoryIsDisabled() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        File missingDir = new File(System.getProperty("java.io.tmpdir"),
                "code-generator-missing-" + System.nanoTime());
        configurer.getTemplateConfig().setOutputDir(missingDir.getPath());

        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        try (PrintStream capture = new PrintStream(errorStream, true, StandardCharsets.UTF_8.name())) {
            System.setErr(capture);
            new VelocityTemplateEngine(configurer).open();
        } finally {
            System.setErr(originalErr);
        }

        assertEquals("", errorStream.toString(StandardCharsets.UTF_8.name()));
    }
}
