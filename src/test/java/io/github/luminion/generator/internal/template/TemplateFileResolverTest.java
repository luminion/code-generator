package io.github.luminion.generator.internal.template;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.TemplateEnum;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.metadata.TemplateClassFile;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplateFileResolverTest {

    @Test
    void blankSubPackageFallsBackToParentPackageWithoutTrailingDot() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        configurer.getTemplateConfig().setOutputDir("/tmp/codegen");
        configurer.getTemplateConfig().setParentPackage("com.example");
        configurer.getTemplateConfig().setParentModule("admin");
        configurer.getTemplateConfig().getEntity().setSubPackage("");

        TableInfo tableInfo = new TableInfo();
        tableInfo.setEntityName("SysUser");

        TemplateClassFile entityFile = configurer.resolveTemplateFiles(tableInfo).get(TemplateEnum.ENTITY.getKey());

        assertEquals("com.example.admin", entityFile.getPackageName());
        assertEquals("com.example.admin.SysUser", entityFile.getFullyQualifiedClassName());
        assertEquals("/tmp/codegen" + File.separator + "com" + File.separator + "example" + File.separator + "admin",
                entityFile.getFileOutputDir());
    }
}
