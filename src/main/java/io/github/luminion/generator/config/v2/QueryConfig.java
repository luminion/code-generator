package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.ExtraFieldStrategy;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.common.support.DefaultExtraFieldStrategy;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import io.github.luminion.generator.util.ClassUtils;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class QueryConfig implements TemplateRender {
    protected final Configurer configurer;

    /**
     * 生成id查询方法
     */
    protected boolean generateVoById = true;
    /**
     * 批量查询相关方法及配套类
     */
    protected boolean generateVoList = false;

    /**
     * 批量查询分页相关方法及配套类
     */
    protected boolean generateVoPage = true;

    /**
     * 额外字段后缀
     */
    protected final Map<String, String> extraFieldSuffixMap = new LinkedHashMap<>();

    /**
     * 额外字段策略
     */
    protected ExtraFieldStrategy extraFieldStrategy = new DefaultExtraFieldStrategy();
    /**
     * 当前页码参数名
     */
    protected String pageName = "current";

    /**
     * 每页条数参数名
     */
    protected String sizeName = "size";

    /**
     * 查询dto父类全限定名
     */
    protected String queryDtoSuperClass;


    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("generateVoById", this.generateVoById);
        data.put("generateVoList", this.generateVoList);
        data.put("generateVoPage", this.generateVoPage);
        data.put("extraFieldSuffixMap", extraFieldSuffixMap);
        data.put("extraFieldStrategy", extraFieldStrategy);
        data.put("pageName", pageName);
        data.put("sizeName", sizeName);
        if (queryDtoSuperClass != null) {
            data.put("queryDtoSuperClass", ClassUtils.getSimpleName(queryDtoSuperClass));
            data.put("queryDtoSuperClassCanonicalName", queryDtoSuperClass);
            // 假设用户没改配置，值是 "pageNum" 和 "pageSize"
            String pageGetter = "get"
                    + pageName.substring(0, 1).toUpperCase()
                    + pageName.substring(1)
                    + "()";
            String sizeGetter = "get"
                    + sizeName.substring(0, 1).toUpperCase()
                    + sizeName.substring(1)
                    + "()";
            data.put("pageGetter", pageGetter);
            data.put("sizeGetter", sizeGetter);
        }

        resolveQueryDtoImports(data, tableInfo);
        resolveQueryVoImports(data, tableInfo);

        return data;
    }

    private void resolveQueryDtoImports(HashMap<String, Object> data, TableInfo tableInfo) {

        Set<String> importPackages = new TreeSet<>();
        importPackages.add(RuntimeClass.JAVA_UTIL_LIST.getClassName());

        for (TableField field : tableInfo.getFields()) {
            if (field.isLogicDeleteField()) {
                continue;
            }
            Optional.ofNullable(field.getPropertyPkg()).ifPresent(importPackages::add);
        }

        // 全局包
        importPackages.addAll(configurer.getGlobalConfig().getModelDocImportPackages());
        importPackages.addAll(configurer.getStrategyConfig().getModelImportPackages());

        // 导入包
        Collection<String> frameworkPackages = importPackages.stream()
                .filter(pkg -> !pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        Collection<String> javaPackages = importPackages.stream()
                .filter(pkg -> pkg.startsWith("java"))
                .collect(Collectors.toCollection(TreeSet::new));
        data.put("queryDtoFramePkg", frameworkPackages);
        data.put("queryDtoJavaPkg", javaPackages);

    }

    private void resolveQueryVoImports(HashMap<String, Object> data, TableInfo tableInfo) {

    }
}
