package io.github.luminion.generator.render.api;

import io.github.luminion.generator.po.TemplateFile;
import io.github.luminion.generator.render.engine.TemplateRender;

import java.util.List;

/**
 * 多模板文件渲染器接口
 * <p>
 * 用于整合多个配置的场景，如 QueryConfig 包含 QueryDTO 和 QueryVO
 *
 * @author luminion
 * @since 1.0.0
 */
public interface MultiTemplateModelRender extends TemplateRender {

    /**
     * 获取需要渲染的多个模板文件
     *
     * @return 模板文件列表
     */
    List<TemplateFile> renderTemplateFiles();
}
