package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class StrategyConfig implements TemplateRender {
    private final Configurer configurer;

    /**
     * 是否为lombok模型（默认 false）
     */
    private boolean lombok = true;

    /**
     * 是否为链式模型setter（默认 false）
     */
    private boolean chainModel;

    /**
     * 实体是否生成 serialVersionUID
     */
    private boolean serializableUID = false;

    /**
     * 实体是否启用java.io.Serial (需JAVA 14) 注解
     *
     */
    private boolean serializableAnnotation = true;




    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();

        data.put("lombok", this.lombok);
        data.put("chainModel", this.chainModel);
        
        data.put("serializableUID", this.serializableUID);
        data.put("serializableAnnotation", this.serializableAnnotation);


 
        return data;
    }



    

    /**
     * 获取领域类需要导入的包
     *
     * @return 包
     */
    public Set<String> getModelImportPackages() {
        Set<String> importPackages = new TreeSet<>();
        if (this.lombok) {
            if (this.chainModel) {
                importPackages.add(RuntimeClass.LOMBOK_ACCESSORS.getClassName());
            }
            importPackages.add(RuntimeClass.LOMBOK_DATA.getClassName());
        }
        if (this.serializableUID) {
            importPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
            if (this.serializableAnnotation) {
                importPackages.add(RuntimeClass.JAVA_IO_SERIAL.getClassName());
            }
        }
        return importPackages;
    }
    
}
