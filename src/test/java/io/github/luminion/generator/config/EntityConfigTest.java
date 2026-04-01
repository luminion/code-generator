package io.github.luminion.generator.config;

import io.github.luminion.generator.metadata.TableInfo;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityConfigTest {

    @Test
    void defaultUnderlineTableNameDoesNotRequireTableNameAnnotation() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("SysUser");

        Map<String, Object> data = configurer.renderMap(configurer.createRenderContext(tableInfo));

        assertEquals(false, data.get("tableNameAnnotation"));
        @SuppressWarnings("unchecked")
        Set<String> entityFramePkg = (Set<String>) data.get("entityFramePkg");
        assertFalse(entityFramePkg.contains("com.baomidou.mybatisplus.annotation.TableName"));
    }

    @Test
    void strippedPrefixStillRequiresExplicitTableNameAnnotation() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("User");

        Map<String, Object> data = configurer.renderMap(configurer.createRenderContext(tableInfo));

        assertEquals(true, data.get("tableNameAnnotation"));
        @SuppressWarnings("unchecked")
        Set<String> entityFramePkg = (Set<String>) data.get("entityFramePkg");
        assertTrue(entityFramePkg.contains("com.baomidou.mybatisplus.annotation.TableName"));
    }
}
