package io.github.luminion.generator.core;

import io.github.luminion.generator.builder.base.GlobalBuilder;
import io.github.luminion.generator.builder.base.InjectionBuilder;
import io.github.luminion.generator.builder.base.StrategyBuilder;
import io.github.luminion.generator.builder.model.CommandBuilder;
import io.github.luminion.generator.builder.model.ControllerBuilder;
import io.github.luminion.generator.builder.model.EntityBuilder;
import io.github.luminion.generator.builder.model.ExcelBuilder;
import io.github.luminion.generator.builder.model.MapperBuilder;
import io.github.luminion.generator.builder.model.QueryBuilder;
import io.github.luminion.generator.builder.model.ServiceBuilder;

import java.util.function.Function;

/**
 * Lambda风格的代码生成器接口
 * 提供链式调用方式配置代码生成的各种选项
 *
 * <p>使用示例:
 * <pre>
 * LambdaGenerator&lt;?&gt; generator = new MybatisPlusGenerator()
 *     .global(g -> g.author("luminion").parentPackage("com.example"))
 *     .injection(i -> i.customMap(Map.of("key", "value")))
 *     .strategy(s -> s.tablePrefix("t_").include("user", "role"))
 *     .controller(c -> c.restController(true).baseUrl("/api"))
 *     .service(s -> s.superClass("BaseService"))
 *     .entity(e -> e.superClass("BaseEntity"))
 *     .execute("user", "role");
 * </pre>
 *
 * @param <B> 扩展配置构建器类型
 * @author luminion
 * @since 1.0.0
 */
public interface LambdaGenerator<B> {

    /**
     * 配置全局选项
     *
     * @param func 全局配置构建器函数
     * @return this
     */
    LambdaGenerator<B> global(Function<GlobalBuilder, GlobalBuilder> func);

    /**
     * 注入配置，用于配置代码生成过程中的自定义注入配置
     * 允许用户自定义模板文件、参数以及生成前的操作
     *
     * @param func 注入配置构建器函数
     * @return this
     */
    LambdaGenerator<B> injection(Function<InjectionBuilder, InjectionBuilder> func);

    /**
     * 配置策略选项
     *
     * @param func 策略配置构建器函数
     * @return this
     */
    LambdaGenerator<B> strategy(Function<StrategyBuilder, StrategyBuilder> func);

    /**
     * 配置Controller选项
     *
     * @param func Controller配置构建器函数
     * @return this
     */
    LambdaGenerator<B> controller(Function<ControllerBuilder, ControllerBuilder> func);

    /**
     * 配置Service选项（整合Service和ServiceImpl）
     *
     * <p>使用示例:
     * <pre>
     * .service(s -> s
     *     .service(svc -> svc.superClass("BaseService"))
     *     .serviceImpl(impl -> impl.superClass("BaseServiceImpl"))
     * )
     * </pre>
     *
     * @param func Service配置构建器函数
     * @return this
     */
    LambdaGenerator<B> service(Function<ServiceBuilder, ServiceBuilder> func);

    /**
     * 配置Mapper选项（整合Mapper和MapperXml）
     *
     * <p>使用示例:
     * <pre>
     * .mapper(m -> m
     *     .mapper(mapper -> mapper.superClass("BaseMapper"))
     *     .mapperXml(xml -> xml.baseResultMapEnable())
     * )
     * </pre>
     *
     * @param func Mapper配置构建器函数
     * @return this
     */
    LambdaGenerator<B> mapper(Function<MapperBuilder, MapperBuilder> func);

    /**
     * 配置Entity选项
     *
     * @param func Entity配置构建器函数
     * @return this
     */
    LambdaGenerator<B> entity(Function<EntityBuilder, EntityBuilder> func);

    /**
     * 配置查询功能（整合QueryDTO和QueryVO）
     *
     * <p>使用示例:
     * <pre>
     * .query(q -> q
     *     .queryParam(p -> p.extendsEntityEnable())
     *     .queryResult(r -> r.extendsEntityEnable())
     * )
     * </pre>
     *
     * @param func 查询功能配置构建器函数
     * @return this
     */
    LambdaGenerator<B> query(Function<QueryBuilder, QueryBuilder> func);

    /**
     * 配置Excel功能（整合ImportDTO和ExportDTO）
     *
     * <p>使用示例:
     * <pre>
     * .excel(ex -> ex
     *     .importExcel(i -> i.generateDisable())
     *     .exportExcel(e -> e.generateDisable())
     * )
     * </pre>
     *
     * @param func Excel功能配置构建器函数
     * @return this
     */
    LambdaGenerator<B> excel(Function<ExcelBuilder, ExcelBuilder> func);

    /**
     * 配置命令功能（整合CreateDTO和UpdateDTO）
     *
     * <p>使用示例:
     * <pre>
     * .command(c -> c
     *     .create(cr -> cr.generateDisable())
     *     .update(up -> up.generateDisable())
     * )
     * </pre>
     *
     * @param func 命令功能配置构建器函数
     * @return this
     */
    LambdaGenerator<B> command(Function<CommandBuilder, CommandBuilder> func);

    /**
     * 配置功能特性选项
     *
     * @param func 功能特性配置构建器函数
     * @return this
     */
    LambdaGenerator<B> feature(Function<B, B> func);

    /**
     * 执行生成
     *
     * @param tables 生成代码的表, 为空时生成全部 
     */
    void execute(String... tables);

}
