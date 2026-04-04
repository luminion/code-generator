package io.github.luminion.generator.builder;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.ExcelApi;
import io.github.luminion.generator.enums.ExcelExportMode;
import io.github.luminion.generator.enums.ExcelImportMode;
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
     * 设置Excel导入策略
     *
     * @param importMode 导入策略
     * @return this
     */
    public ExcelBuilder importMode(ExcelImportMode importMode) {
        configurer.getExcelConfig().setExcelImportMode(importMode);
        return this;
    }

    /**
     * 设置Excel导入分批大小
     *
     * @param batchSize 批量大小
     * @return this
     */
    public ExcelBuilder excelImportBatchSize(int batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("excel import batch size must be greater than 0");
        }
        configurer.getExcelConfig().setExcelImportBatchSize(batchSize);
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
     * 设置Excel导出策略
     *
     * @param exportMode 导出策略
     * @return this
     */
    public ExcelBuilder exportMode(ExcelExportMode exportMode) {
        configurer.getExcelConfig().setExcelExportMode(exportMode);
        return this;
    }

    /**
     * 设置Excel导出分页大小
     *
     * @param pageSize 分页大小
     * @return this
     */
    public ExcelBuilder excelExportPageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("excel export page size must be greater than 0");
        }
        configurer.getExcelConfig().setExcelExportPageSize(pageSize);
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
