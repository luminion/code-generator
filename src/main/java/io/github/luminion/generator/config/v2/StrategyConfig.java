package io.github.luminion.generator.config.v2;

import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.po.TableInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author luminion
 * @since 1.0.0
 */
public class StrategyConfig implements TemplateRender {

    /**
     * 是否为lombok模型（默认 false）
     */
    protected boolean lombok = true;

    /**
     * 是否为链式模型setter（默认 false）
     */
    protected boolean chainModel;

    /**
     * 实体是否生成 serialVersionUID
     */
    protected boolean serializableUID = false;

    /**
     * 实体是否启用java.io.Serial (需JAVA 14) 注解
     *
     */
    protected boolean serializableAnnotation = true;


    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    protected boolean booleanColumnRemoveIsPrefix;

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();

        data.put("lombok", this.lombok);
        data.put("chainModel", this.chainModel);
        
        data.put("serializableUID", this.serializableUID);
        data.put("serializableAnnotation", this.serializableAnnotation);

        data.put("booleanColumnRemoveIsPrefix", booleanColumnRemoveIsPrefix);

 
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
                importPackages.add("lombok.experimental.Accessors");
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
