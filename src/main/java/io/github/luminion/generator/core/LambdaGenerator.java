package io.github.luminion.generator.core;

import io.github.luminion.generator.builder.core.GlobalBuilder;
import io.github.luminion.generator.builder.core.InjectionBuilder;
import io.github.luminion.generator.builder.core.StrategyBuilder;
import io.github.luminion.generator.builder.model.*;

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
     * 配置Service选项
     *
     * @param func Service配置构建器函数
     * @return this
     */
    LambdaGenerator<B> service(Function<ServiceBuilder, ServiceBuilder> func);

    /**
     * 配置ServiceImpl选项
     *
     * @param func ServiceImpl配置构建器函数
     * @return this
     */
    LambdaGenerator<B> serviceImpl(Function<ServiceImplBuilder, ServiceImplBuilder> func);

    /**
     * 配置Mapper选项
     *
     * @param func Mapper配置构建器函数
     * @return this
     */
    LambdaGenerator<B> mapper(Function<MapperBuilder, MapperBuilder> func);

    /**
     * 配置MapperXml选项
     *
     * @param func MapperXml配置构建器函数
     * @return this
     */
    LambdaGenerator<B> mapperXml(Function<MapperXmlBuilder, MapperXmlBuilder> func);

    /**
     * 配置Entity选项
     *
     * @param func Entity配置构建器函数
     * @return this
     */
    LambdaGenerator<B> entity(Function<EntityBuilder, EntityBuilder> func);

    /**
     * 配置QueryDTO选项
     *
     * @param func QueryDTO配置构建器函数
     * @return this
     */
    LambdaGenerator<B> queryDTO(Function<QueryDTOBuilder, QueryDTOBuilder> func);

    /**
     * 配置QueryVO选项
     *
     * @param func QueryVO配置构建器函数
     * @return this
     */
    LambdaGenerator<B> queryVO(Function<QueryVOBuilder, QueryVOBuilder> func);

    /**
     * 配置CreateDTO选项
     *
     * @param func CreateDTO配置构建器函数
     * @return this
     */
    LambdaGenerator<B> createDTO(Function<CreateDTOBuilder, CreateDTOBuilder> func);

    /**
     * 配置UpdateDTO选项
     *
     * @param func UpdateDTO配置构建器函数
     * @return this
     */
    LambdaGenerator<B> updateDTO(Function<UpdateDTOBuilder, UpdateDTOBuilder> func);

    /**
     * 配置ExportDTO选项
     *
     * @param func ExportDTO配置构建器函数
     * @return this
     */
    LambdaGenerator<B> exportDTO(Function<ExportDTOBuilder, ExportDTOBuilder> func);

    /**
     * 配置ImportDTO选项
     *
     * @param func ImportDTO配置构建器函数
     * @return this
     */
    LambdaGenerator<B> importDTO(Function<ImportDTOBuilder, ImportDTOBuilder> func);

    /**
     * 配置扩展选项
     *
     * @param func 扩展配置构建器函数
     * @return this
     */
    LambdaGenerator<B> extension(Function<B, B> func);

    /**
     * 执行生成
     *
     * @param tables 生成代码的表, 为空时生成全部 
     */
    void execute(String... tables);

}
