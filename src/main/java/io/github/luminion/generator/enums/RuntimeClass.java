package io.github.luminion.generator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author luminion
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum RuntimeClass {
    JAVA_IO_SERIALIZABLE("java.io.Serializable"),
    JAVA_IO_SERIAL("java.io.Serial"),
    JAVA_IO_IOEXCEPTION("java.io.IOException"),
    JAVA_IO_INPUT_STREAM("java.io.InputStream"),
    JAVA_IO_OUTPUT_STREAM("java.io.OutputStream"),
    JAVA_UTIL_LIST("java.util.List"),
    JAVA_UTIL_COLLECTIONS("java.util.Collections"),
    JAVA_STREAM_COLLECTORS("java.util.stream.Collectors"),
    
    MYBATIS_PLUS_I_SERVICE("com.baomidou.mybatisplus.extension.service.IService"),
    MYBATIS_PLUS_SERVICE_IMPL("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl"),
    MYBATIS_PLUS_BASE_MAPPER("com.baomidou.mybatisplus.core.mapper.BaseMapper"),
    MYBATIS_PLUS_I_PAGE("com.baomidou.mybatisplus.core.metadata.IPage"),
    MYBATIS_PLUS_PAGE("com.baomidou.mybatisplus.extension.plugins.pagination.Page"),
    
    LOMBOK_EQUALS_AND_HASH_CODE("lombok.EqualsAndHashCode"),
    LOMBOK_DATA("lombok.Data"),
    LOMBOK_ACCESSORS("lombok.experimental.Accessors"),
    LOMBOK_REQUIRED_ARGS_CONSTRUCTOR("lombok.RequiredArgsConstructor"),
    
    SWAGGER_V2_API("io.swagger.annotations.Api"),
    SWAGGER_V2_API_OPERATION("io.swagger.annotations.ApiOperation"),
    SWAGGER_V2_API_IMPLICIT_PARAM("io.swagger.annotations.ApiImplicitParam"),
    SWAGGER_V2_API_IMPLICIT_PARAMS("io.swagger.annotations.ApiImplicitParams"),
    SWAGGER_V2_API_MODEL("io.swagger.annotations.ApiModel"),
    SWAGGER_V2_API_MODEL_PROPERTY("io.swagger.annotations.ApiModelProperty"),
    
    SWAGGER_V3_TAG("io.swagger.v3.oas.annotations.tags.Tag"),
    SWAGGER_V3_OPERATION("io.swagger.v3.oas.annotations.Operation"),
    SWAGGER_V3_PARAMETER("io.swagger.v3.oas.annotations.Parameter"),
    SWAGGER_V3_PARAMETERS("io.swagger.v3.oas.annotations.Parameters"),
    SWAGGER_V3_SCHEMA("io.swagger.v3.oas.annotations.media.Schema"),
    
    SPRING_BOOT_WEB_ANNOTATION_S("org.springframework.web.bind.annotation.*"),
    SPRING_BOOT_CONTROLLER("org.springframework.stereotype.Controller"),
    SPRING_BOOT_SERVICE("org.springframework.stereotype.Service"),
    SPRING_BOOT_VALIDATED("org.springframework.validation.annotation.Validated"),
    SPRING_BOOT_MULTIPART_FILE("org.springframework.web.multipart.MultipartFile"),
    SPRING_BOOT_BEAN_UTILS("org.springframework.beans.BeanUtils"),

    SQL_BOOSTER_SQL_WRAPPER("io.github.luminion.sqlbooster.model.api.Wrapper"),
    SQL_BOOSTER_SQL_HELPER("io.github.luminion.sqlbooster.model.sql.helper.SqlHelper"),
    SQL_BOOSTER_BOOSTER_PAGE("io.github.luminion.sqlbooster.core.BoosterPage"),
    SQL_BOOSTER_BOOSTER_MP_MAPPER("io.github.luminion.sqlbooster.extension.mybatisplus.BoosterMpMapper"),
    SQL_BOOSTER_BOOSTER_MP_SERVICE_IMPL("io.github.luminion.sqlbooster.extension.mybatisplus.BoosterMpServiceImpl"),
    SQL_BOOSTER_BOOSTER_MP_SERVICE("io.github.luminion.sqlbooster.extension.mybatisplus.BoosterMpService"),

    PREFIX_JAKARTA_SERVLET_REQUEST("servlet.http.HttpServletRequest"),
    PREFIX_JAKARTA_SERVLET_RESPONSE("servlet.http.HttpServletResponse"),
    PREFIX_JAKARTA_VALIDATION_SIZE("validation.constraints.Size"),
    PREFIX_JAKARTA_VALIDATION_NOT_BLANK("validation.constraints.NotBlank"),
    PREFIX_JAKARTA_VALIDATION_NOT_NULL("validation.constraints.NotNull"),
    
    PREFIX_EXCEL_EXCEL_IGNORE_UNANNOTATED("annotation.ExcelIgnoreUnannotated"),
    PREFIX_EXCEL_EXCEL_PROPERTY("annotation.ExcelProperty"),
    PREFIX_EXCEL_LONGEST_MATCH_COLUMN_WIDTH_STYLE_STRATEGY("write.style.column.LongestMatchColumnWidthStyleStrategy"),
    
    ;
    @Getter
    private final String className;
    
}
