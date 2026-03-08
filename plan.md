# 项目重构计划


## 配置类重构

### 结构
- 一个总配置 + 多个子配置
- 总配置持有所有子配置的引用, 子配置构造器必须传入总配置, 实例化时用setter将自身设置为总配置的属性
- 子配置类中, 持有总配置的引用, 便于设置关联选项

### 子配置列表
- 数据源配置(将数据源转化相关的配置都迁移到此处)
- 全局配置(管理其他配置都需要使用的公共项)
- 模板配置(管理模板文件/模板路径/包信息等)
- controller
- service
- mapper
- entity
- 查询功能配置
- 新增和修改功能配置
- excel功能配置

### 重构思路
- 按以上列表 整合配置项目, 各个属性迁移到对应的配置项中
- 若遇到字段冲突, 重命名字段, 并修改resource下模板文件的引用, 比如都有superclass, service配置中, 就改为serviceSuperClass和serviceImplClass
- 仔细核查配置项相关的,比如查询相关配置, 该从controller迁移到query的就迁移
- 对于某些冗长或不合适的属性, 该重命名就重命名
- 遇到需要关联设置的, 通过上文config的关联机制, 在这个setter中调用其他类需要设置的setter, 比如禁用查询, 那么vobyid, volist和vopage都应该被设置
- configresolver中的方法,迁移到对应类中,比如获取包和类名的, 迁移到template中


## builder重构

### 思路及方式
参考子配置列表, 调整每个配置的范围, 每个子配置对应一个builder
- 和现有builder大致类似, 不过需要随配置调整范围
- 如果config整合, 那么builder还是
- 对于所有模板, 提供统一的templateFilebuilder, 在模板配置对应builder中, 除了保留公用的,然后就是提供每个持有模板的templateFilebuilder
- builder不应暴露模板所有功能, 只暴露应该暴露的, 比如数据源url,username,pwd不允许二次配置
- 如果设置的值时boolean, 使用enable/disable作为前缀(视默认值而定), 移除入参
- feature功能配置(不同生成器不同配置项)

## 结构优化

### 问题点
目前的接口分散, 且分包/命名不好

### 优化思路
- 该删的删
- 该改名的改名
- 该调整包的调整包

# 总目标
以上为大致思路,重构后需要满足以下几点:
- TemplateRender接口只有一个renderdata方法
- 删除TemplateModelRender, 模板文件由template统一管理
- 每个config类setter就包含了完整需要处理的逻辑
- 后续维护迭代很好扩展/修改

