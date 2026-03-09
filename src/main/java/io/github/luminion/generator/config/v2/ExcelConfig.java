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
    protected final Configurer configurer;

    /**
     * excel api
     */
    protected ExcelApi excelApi = ExcelApi.EASY_EXCEL;

    /**
     * 生成导入方法及配套类(需允许新增)
     */
    protected boolean generateImport = true;

    /**
     * 生成导出方法(需允许查询)
     */
    protected boolean generateExport = true;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("excelApiPackagePrefix", excelApi.getPackagePrefix());
        data.put("excelApiClass", excelApi.getMainEntrance());

        data.put("generateImport", this.generateImport);
        data.put("generateExport", this.generateExport);
        
        // todo 检查是否允许查询
        
        return data;
    }
}
