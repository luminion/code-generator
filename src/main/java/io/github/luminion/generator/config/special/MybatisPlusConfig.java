package io.github.luminion.generator.config.special;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import io.github.luminion.generator.common.JavaFieldInfo;
import io.github.luminion.generator.common.TemplateRender;
import io.github.luminion.generator.config.ConfigCollector;
import io.github.luminion.generator.config.ConfigResolver;
import io.github.luminion.generator.enums.RuntimeClass;
import io.github.luminion.generator.po.TableField;
import io.github.luminion.generator.po.TableInfo;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

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
        // 设置表信息
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
    }

    @Override
    public Map<String, Object> renderData(TableInfo tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("idType", idType == null ? null : idType.toString());
        data.put("logicDeleteColumnName", this.logicDeleteColumnName);
        data.put("versionColumnName", this.versionColumnName);
        data.put("activeRecord", this.activeRecord);
        data.put("tableFieldAnnotation", this.tableFieldAnnotation);
        data.put("tableFillMap", this.tableFillMap);
        return data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void renderDataPostProcess(TableInfo tableInfo, Map<String, Object> renderData) {
        ConfigResolver configResolver = tableInfo.getConfigResolver();
        ConfigCollector<?> configCollector = configResolver.getConfigCollector();

        // 追加导入包
        Set<String> entityImportPackages = new TreeSet<>();
        if (this.tableFieldAnnotation) {
            entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getClassName());
        }
        if (this.activeRecord) {
            entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_ACTIVE_RECORD_MODEL.getClassName());
        }
        if (tableInfo.isConvert()) {
            entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_NAME.getClassName());
        }
        if (this.activeRecord) {
            entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_ACTIVE_RECORD_MODEL.getClassName());
            entityImportPackages.add(RuntimeClass.JAVA_IO_SERIALIZABLE.getClassName());
        }
        if (configCollector.getGlobalConfig().isLombok() && this.activeRecord) {
            entityImportPackages.add(RuntimeClass.LOMBOK_EQUALS_AND_HASH_CODE.getClassName());
        }
        tableInfo.getFields().forEach(field -> {
            JavaFieldInfo columnType = field.getJavaType();
            if (null != columnType && null != columnType.getPkg()) {
                entityImportPackages.add(columnType.getPkg());
            }
            if (field.isKeyFlag()) {
                // 主键
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_ID.getClassName());
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_ID_TYPE.getClassName());
            } else if (field.isConvert() || this.tableFieldAnnotation) {
                // 普通字段
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getClassName());
            }
            if (null != field.getFill()) {
                // 填充字段
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_FIELD.getClassName());
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_FIELD_FILL.getClassName());
            }
            if (field.isVersionField()) {
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_VERSION.getClassName());
            }
            if (field.isLogicDeleteField()) {
                entityImportPackages.add(RuntimeClass.MYBATIS_PLUS_TABLE_LOGIC.getClassName());
            }
        });
        Collection<String> entityJavaPackages = entityImportPackages.stream().filter(pkg -> pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> entityFramePackages = entityImportPackages.stream().filter(pkg -> !pkg.startsWith("java")).collect(Collectors.toList());
        Collection<String> entityFramePkg = (Collection<String>) renderData.get("entityFramePkg");
        Collection<String> entityJavaPkg = (Collection<String>) renderData.get("entityJavaPkg");
        entityFramePkg.addAll(entityFramePackages);
        entityJavaPkg.addAll(entityJavaPackages);
    }
}
