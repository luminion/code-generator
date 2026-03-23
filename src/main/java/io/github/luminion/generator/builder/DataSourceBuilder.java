package io.github.luminion.generator.builder;

import io.github.luminion.generator.datasource.DatabaseKeywordsHandler;
import io.github.luminion.generator.datasource.FieldTypeConverter;
import io.github.luminion.generator.naming.NamingConverter;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.DateType;
import lombok.RequiredArgsConstructor;

import java.util.*;

/**
 * Controller配置构建器
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class DataSourceBuilder {
    private final Configurer configurer;


    /**
     * 数据库schema（模式）
     *
     * @param schema 模式
     */
    public DataSourceBuilder schema(String schema){
        configurer.getDataSourceConfig().setSchema(schema);
        return this;
    }
    
    /**
     * 日期类型
     *
     * @param dateType 日期类型
     */
    public DataSourceBuilder dateType(DateType dateType){
        configurer.getDataSourceConfig().setDateType(dateType);
        return this;
    }

    /**
     * 数据库表明/字段名转化到实体类名/属性名的转化器
     *
     * @param namingConverter 命名转换器
     */
    public DataSourceBuilder namingConverter(NamingConverter namingConverter){
        configurer.getDataSourceConfig().setNamingConverter(namingConverter);
        return this;
    }

    /**
     * 数据库字段类型转化为java字段类型的方式
     */
    public DataSourceBuilder fieldTypeConverter(FieldTypeConverter fieldTypeConverter){
        configurer.getDataSourceConfig().setFieldTypeConverter(fieldTypeConverter);
        return this;
    }

    /**
     * 数据库关键字处理器
     */
    public DataSourceBuilder keywordsHandler(DatabaseKeywordsHandler keywordsHandler){
        configurer.getDataSourceConfig().setKeyWordsHandler(keywordsHandler);
        return this;
    }

    /**
     * 启用跳过视图
     */
    public DataSourceBuilder enableSKipView(){
        configurer.getDataSourceConfig().setSkipView(true);
        return this;
    }
    
    /**
     * 启用布尔字段去除is前缀
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    public DataSourceBuilder enableBooleanColumnRemoveIsPrefix(){
        configurer.getDataSourceConfig().setBooleanColumnRemoveIsPrefix(true);
        return this;
    }
    
    /**
     * 模糊查询包含的表名, 需要自行拼接(%)
     */
    public DataSourceBuilder tableNamePattern(String tableNamePattern){
        configurer.getDataSourceConfig().setTableNamePattern(tableNamePattern);
        return this;
    }

    /**
     * 需要包含的表名
     */
    public DataSourceBuilder includeTables(String... includeTables){
        configurer.getDataSourceConfig().setIncludeTables(new LinkedHashSet<>(Arrays.asList(includeTables)));
        return this;
    }

    /**
     * 需要包含的表名
     */
    public DataSourceBuilder includeTables(Collection<String> includeTables){
        configurer.getDataSourceConfig().setIncludeTables(new LinkedHashSet<>(includeTables));
        return this;
    }

    /**
     * 需要排除的表名
     */
    public DataSourceBuilder excludeTables(String... excludeTables){
        configurer.getDataSourceConfig().setExcludeTables(new LinkedHashSet<>(Arrays.asList(excludeTables)));
        return this;
    }

    /**
     * 需要排除的表名
     */
    public DataSourceBuilder excludeTables(Collection<String> excludeTables){
        configurer.getDataSourceConfig().setExcludeTables(new LinkedHashSet<>(excludeTables));
        return this;
    }
    
    
    /**
     * 过滤表前缀
     */
    public DataSourceBuilder tablePrefixes(String... tablePrefixes){
        configurer.getDataSourceConfig().setTablePrefixes(new LinkedHashSet<>(Arrays.asList(tablePrefixes)));
        return this;
    }
    
    /**
     * 过滤表前缀
     */
    public DataSourceBuilder tablePrefixes(Collection<String> tablePrefixes){
        configurer.getDataSourceConfig().setTablePrefixes(new LinkedHashSet<>(tablePrefixes));
        return this;
    }

    /**
     * 过滤表前缀
     */
    public DataSourceBuilder tableSuffixes(String... tableSuffixes){
        configurer.getDataSourceConfig().setTableSuffixes(new LinkedHashSet<>(Arrays.asList(tableSuffixes)));
        return this;
    }

    /**
     * 过滤表前缀
     */
    public DataSourceBuilder tableSuffixes(Collection<String> tableSuffixes){
        configurer.getDataSourceConfig().setTableSuffixes(new LinkedHashSet<>(tableSuffixes));
        return this;
    }

    /**
     * 自定义实体父类公共字段
     */
    public DataSourceBuilder commonColumns(String... commonColumns){
        configurer.getDataSourceConfig().setCommonColumns(new LinkedHashSet<>(Arrays.asList(commonColumns)));
        return this;
    }

    /**
     * 自定义实体父类公共字段
     */
    public DataSourceBuilder commonColumns(Collection<String> commonColumns){
        configurer.getDataSourceConfig().setCommonColumns(new LinkedHashSet<>(commonColumns));
        return this;
    }

    /**
     * 自定义实体父类公共字段
     */
    public DataSourceBuilder ignoreColumns(String... ignoreColumns){
        configurer.getDataSourceConfig().setIgnoreColumns(new LinkedHashSet<>(Arrays.asList(ignoreColumns)));
        return this;
    }

    /**
     * 自定义实体父类公共字段
     */
    public DataSourceBuilder ignoreColumns(Collection<String> ignoreColumns){
        configurer.getDataSourceConfig().setIgnoreColumns(new LinkedHashSet<>(ignoreColumns));
        return this;
    }
    

    /**
     * 过滤字段前缀
     */
    public DataSourceBuilder columnPrefixes(String... columnPrefixes){
        configurer.getDataSourceConfig().setColumnPrefixes(new LinkedHashSet<>(Arrays.asList(columnPrefixes)));
        return this;
    }

    /**
     * 过滤字段前缀
     */
    public DataSourceBuilder columnPrefixes(Collection<String> columnPrefixes){
        configurer.getDataSourceConfig().setColumnPrefixes(new LinkedHashSet<>(columnPrefixes));
        return this;
    }


    /**
     * 过滤字段后缀
     */
    public DataSourceBuilder columnSuffixes(String... columnSuffixes){
        configurer.getDataSourceConfig().setColumnSuffixes(new LinkedHashSet<>(Arrays.asList(columnSuffixes)));
        return this;
    }

    /**
     * 过滤字段后缀
     */
    public DataSourceBuilder columnSuffixes(Collection<String> columnSuffixes){
        configurer.getDataSourceConfig().setColumnSuffixes(new LinkedHashSet<>(columnSuffixes));
        return this;
    }
    
    
    
    
    
    
    
    

}
