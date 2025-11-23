package io.github.luminion.generator.config.custom;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class MybatisPlusConfig implements TemplateRender {
    /**
     * 指定生成的主键的ID类型
     */
    protected IdType idType = IdType.ASSIGN_ID;
    /**
     * 乐观锁字段名称(数据库字段)
     */
    protected String versionColumnName;
    /**
     * 逻辑删除字段名称(数据库字段)
     */
    protected String logicDeleteColumnName;
    /**
     * 开启 ActiveRecord 模式
     */
    protected boolean activeRecord;
    /**
     * 是否生成实体时，生成字段注解
     */
    protected boolean tableFieldAnnotation;
    /**
     * 表填充字段
     */
    protected final Map<String, FieldFill> tableFillMap = new HashMap<>();

    @Override
    public int order() {
        return -1;
    }

    @Override
    public void renderDataPreProcess(TableInfo tableInfo) {
        Configurer<?> configurer = tableInfo.getResolver().getConfigurer();

        for (TableField field : tableInfo.getFields()) {
            String columnName = field.getColumnName();
            if (columnName != null && columnName.equals(this.logicDeleteColumnName)) {
                field.setLogicDeleteField(true);
            }
            if (columnName != null && columnName.equals(this.versionColumnName)) {
                field.setVersionField(true);
            }
            tableFillMap.entrySet().stream()
                    //忽略大写字段问题
                    .filter(entry -> entry.getKey().equalsIgnoreCase(field.getName()))
                    .findFirst()
                    .ifPresent(entry -> field.setFill(entry.getValue().name()));
        }

        // service

        // serviceImpl

        // mapper


        // 实体类
        Set<String> entityImportPackages = configurer.getEntityConfig().getImportPackages();
        if (this.tableFieldAnnotation) {
            entityImportPackages.add("com.baomidou.mybatisplus.annotation.TableField");
        }
        if (this.activeRecord) {
            entityImportPackages.add("com.baomidou.mybatisplus.extension.activerecord.Model");
        }
        if (tableInfo.isConvert()) {
            entityImportPackages.add("com.baomidou.mybatisplus.annotation.TableName");
        }
        if (this.activeRecord) {
            configurer.getControllerConfig().setSuperClass(null);
            entityImportPackages.add("com.baomidou.mybatisplus.extension.activerecord.Model");
            entityImportPackages.add("java.io.Serializable");
        }
        if (configurer.getGlobalConfig().isLombok() && this.activeRecord) {
            entityImportPackages.add("lombok.EqualsAndHashCode");
        }
        tableInfo.getFields().forEach(field -> {
            JavaFieldInfo columnType = field.getJavaType();
            if (null != columnType && null != columnType.getPkg()) {
                entityImportPackages.add(columnType.getPkg());
            }
            if (field.isKeyFlag()) {
                // 主键
                entityImportPackages.add("com.baomidou.mybatisplus.annotation.TableId");
                entityImportPackages.add("com.baomidou.mybatisplus.annotation.IdType");
            } else if (field.isConvert() || this.tableFieldAnnotation) {
                // 普通字段
                entityImportPackages.add("com.baomidou.mybatisplus.annotation.TableField");
            }
            if (null != field.getFill()) {
                // 填充字段
                entityImportPackages.add("com.baomidou.mybatisplus.annotation.TableField");
                entityImportPackages.add("com.baomidou.mybatisplus.annotation.FieldFill");
            }
            if (field.isVersionField()) {
                entityImportPackages.add("com.baomidou.mybatisplus.annotation.Version");
            }
            if (field.isLogicDeleteField()) {
                entityImportPackages.add("com.baomidou.mybatisplus.annotation.TableLogic");
            }
        });
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = TemplateRender.super.renderData(tableInfo);
        data.put("idType", idType == null ? null : idType.toString());
        data.put("logicDeleteColumnName", this.logicDeleteColumnName);
        data.put("versionColumnName", this.versionColumnName);
        data.put("activeRecord", this.activeRecord);
        data.put("tableFieldAnnotation", this.tableFieldAnnotation);
        data.put("tableFillMap", this.tableFillMap);
        return data;
    }
}
