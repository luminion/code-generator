package io.github.luminion.generator.config;

import io.github.luminion.generator.builder.ServiceBuilder;
import io.github.luminion.generator.engine.VelocityTemplateEngine;
import io.github.luminion.generator.metadata.TableField;
import io.github.luminion.generator.metadata.TableInfo;
import io.github.luminion.generator.util.InitializeUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceConfigTest {

    @Test
    void statelessServiceTemplateDoesNotExtendRuntimeServiceTypes() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMybatisPlus(configurer);
        new ServiceBuilder(configurer).enableStateless();

        TableInfo tableInfo = sampleTableInfo();
        String service = render(configurer, tableInfo, "service", "templates/service.java.vm");
        String serviceImpl = render(configurer, tableInfo, "serviceImpl", "templates/serviceImpl.java.vm");

        assertTrue(service.contains("public interface SysUserService {"));
        assertFalse(service.contains("extends IService<SysUser>"));
        assertTrue(serviceImpl.contains("public class SysUserServiceImpl implements SysUserService {"));
        assertTrue(serviceImpl.contains("private final SysUserMapper baseMapper;"));
        assertTrue(serviceImpl.contains("return baseMapper.updateById(entity) > 0;"));
        assertTrue(serviceImpl.contains("return baseMapper.deleteById(id) > 0;"));
        assertTrue(serviceImpl.contains("List<SysUserVO> list = baseMapper.selectByXml(dto, null);"));
        assertFalse(serviceImpl.contains("extends ServiceImpl<SysUserMapper, SysUser>"));
        assertFalse(serviceImpl.contains("getBaseMapper()"));
        assertFalse(serviceImpl.contains("super.save(entity)"));
    }

    @Test
    void statelessFlagKeepsSqlBoosterInheritanceBecauseBoosterQueriesNeedServiceBase() throws Exception {
        Configurer configurer = new Configurer("jdbc:h2:mem:test", "sa", "");
        InitializeUtils.initializeMpBooster(configurer);
        new ServiceBuilder(configurer).enableStateless();

        TableInfo tableInfo = sampleTableInfo();
        String service = render(configurer, tableInfo, "service", "templates/service.java.vm");
        String serviceImpl = render(configurer, tableInfo, "serviceImpl", "templates/serviceImpl.java.vm");

        assertTrue(service.contains("extends MpService<SysUser, SysUserVO>"), service);
        assertTrue(serviceImpl.contains("extends MpServiceImpl<SysUserMapper, SysUser, SysUserVO> implements SysUserService"));
        assertFalse(serviceImpl.contains("private final SysUserMapper baseMapper;"));
    }

    private static String render(Configurer configurer, TableInfo tableInfo, String key, String resourcePath) throws Exception {
        VelocityTemplateEngine engine = new VelocityTemplateEngine(configurer);
        Map<String, Object> renderData = configurer.renderMap(configurer.createRenderContext(tableInfo));
        return engine.writer(renderData, key, readResource(resourcePath));
    }

    private static String readResource(String path) throws IOException {
        try (InputStream inputStream = ServiceConfigTest.class.getClassLoader().getResourceAsStream(path)) {
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

        TableField idField = new TableField();
        idField.setRawColumnName("id");
        idField.setColumnName("id");
        idField.setPropertyName("id");
        idField.setPropertyType("Long");
        idField.setJavaTypeCanonicalName("java.lang.Long");
        idField.setComment("主键");
        idField.setPrimaryKey(true);
        idField.setMetaInfo(metaInfo(false));

        TableField nameField = new TableField();
        nameField.setRawColumnName("username");
        nameField.setColumnName("username");
        nameField.setPropertyName("username");
        nameField.setPropertyType("String");
        nameField.setJavaTypeCanonicalName("java.lang.String");
        nameField.setComment("用户名");
        nameField.setMetaInfo(metaInfo(false));

        tableInfo.setPrimaryKeyField(idField);
        tableInfo.setHasPrimaryKey(true);
        tableInfo.getFields().add(idField);
        tableInfo.getFields().add(nameField);
        return tableInfo;
    }

    private static TableField.MetaInfo metaInfo(boolean nullable) {
        TableField.MetaInfo metaInfo = new TableField.MetaInfo();
        metaInfo.setNullable(nullable);
        return metaInfo;
    }
}
