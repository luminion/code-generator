# Architecture Map

## Stable entry points

- `GeneratorHelper`: create generator instances.
- `LambdaGenerator`: chain-style user entry.
- `builder/*Builder`: all user-facing configuration APIs.

These are the main places users should touch first.

## Configuration ownership

- `config/DataSourceConfig`: datasource, schema, table filters, naming converter, field type converter.
- `config/GlobalConfig`: global style, doc style, framework style, feature enable/disable switches.
- `config/TemplateConfig`: output directory, parent package, per-template file definitions.
- `config/EntityConfig`: entity-specific generation options.
- `config/MapperConfig`: mapper-specific generation options.
- `config/ServiceConfig`: service/serviceImpl inheritance options.
- `config/ControllerConfig`: controller API style, return wrapper, page wrapper.
- `config/CommandConfig`: create/update/delete method names and validation-related options.
- `config/QueryConfig`: query method names, extra query suffix fields, page parameter naming.
- `config/ExcelConfig`: excel API and excel import/export method naming.

## Internal generation flow

### `internal/schema`

- `JdbcTableFieldMapper`: convert JDBC columns to generator fields with clear ownership of naming, type conversion, and field classification.

### `internal/template`

- `TemplateFileResolver`: resolve template output package, output directory, class name, and final template file metadata for one table.

### `internal/render`

- `RenderContext`: one-table render context shared across all render contributors.
- `RenderDataCollector`: collect `@RenderField` values.
- `ImportPackageSupport`: centralize optional import registration and Java/framework package splitting.
- `TemplateGenerationPlanner`: decide extra query fields, per-table template generation policy, skip reasons, and no-primary-key generation hints.

## How to find code quickly

- Change default config values: go to `config/*Config`.
- Change chain API names or overloads: go to `builder/*Builder`.
- Change whether a file should be generated: go to `internal/render/TemplateGenerationPlanner`.
- Change template file naming/output rules: go to `internal/template/TemplateFileResolver`.
- Change final generated code: go to `src/main/resources/templates/*`.
- Change database metadata parsing: go to `internal/schema/JdbcTableFieldMapper` and `datasource/support/JdbcTableInfoProvider`.

## Current refactor rule

- Keep user-facing entry points stable.
- Move coordination logic inward first.
- Add compatibility aliases before removing old names.
- Prefer one clear ownership point for each kind of setting or generation decision.