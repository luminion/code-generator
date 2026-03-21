package io.github.luminion.generator.builder.special;

import io.github.luminion.generator.common.MethodReference;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.special.MybatisPlusConfig;
import io.github.luminion.generator.po.ClassMethodPayload;
import io.github.luminion.generator.util.ReflectUtils;
import io.github.luminion.sqlbooster.model.BPage;

/**
 * MyBatisPlus-SqlBooster代码生成器构建器
 * <p>
 * 继承自AbstractMyBatisPlusBuilder，提供MyBatisPlus和SqlBooster框架的通用配置
 * 用于配置相关的代码生成选项：
 * <ul>
 *   <li>分页返回包装方法</li>
 *   <li>sqlContext查询</li>
 * </ul>
 *
 * @author luminion
 * @since 1.0.0
 */
public class MybatisPlusBoosterBuilder extends AbstractMyBatisPlusBuilder<MybatisPlusBoosterBuilder> {

    public MybatisPlusBoosterBuilder(ConfigCollector<MybatisPlusConfig> configCollector) {
        super(configCollector);
    }

    /**
     * 指定controller返回的分页包装类及方法
     *
     * @param methodReference 分页返回的包装方法
     * @return this
     */
    public <T, R> MybatisPlusBoosterBuilder pageMethod(MethodReference<BPage<T>, R> methodReference) {
        ClassMethodPayload payload = ReflectUtils.lambdaMethodInvokeInfo(methodReference, BPage.class);
        this.configCollector.getControllerConfig().setPageMethod(payload);
        return this;
    }


    /**
     * 禁用controller的sqlContext入参查询（默认启用）
     * <p>
     * sql-booster的sqlContext用于在查询时传入额外的SQL条件
     *
     * @return this
     */
    public MybatisPlusBoosterBuilder sqlContextQueryDisable() {
        this.configCollector.getControllerConfig().setSqlContextQuery(false);
        return this;
    }
    


}
