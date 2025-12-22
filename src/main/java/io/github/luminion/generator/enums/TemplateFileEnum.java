/*
 * Copyright (c) 2011-2025, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.luminion.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 输出文件类型
 *
 * @author hubin
 * @author luminion
 * @since 1.0.0
 */
@AllArgsConstructor
public enum TemplateFileEnum {
    CONTROLLER("controller"),
    SERVICE("service"),
    SERVICE_IMPL("serviceImpl"),
    MAPPER("mapper"),
    MAPPER_XML("mapperXml"),
    
    ENTITY("entity"),
    
    ENTITY_QUERY_DTO("queryDTO"),
    ENTITY_QUERY_VO("queryVO"),
    
    ENTITY_CREATE_DTO("createDTO"),
    ENTITY_UPDATE_DTO("updateDTO"),
    
    ENTITY_EXCEL_IMPORT_DTO("excelImportDTO"),
    ENTITY_EXCEL_EXPORT_DTO("excelExportDTO"),
    ;
    @Getter
    private final String key;
}
