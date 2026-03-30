package io.github.luminion.generator.config;

import io.github.luminion.generator.builder.ControllerBuilder;
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
        @SuppressWarnings("unchecked")
        Set<String> serviceFramePkg = (Set<String>) data.get("serviceFramePkg");

        assertEquals("com.example.common.PageResult", pageType.getFullyQualifiedClassName());
        assertEquals("IPage", data.get("servicePageType"));
        assertTrue(serviceFramePkg.contains("com.baomidou.mybatisplus.core.metadata.IPage"));
    }

    @Test
    void servicePageParameterNamesStayIndependentFromQueryDtoFieldNames() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        configurer.getQueryConfig().setPageParamName("current");
        configurer.getQueryConfig().setSizeParamName("size");

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("SysUser");

        Map<String, Object> data = configurer.renderMap(configurer.createRenderContext(tableInfo));

        assertEquals("pageNo", data.get("servicePageParamName"));
        assertEquals("pageSize", data.get("serviceSizeParamName"));
        assertEquals("current", data.get("pageParamName"));
        assertEquals("size", data.get("sizeParamName"));
    }

    @Test
    void stringWrapperShortcutsBuildExpectedInvokeInfo() {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        ControllerBuilder builder = new ControllerBuilder(configurer);

        builder.returnMethod("com.example.common.Result", "success");
        builder.pageMethod("com.example.common.PageResult", "success");

        assertEquals("Result<String>", configurer.getControllerConfig().getReturnType().toTypeString("String"));
        assertEquals("Result.success(data)", configurer.getControllerConfig().getReturnType().toInvokeString("data"));
        assertEquals("PageResult<String>", configurer.getControllerConfig().getPageType().toTypeString("String"));
        assertEquals("PageResult.success(data)", configurer.getControllerConfig().getPageType().toInvokeString("data"));
    }
}
