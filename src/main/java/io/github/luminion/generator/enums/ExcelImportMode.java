package io.github.luminion.generator.enums;

/**
 * Excel导入策略
 *
 * @author luminion
 * @since 1.0.0
 */
public enum ExcelImportMode {
    /**
     * 直接同步读取为List，适合中小数据量
     */
    SIMPLE,

    /**
     * 使用listener分批消费，适合大数据量导入
     */
    BATCH
}
