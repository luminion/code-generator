package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.ExcelApi;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class ExcelConfig implements TemplateRender {
    private final Configurer configurer;

    /**
     * excel api
     */
    private ExcelApi excelApi = ExcelApi.EASY_EXCEL;

    /**
     * 生成导入方法及配套类(需允许新增)
     */
    private boolean enableExcelImport = true;

    /**
     * 生成导出方法(需允许查询)
     */
    private boolean enableExcelExport = true;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("excelApiPackagePrefix", excelApi.getPackagePrefix());
        data.put("excelApiClass", excelApi.getMainEntrance());

        data.put("generateImport", this.enableExcelImport);
        data.put("generateExport", this.enableExcelExport);
        
        // todo 检查是否允许查询
        
        return data;
    }
}
