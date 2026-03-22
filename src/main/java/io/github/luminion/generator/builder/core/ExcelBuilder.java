package io.github.luminion.generator.builder.core;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.ExcelApi;
import lombok.RequiredArgsConstructor;

/**
 * Excel配置构建器
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class ExcelBuilder {
    private final Configurer configurer;

    /**
     * 设置Excel框架类型
     *
     * @param excelApi Excel框架
     * @return this
     */
    public ExcelBuilder api(ExcelApi excelApi) {
        configurer.getExcelConfig().setExcelApi(excelApi);
        return this;
    }
    
    /**
     * 设置Excel导入方法名
     *
     * @param methodName 方法名
     * @return this
     */
    public ExcelBuilder excelImportMethodName(String methodName){
        configurer.getExcelConfig().setExcelImportMethodName(methodName);
        return this;
    }

    /**
     * 设置Excel导入模板方法名
     *
     * @param methodName 方法名
     * @return this
     */
    public ExcelBuilder excelImportTemplateMethodName(String methodName){
        configurer.getExcelConfig().setExcelImportTemplateMethodName(methodName);
        return this;
    }
    
    /**
     * 设置Excel导出方法名
     *
     * @param methodName 方法名
     * @return this
     */
    public ExcelBuilder excelExportMethodName(String methodName){
        configurer.getExcelConfig().setExcelExportMethodName(methodName);
        return this;
    }
}
