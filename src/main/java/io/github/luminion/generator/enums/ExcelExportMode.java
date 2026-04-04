package io.github.luminion.generator.enums;

/**
 * Excel导出策略
 *
 * @author luminion
 * @since 1.0.0
 */
public enum ExcelExportMode {
    /**
     * 一次性查询并写出，适合中小数据量
     */
    SIMPLE,

    /**
     * 分页查询并分批写出，适合大数据量导出
     */
    PAGED
}
