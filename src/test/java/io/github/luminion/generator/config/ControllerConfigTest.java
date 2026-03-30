package io.github.luminion.generator.config;

import io.github.luminion.generator.metadata.InvokeInfo;
import io.github.luminion.generator.metadata.TableInfo;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerConfigTest {

    @Test
    void requestBaseUrlTrimsDuplicateBoundarySlashes() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        configurer.getTemplateConfig().setParentModule("/system/");
        configurer.getControllerConfig().setBaseUrl("/api/");

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("SysUser");

        Map<String, Object> data = configurer.getControllerConfig().renderData(configurer.createRenderContext(tableInfo));

        assertEquals("/api/system/sys-user", data.get("requestBaseUrl"));
    }

    @Test
    void customPageWrapperKeepsInternalPageTypeStable() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        configurer.getControllerConfig().setPageType(new InvokeInfo(
                "com.example.common.PageResult",
                "PageResult<%s>",
                "PageResult::success"
        ));

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("SysUser");

        Map<String, Object> data = configurer.renderMap(configurer.createRenderContext(tableInfo));

        InvokeInfo pageType = (InvokeInfo) data.get("pageType");
        InvokeInfo pageDataType = (InvokeInfo) data.get("pageDataType");
        @SuppressWarnings("unchecked")
        Set<String> serviceFramePkg = (Set<String>) data.get("serviceFramePkg");

        assertEquals("com.example.common.PageResult", pageType.getFullyQualifiedClassName());
        assertEquals("com.baomidou.mybatisplus.core.metadata.IPage", pageDataType.getFullyQualifiedClassName());
        assertTrue(serviceFramePkg.contains("com.baomidou.mybatisplus.core.metadata.IPage"));
    }
}
