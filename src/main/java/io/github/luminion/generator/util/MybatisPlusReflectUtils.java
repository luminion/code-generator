package io.github.luminion.generator.util;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;

/**
 * @author luminion
 * @since 1.0.0
 */
public class MybatisPlusReflectUtils {

    /**
     * <p>
     * 父类 Class 反射属性转换为公共字段
     * </p>
     *
     * @param clazz 实体父类 Class
     */
    public static void convertSuperEntityColumns(Class<?> clazz) {
        // todo 转化父类字段
        List<Field> fields = TableInfoHelper.getAllFields(clazz);
        this.superEntityColumns.addAll(fields.stream().map(field -> {
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId != null && StringUtils.isNotBlank(tableId.value())) {
                return tableId.value();
            }
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && StringUtils.isNotBlank(tableField.value())) {
                return tableField.value();
            }
            if (null == columnNaming || columnNaming == NamingStrategy.no_change) {
                return field.getName();
            }
            return StringUtils.camelToUnderline(field.getName());
        }).collect(Collectors.toSet()));
    }


}
