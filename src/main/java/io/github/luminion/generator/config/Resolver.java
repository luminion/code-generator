package io.github.luminion.generator.config;

/**
 * @author luminion
 * @since 1.0.0
 */

import io.github.luminion.generator.enums.TemplateFileEnum;
import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.util.StringUtils;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于为其他配置提供使用方法
 *
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class Resolver {
    private final Configurer configurer;

    /**
     * 获取父包名( parent.module.xxxx)
     *
     * @return {@link String }
     * @since 1.0.0
     */
    public String getParentPackage() {
        String outputModule = configurer.getGlobalConfig().getOutputModule();
        String outputParentPackage = configurer.getGlobalConfig().getOutputParentPackage();
        if (StringUtils.isNotBlank(outputModule)) {
            return outputParentPackage + "." + outputModule;
        }
        return outputParentPackage;
    }

    /**
     * 连接父子包名
     *
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    public String joinPackage(String subPackage) {
        String parent = getParentPackage();
        return StringUtils.isBlank(parent) ? subPackage : (parent + "." + subPackage);
    }
    
    /**
     * 获取模板文件
     *
     * @return {@link List }<{@link TemplateFile }>
     * @since 1.0.0
     */
    public List<TemplateFile> getTemplateFiles() {
        // todo 获取对应的文件输出信息
        return new ArrayList<>();
    }

    /**
     * 是否生成指定模板文件
     *
     * @param templateFileEnum 输出文件
     * @return {@link TemplateFileEnum }
     * @since 1.0.0
     */
    public boolean isGenerate(TemplateFileEnum templateFileEnum) {
        // todo 获取对应的文件输出信息
        return false;
    }

    /**
     * 获取类简单名称
     *
     * @param templateFileEnum 模板文件枚举
     * @return {@link String }
     * @since 1.0.0
     */
    public String getClassSimpleName(TemplateFileEnum templateFileEnum) {
        // todo 获取对应的文件输出信息
        return "";
    }

    /**
     * 获取类规范名称
     *
     * @param templateFileEnum 模板文件枚举
     * @return {@link String }
     * @since 1.0.0
     */
    public String getClassCanonicalName(TemplateFileEnum templateFileEnum) {
        // todo 获取对应的文件输出信息
        return "";
    }


}
