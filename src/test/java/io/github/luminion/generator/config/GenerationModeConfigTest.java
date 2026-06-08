package io.github.luminion.generator.config;

import io.github.luminion.generator.engine.VelocityTemplateEngine;
import io.github.luminion.generator.enums.JdbcType;
import io.github.luminion.generator.enums.RuntimeEnv;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.util.InitializeUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerationModeConfigTest {

    @Test
    void pureMybatisGeneratesPlainMapperAndHidesSqlBooster() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatis(configurer);

        Rendered rendered = renderAll(configurer);

        assertEquals(RuntimeEnv.MYBATIS, configurer.getGlobalConfig().getRuntimeEnv());
        assertTrue(rendered.mapper.contains("public interface SysUserMapper {"), rendered.mapper);
        assertTrue(rendered.mapper.contains("int insert(SysUser entity);"), rendered.mapper);
        assertTrue(rendered.mapper.contains("List<SysUserVO> selectByXml(SysUserQueryDTO param, RowBounds rowBounds);"), rendered.mapper);
        assertTrue(rendered.mapperXml.contains("<insert id=\"insert\""), rendered.mapperXml);
        assertTrue(rendered.mapperXml.contains("<include refid=\"conditions\"/>"), rendered.mapperXml);
        assertFalse(rendered.mapperXml.contains("sqlbooster.conditions"), rendered.mapperXml);
        assertTrue(rendered.service.contains("List<SysUserVO> voPage(SysUserQueryDTO param, long current, long size);"), rendered.service);
        assertTrue(rendered.serviceImpl.contains("new RowBounds((int) ((current - 1) * size), (int) size)"), rendered.serviceImpl);
        assertFalse(rendered.controller.contains("lambdaBooster()"), rendered.controller);
        assertFalse(rendered.entity.contains("@TableName"), rendered.entity);
        assertFalse(rendered.entity.contains("@TableId"), rendered.entity);
    }

    @Test
    void mybatisPlusKeepsExistingBaseMapperAndServiceShape() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisPlus(configurer);

        Rendered rendered = renderAll(configurer);

        assertEquals(RuntimeEnv.MYBATIS_PLUS, configurer.getGlobalConfig().getRuntimeEnv());
        assertTrue(rendered.mapper.contains("extends BaseMapper<SysUser>"), rendered.mapper);
        assertTrue(rendered.mapper.contains("List<SysUserVO> selectByXml(SysUserQueryDTO param, IPage<SysUserVO> page);"), rendered.mapper);
        assertTrue(rendered.service.contains("extends IService<SysUser>"), rendered.service);
        assertTrue(rendered.service.contains("IPage<SysUserVO> voPage(SysUserQueryDTO param, long current, long size);"), rendered.service);
        assertTrue(rendered.serviceImpl.contains("extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService"), rendered.serviceImpl);
        assertTrue(rendered.serviceImpl.contains("Page<SysUserVO> page = new Page<>(current, size);"), rendered.serviceImpl);
        assertFalse(rendered.controller.contains("lambdaBooster()"), rendered.controller);
    }

    @Test
    void mybatisSqlBoosterIntegratedExposesBoosterServiceApi() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisSqlBooster(configurer);

        Rendered rendered = renderAll(configurer);

        assertEquals(RuntimeEnv.MYBATIS_BOOSTER, configurer.getGlobalConfig().getRuntimeEnv());
        assertTrue(rendered.mapper.contains("extends PhMapper<SysUser, SysUserVO>"), rendered.mapper);
        assertFalse(rendered.mapper.contains("selectByXml("), rendered.mapper);
        assertTrue(rendered.mapperXml.contains("sqlbooster.conditions"), rendered.mapperXml);
        assertTrue(rendered.service.contains("extends BoosterService<SysUser, SysUserVO>"), rendered.service);
        assertFalse(rendered.service.contains("voList(SysUserQueryDTO"), rendered.service);
        assertTrue(rendered.controller.contains("baseService.lambdaBooster().fromBean(param).list()"), rendered.controller);
        assertTrue(rendered.controller.contains("baseService.lambdaBooster().fromBean(param).page("), rendered.controller);
    }

    @Test
    void mybatisPlusSqlBoosterIntegratedKeepsExistingMpBoosterShape() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMpBooster(configurer);

        Rendered rendered = renderAll(configurer);

        assertEquals(RuntimeEnv.MP_BOOSTER, configurer.getGlobalConfig().getRuntimeEnv());
        assertTrue(rendered.mapper.contains("extends MpMapper<SysUser, SysUserVO>"), rendered.mapper);
        assertTrue(rendered.service.contains("extends MpService<SysUser, SysUserVO>"), rendered.service);
        assertTrue(rendered.serviceImpl.contains("extends MpServiceImpl<SysUserMapper, SysUser, SysUserVO> implements SysUserService"), rendered.serviceImpl);
        assertTrue(rendered.controller.contains("baseService.lambdaBooster().fromBean(param).list()"), rendered.controller);
    }

    @Test
    void mybatisPlusSqlBoosterContextHidesBoosterBehindServiceMethods() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisPlusSqlBoosterContext(configurer);

        Rendered rendered = renderAll(configurer);

        assertEquals(RuntimeEnv.MP_SQL_BOOSTER, configurer.getGlobalConfig().getRuntimeEnv());
        assertTrue(rendered.mapper.contains("extends BaseMapper<SysUser>"), rendered.mapper);
        assertTrue(rendered.mapper.contains("List<SysUserVO> selectByXml(SqlContext<SysUser> sqlContext, IPage<SysUserVO> page);"), rendered.mapper);
        assertTrue(rendered.mapperXml.contains("sqlbooster.conditions"), rendered.mapperXml);
        assertTrue(rendered.service.contains("IPage<SysUserVO> voPage(SysUserQueryDTO param, long current, long size);"), rendered.service);
        assertTrue(rendered.serviceImpl.contains("SqlContext<SysUser> sqlContext = SqlBuilder.of(SysUser.class).fromBean(param).toSqlContext();"), rendered.serviceImpl);
        assertTrue(rendered.serviceImpl.contains("getBaseMapper().selectByXml(sqlContext, page);"), rendered.serviceImpl);
        assertFalse(rendered.controller.contains("lambdaBooster()"), rendered.controller);
    }

    @Test
    void mybatisSqlBoosterContextUsesPlainMapperAndHidesBoosterBehindServiceMethods() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisSqlBoosterContext(configurer);

        Rendered rendered = renderAll(configurer);

        assertEquals(RuntimeEnv.MYBATIS_SQL_BOOSTER, configurer.getGlobalConfig().getRuntimeEnv());
        assertTrue(rendered.mapper.contains("public interface SysUserMapper {"), rendered.mapper);
        assertTrue(rendered.mapper.contains("List<SysUserVO> selectByXml(SqlContext<SysUser> sqlContext, RowBounds rowBounds);"), rendered.mapper);
        assertTrue(rendered.mapperXml.contains("sqlbooster.conditions"), rendered.mapperXml);
        assertTrue(rendered.service.contains("List<SysUserVO> voPage(SysUserQueryDTO param, long current, long size);"), rendered.service);
        assertTrue(rendered.serviceImpl.contains("SqlContext<SysUser> sqlContext = SqlBuilder.of(SysUser.class).fromBean(param).toSqlContext();"), rendered.serviceImpl);
        assertTrue(rendered.serviceImpl.contains("baseMapper.selectByXml(sqlContext, rowBounds);"), rendered.serviceImpl);
        assertFalse(rendered.controller.contains("lambdaBooster()"), rendered.controller);
    }

    @Test
    void mybatisPageHelperUsesPageInfoAndPlainMapperMethods() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisPageHelper(configurer);

        Rendered rendered = renderAll(configurer);

        assertEquals(RuntimeEnv.MYBATIS_PAGE_HELPER, configurer.getGlobalConfig().getRuntimeEnv());
        assertTrue(rendered.mapper.contains("public interface SysUserMapper {"), rendered.mapper);
        assertTrue(rendered.mapper.contains("List<SysUserVO> selectByXml(SysUserQueryDTO param);"), rendered.mapper);
        assertTrue(rendered.service.contains("PageInfo<SysUserVO> voPage(SysUserQueryDTO param, long current, long size);"), rendered.service);
        assertTrue(rendered.serviceImpl.contains("PageHelper.startPage((int) current, (int) size);"), rendered.serviceImpl);
        assertTrue(rendered.serviceImpl.contains("return new PageInfo<>(list);"), rendered.serviceImpl);
        assertFalse(rendered.controller.contains("lambdaBooster()"), rendered.controller);
    }

    @Test
    void mybatisPageHelperSqlBoosterContextUsesPageInfoAndInternalSqlContext() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisPageHelperSqlBoosterContext(configurer);

        Rendered rendered = renderAll(configurer);

        assertEquals(RuntimeEnv.MYBATIS_PAGE_HELPER_SQL_BOOSTER, configurer.getGlobalConfig().getRuntimeEnv());
        assertTrue(rendered.mapper.contains("public interface SysUserMapper {"), rendered.mapper);
        assertTrue(rendered.mapper.contains("List<SysUserVO> selectByXml(SqlContext<SysUser> sqlContext);"), rendered.mapper);
        assertTrue(rendered.mapperXml.contains("sqlbooster.conditions"), rendered.mapperXml);
        assertTrue(rendered.service.contains("PageInfo<SysUserVO> voPage(SysUserQueryDTO param, long current, long size);"), rendered.service);
        assertTrue(rendered.serviceImpl.contains("SqlContext<SysUser> sqlContext = SqlBuilder.of(SysUser.class).fromBean(param).toSqlContext();"), rendered.serviceImpl);
        assertTrue(rendered.serviceImpl.contains("PageHelper.startPage((int) current, (int) size);"), rendered.serviceImpl);
        assertFalse(rendered.controller.contains("lambdaBooster()"), rendered.controller);
    }

    @Test
    void mybatisLogicDeleteUsesResolvedBooleanValuesInsteadOfPhysicalDelete() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatis(configurer);

        Rendered rendered = renderAll(configurer, sampleLogicDeleteTableInfo("Boolean", "java.lang.Boolean", JdbcType.BOOLEAN, "bool", "false"));

        assertTrue(rendered.mapperXml.contains("<update id=\"deleteById\">"), rendered.mapperXml);
        assertTrue(rendered.mapperXml.contains("SET deleted = true"), rendered.mapperXml);
        assertTrue(rendered.mapperXml.contains("AND a.deleted = false"), rendered.mapperXml);
        assertFalse(rendered.mapperXml.contains("<delete id=\"deleteById\">"), rendered.mapperXml);
    }

    @Test
    void mybatisPlusTableLogicUsesExplicitResolvedValues() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisPlus(configurer);

        Rendered rendered = renderAll(configurer, sampleLogicDeleteTableInfo("Integer", "java.lang.Integer", JdbcType.TINYINT, "tinyint", "0"));

        assertTrue(rendered.entity.contains("@TableLogic(value = \"0\", delval = \"1\")"), rendered.entity);
        assertTrue(rendered.mapperXml.contains("AND a.deleted = 0"), rendered.mapperXml);
    }

    @Test
    void unknownLogicDeleteValueFallsBackToNullQueryAndPhysicalDelete() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatis(configurer);

        Rendered rendered = renderAll(configurer, sampleLogicDeleteTableInfo("Object", "java.lang.Object", JdbcType.OTHER, "jsonb", null));

        assertTrue(rendered.mapperXml.contains("<delete id=\"deleteById\">"), rendered.mapperXml);
        assertTrue(rendered.mapperXml.contains("AND a.deleted IS NULL"), rendered.mapperXml);
        assertFalse(rendered.mapperXml.contains("@TableLogic(value"), rendered.entity);
    }

    private static Rendered renderAll(Configurer configurer) throws Exception {
        return renderAll(configurer, sampleTableInfo());
    }

    private static Rendered renderAll(Configurer configurer, TableInfo tableInfo) throws Exception {
        VelocityTemplateEngine engine = new VelocityTemplateEngine(configurer);
        Map<String, Object> renderData = configurer.renderMap(configurer.createRenderContext(tableInfo));
        return new Rendered(
                engine.writer(renderData, "entity", readResource("templates/entity.java.vm")),
                engine.writer(renderData, "mapper", readResource("templates/mapper.java.vm")),
                engine.writer(renderData, "mapperXml", readResource("templates/mapper.xml.vm")),
                engine.writer(renderData, "service", readResource("templates/service.java.vm")),
                engine.writer(renderData, "serviceImpl", readResource("templates/serviceImpl.java.vm")),
                engine.writer(renderData, "controller", readResource("templates/controller.java.vm"))
        );
    }

    private static String readResource(String path) throws IOException {
        try (InputStream inputStream = GenerationModeConfigTest.class.getClassLoader().getResourceAsStream(path)) {
            assertNotNull(inputStream, "Missing resource: " + path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toString(StandardCharsets.UTF_8.name());
        }
    }

    private static TableInfo sampleTableInfo() {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("sys_user");
        tableInfo.setEntityName("SysUser");
        tableInfo.setComment("系统用户");

        TableField idField = field("id", "id", "Long", "java.lang.Long", "主键");
        idField.setPrimaryKey(true);

        TableField nameField = field("username", "username", "String", "java.lang.String", "用户名");
        TableField statusField = field("status", "status", "Integer", "java.lang.Integer", "状态");

        tableInfo.setPrimaryKeyField(idField);
        tableInfo.setHasPrimaryKey(true);
        tableInfo.getFields().add(idField);
        tableInfo.getFields().add(nameField);
        tableInfo.getFields().add(statusField);
        return tableInfo;
    }

    private static TableInfo sampleLogicDeleteTableInfo(String propertyType, String javaType, JdbcType jdbcType, String typeName, String defaultValue) {
        TableInfo tableInfo = sampleTableInfo();
        TableField logicDeleteField = field("deleted", "deleted", propertyType, javaType, "逻辑删除");
        logicDeleteField.setLogicDeleteField(true);
        logicDeleteField.getMetaInfo().setJdbcType(jdbcType);
        logicDeleteField.getMetaInfo().setTypeName(typeName);
        logicDeleteField.getMetaInfo().setDefaultValue(defaultValue);
        tableInfo.getFields().add(logicDeleteField);
        return tableInfo;
    }

    private static TableField field(String columnName, String propertyName, String propertyType, String javaType, String comment) {
        TableField field = new TableField();
        field.setRawColumnName(columnName);
        field.setColumnName(columnName);
        field.setPropertyName(propertyName);
        field.setPropertyType(propertyType);
        field.setJavaTypeCanonicalName(javaType);
        field.setComment(comment);
        field.setMetaInfo(metaInfo(false));
        return field;
    }

    private static TableField.MetaInfo metaInfo(boolean nullable) {
        TableField.MetaInfo metaInfo = new TableField.MetaInfo();
        metaInfo.setNullable(nullable);
        return metaInfo;
    }

    private static class Rendered {
        private final String entity;
        private final String mapper;
        private final String mapperXml;
        private final String service;
        private final String serviceImpl;
        private final String controller;

        private Rendered(String entity, String mapper, String mapperXml, String service, String serviceImpl, String controller) {
            this.entity = entity;
            this.mapper = mapper;
            this.mapperXml = mapperXml;
            this.service = service;
            this.serviceImpl = serviceImpl;
            this.controller = controller;
        }
    }
}
