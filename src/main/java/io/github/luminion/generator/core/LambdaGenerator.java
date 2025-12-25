package io.github.luminion.generator.core;

import io.github.luminion.generator.builder.base.GlobalBuilder;
import io.github.luminion.generator.builder.base.InjectionBuilder;
import io.github.luminion.generator.builder.base.StrategyBuilder;
import io.github.luminion.generator.builder.model.*;

import java.util.function.Function;

/**
 * Lambda风格的代码生成器接口
 * 提供链式调用方式配置代码生成的各种选项
 * 
 * @param <B> 特殊配置构建器类型
 * @author luminion
 * @since 1.0.0
 */
public interface LambdaGenerator<B> {

    LambdaGenerator<B> global(Function<GlobalBuilder, GlobalBuilder> func);

    /**
     * 注入配置，用于配置代码生成过程中的自定义注入配置
     * 允许用户自定义模板文件、参数以及生成前的操作
     *
     * @param func 注入配置构建器函数
     * @return this
     */
    LambdaGenerator<B> injection(Function<InjectionBuilder, InjectionBuilder> func);
    
    LambdaGenerator<B> strategy(Function<StrategyBuilder, StrategyBuilder> func);

    LambdaGenerator<B> controller(Function<ControllerBuilder, ControllerBuilder> func);

    LambdaGenerator<B> service(Function<ServiceBuilder, ServiceBuilder> func);

    LambdaGenerator<B> serviceImpl(Function<ServiceImplBuilder, ServiceImplBuilder> func);

    LambdaGenerator<B> mapper(Function<MapperBuilder, MapperBuilder> func);

    LambdaGenerator<B> mapperXml(Function<MapperXmlBuilder, MapperXmlBuilder> func);

    LambdaGenerator<B> entity(Function<EntityBuilder, EntityBuilder> func);

    LambdaGenerator<B> queryDTO(Function<QueryDTOBuilder, QueryDTOBuilder> func);

    LambdaGenerator<B> queryVO(Function<QueryVOBuilder, QueryVOBuilder> func);

    LambdaGenerator<B> createDTO(Function<CreateDTOBuilder, CreateDTOBuilder> func);

    LambdaGenerator<B> updateDTO(Function<UpdateDTOBuilder, UpdateDTOBuilder> func);

    LambdaGenerator<B> exportDTO(Function<ExportDTOBuilder, ExportDTOBuilder> func);

    LambdaGenerator<B> importDTO(Function<ImportDTOBuilder, ImportDTOBuilder> func);

    LambdaGenerator<B> special(Function<B, B> func);

    void execute(String... tables);

}
