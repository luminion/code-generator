package io.github.luminion.generator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum ExcelApi {
    // 主入口
    EASY_EXCEL("com.alibaba.excel.","EasyExcel"),
    FAST_EXCEL("cn.idev.excel.","FastExcel"),
    ;
    @Getter
    private final String packagePrefix;
    @Getter
    private final String mainEntrance;



}
