package io.github.luminion.generator.po;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luminion
 * @since 1.0.0
 */
@Data
public class TableInfo {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 公共字段
     */
    private final List<TableField> commonFields = new ArrayList<>();

    /**
     * 表字段
     */
    private final List<TableField> fields = new ArrayList<>();

    /**
     * 额外字段
     */
    private final List<TableSuffixField> extraFields = new ArrayList<>();

    /**
     * 是否有主键
     */
    private boolean havePrimaryKey;

    /**
     * 主键字段
     */
    private TableField idField;
    


    public String getEntityPath() {
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    }

    public boolean isConvert() {
        return !tableName.equals(entityName);
    }

    public String getBaseResultColumns() {
        return this.fields.stream()
                .map(TableField::getColumnName)
                .collect(Collectors.joining(", "));
    }
}
