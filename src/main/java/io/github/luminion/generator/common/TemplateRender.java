package io.github.luminion.generator.common;

import io.github.luminion.generator.po.TableInfo;

import java.util.Map;

/**
 * @author luminion
 * @since 1.0.0
 */
public interface TemplateRender {

    /**
     * 决定该配置的执行顺序,越小的越先执行
     */
    default int order() {
        return 100;
    }

    /**
     * 验证/初始化配置项
     *
     * @since 1.0.0
     */
    default void init() {

    }
    
    /**
     * 渲染前处理, 允许在这一步读取修改配置及表信息
     *
     * @param tableInfo 表信息
     * @since 1.0.0
     */
    default void renderDataPreProcess(TableInfo tableInfo) {

    }

    /**
     * 渲染数据, 这一步应该只渲染信息, 不能再修改配置及表信息
     *
     * @param tableInfo 表信息
     * @return map
     * @since 1.0.0
     */
     Map<String, Object> renderData(TableInfo tableInfo);

    /**
     * 渲染后, 对渲染的map进行修改
     *
     * @param tableInfo  表信息
     * @param renderData 渲染数据
     * @since 1.0.0
     */
    default void renderDataPostProcess(TableInfo tableInfo, Map<String, Object> renderData){
        
    }
    
}
