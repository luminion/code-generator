package io.github.luminion.generator.config.support.adapter;

import io.github.luminion.generator.config.enums.IdType;
import io.github.luminion.generator.config.rules.NamingStrategy;
import io.github.luminion.generator.config.support.EntityConfig;
import io.github.luminion.generator.config.fill.IFill;
import io.github.luminion.generator.util.ClassUtils;
import io.github.luminion.generator.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author luminion
 * @since 1.0.0
 */
@Slf4j
public class EntityAdapter {
    private final EntityConfig entityConfig;

    public EntityAdapter(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    /**
     * 自定义继承的Entity类全称
     *
     * @param clazz 类
     * @return this
     */
    public EntityAdapter superClass(Class<?> clazz) {
        return superClass(clazz.getName());
    }

    /**
     * 自定义继承的Entity类全称，带包名
     *
     * @param superEntityClass 类全称
     * @return this
     */
    public EntityAdapter superClass(String superEntityClass) {
        this.entityConfig.setSuperClass(superEntityClass);
        if (StringUtils.isNotBlank(superEntityClass)) {
            try {
                Optional.of(ClassUtils.toClassConfident(superEntityClass)).ifPresent(this.entityConfig::convertSuperEntityColumns);
            } catch (Exception e) {
                //当父类实体存在类加载器的时候,识别父类实体字段，不存在的情况就只有通过指定superEntityColumns属性了。
            }
        } else {
            if (!this.entityConfig.getSuperEntityColumns().isEmpty()) {
                log.warn("Forgot to set entity supper class ?");
            }
        }
        return this;
    }

    /**
     * 设置乐观锁数据库表字段名称
     *
     * @param versionColumnName 乐观锁数据库字段名称
     * @return this
     */
    public EntityAdapter versionColumnName(String versionColumnName) {
        this.entityConfig.setVersionColumnName(versionColumnName);
        return this;
    }

    /**
     * 设置乐观锁实体属性字段名称
     *
     * @param versionPropertyName 乐观锁实体属性字段名称
     * @return this
     */
    public EntityAdapter versionPropertyName(String versionPropertyName) {
        this.entityConfig.setVersionPropertyName(versionPropertyName);
        return this;
    }

    /**
     * 逻辑删除数据库字段名称
     *
     * @param logicDeleteColumnName 逻辑删除字段名称
     * @return this
     */
    public EntityAdapter logicDeleteColumnName(String logicDeleteColumnName) {
        this.entityConfig.setLogicDeleteColumnName(logicDeleteColumnName);
        return this;
    }

    /**
     * 逻辑删除实体属性名称
     *
     * @param logicDeletePropertyName 逻辑删除实体属性名称
     * @return this
     */
    public EntityAdapter logicDeletePropertyName(String logicDeletePropertyName) {
        this.entityConfig.setLogicDeletePropertyName(logicDeletePropertyName);
        return this;
    }

    /**
     * 数据库表映射到实体的命名策略
     *
     * @param namingStrategy 数据库表映射到实体的命名策略
     * @return this
     */
    public EntityAdapter naming(NamingStrategy namingStrategy) {
        this.entityConfig.setNaming(namingStrategy);
        return this;
    }

    /**
     * 数据库表字段映射到实体的命名策略
     *
     * @param namingStrategy 数据库表字段映射到实体的命名策略
     * @return this
     */
    public EntityAdapter columnNaming(NamingStrategy namingStrategy) {
        this.entityConfig.setColumnNaming(namingStrategy);
        return this;
    }

    /**
     * 添加父类公共字段
     *
     * @param superEntityColumns 父类字段(数据库字段列名)
     * @return this
     * @since 3.5.0
     */
    public EntityAdapter addSuperEntityColumns(String... superEntityColumns) {
        return addSuperEntityColumns(Arrays.asList(superEntityColumns));
    }

    public EntityAdapter addSuperEntityColumns(List<String> superEntityColumnList) {
        this.entityConfig.getSuperEntityColumns().addAll(superEntityColumnList);
        return this;
    }

    /**
     * 添加忽略字段
     *
     * @param ignoreColumns 需要忽略的字段(数据库字段列名)
     * @return this
     * @since 3.5.0
     */
    public EntityAdapter addIgnoreColumns(String... ignoreColumns) {
        return addIgnoreColumns(Arrays.asList(ignoreColumns));
    }

    public EntityAdapter addIgnoreColumns(List<String> ignoreColumnList) {
        this.entityConfig.getIgnoreColumns().addAll(ignoreColumnList);
        return this;
    }

    /**
     * 添加表字段填充
     *
     * @param tableFills 填充字段
     * @return this
     * @since 3.5.0
     */
    public EntityAdapter addTableFills(IFill... tableFills) {
        return addTableFills(Arrays.asList(tableFills));
    }

    /**
     * 添加表字段填充
     *
     * @param tableFillList 填充字段集合
     * @return this
     * @since 3.5.0
     */
    public EntityAdapter addTableFills(List<IFill> tableFillList) {
        this.entityConfig.getTableFillList().addAll(tableFillList);
        return this;
    }

    /**
     * 指定生成的主键的ID类型
     *
     * @param idType ID类型
     * @return this
     * @since 3.5.0
     */
    public EntityAdapter idType(IdType idType) {
        this.entityConfig.setIdType(idType);
        return this;
    }

    /**
     * 启用生成 {@link java.io.Serial} (需JAVA 14)
     *
     * @return this
     * @since 3.5.11
     */
    public EntityAdapter enableSerialAnnotation() {
        this.entityConfig.setSerialAnnotation(true);
        return this;
    }

    /**
     * 开启生成字段常量
     *
     * @return this
     * @since 3.5.0
     */
    public EntityAdapter enableColumnConstant() {
        this.entityConfig.setColumnConstant(true);
        return this;
    }

    /**
     * 开启Boolean类型字段移除is前缀
     *
     * @return this
     * @since 3.5.0
     */
    public EntityAdapter enableRemoveIsPrefix() {
        this.entityConfig.setBooleanColumnRemoveIsPrefix(true);
        return this;
    }

    /**
     * 开启生成实体时生成字段注解
     *
     * @return this
     * @since 3.5.0
     */
    public EntityAdapter enableTableFieldAnnotation() {
        this.entityConfig.setTableFieldAnnotationEnable(true);
        return this;
    }

    /**
     * 开启 ActiveRecord 模式
     *
     * @return this
     * @since 3.5.0
     */
    public EntityAdapter enableActiveRecord() {
        this.entityConfig.setActiveRecord(true);
        return this;
    }

    /**
     * 禁用生成serialVersionUID
     *
     * @return this
     * @since 3.5.0
     */
    public EntityAdapter disableSerialVersionUID() {
        this.entityConfig.setSerialVersionUID(false);
        return this;
    }

}
