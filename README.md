# AutoJava — 自动化代码生成工具

AutoJava 是一个基于纯 Java 实现的后端代码生成器。只需连接 MySQL 数据库并填写少量配置，即可一键读取数据库表结构，自动生成一套完整的 Spring Boot + MyBatis 后端项目骨架，彻底告别重复的 CRUD 模板代码。

---

## 功能特性

- **自动读取数据库**：通过 JDBC 获取表结构、字段信息、注释及索引，无需手动描述表结构
- **全层级代码生成**：覆盖 PO、Query、Mapper 接口、Mapper XML、Service、ServiceImpl、Controller 所有层
- **智能查询扩展**：自动为字符串字段生成模糊查询，为日期字段生成范围查询（Start / End）
- **索引感知**：根据数据库索引自动生成对应的 `selectByXxx` / `updateByXxx` / `deleteByXxx` 方法
- **动态 SQL**：MyBatis XML 使用 `<if>` 标签按需拼接，支持批量插入、批量 `insertOrUpdate`
- **覆盖保护**：生成前自动检测目标目录是否已有文件，存在则警告并需用户手动确认后才继续
-  **高度可配置**：包名、路径、作者、命名后缀、忽略字段等均可在配置文件中灵活调整

---

## 生成文件一览

### 基础模板（仅首次生成一次）

| 文件 | 说明 |
|---|---|
| `BaseMapper.java` | 通用 Mapper 接口，定义增删改查等基础方法 |
| `BaseQuery.java` | 查询基类，内置分页参数 |
| `SimplePage.java` / `PageSize.java` | 分页工具类 |
| `PaginationResultVO.java` / `ResponseVO.java` | 统一分页与响应封装 |
| `ResponseCodeEnum.java` | 统一响应码枚举 |
| `BusinessException.java` | 业务异常类 |
| `BaseController.java` | Controller 基类 |
| `GlobalExceptionHandlerController.java` | 全局异常拦截器 |
| `DateUtils.java` / `DateTimePatternEnum.java` | 日期处理工具 |

### 按表生成（每张表独立生成一套）

| 文件 | 说明 |
|---|---|
| `XxxPO.java` | 实体类，字段与数据库列一一映射，自动添加日期序列化注解 |
| `XxxQuery.java` | 查询对象，包含原始字段、模糊查询字段及日期范围字段 |
| `XxxMapper.java` | Mapper 接口，包含通用方法及按索引生成的特定操作方法 |
| `XxxMapper.xml` | MyBatis XML，包含 resultMap、动态查询条件、批量操作等完整 SQL |
| `XxxService.java` | Service 接口 |
| `XxxServiceImpl.java` | Service 实现类，内置分页查询逻辑 |
| `XxxController.java` | RESTful Controller，包含分页加载、新增、批量操作等接口 |

---

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/JaaaLmg/Auto-Java.git
```

### 2. 修改配置文件

编辑 `autoJava/src/main/resources/application.properties`：

```properties
# 数据库连接
db.url=jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf-8
db.username=root
db.password=your_password

# 目标项目配置
project.homePath=D:/your-workspace/         # 工作空间根目录（末尾带 /）
project.name=your-project-name              # 目标子项目目录名
project.author=YourName                     # 生成代码中的作者署名

# 基础包名
package.base=com.example.yourproject
```

### 3. 运行生成器

直接运行 `RunApplication.java` 的 `main` 方法即可。

```
com.autojava.RunApplication
```

若目标目录下已有文件，控制台会输出覆盖警告：

```
========================================================
  ⚠  警告：以下目录下已存在文件，继续生成将会覆盖原有文件！
========================================================
  - D:/your-workspace/your-project/src/main/java/com/example/entity/po
  - D:/your-workspace/your-project/src/main/resources/com/example/mappers
--------------------------------------------------------
  请确认是否继续生成？输入 Y 继续，输入其他任意键退出：
  >
```

输入 `Y` 确认继续，输入其他任意内容则安全退出。

---

## 完整配置项说明

```properties
# ==================== 数据库 ====================
db.driver.name=com.mysql.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/your_db
db.username=root
db.password=123456

# ==================== 项目 ====================
project.homePath=D:/workspace/
project.name=your-project
project.author=YourName

# ==================== 表名处理 ====================
# 是否忽略表前缀（true 则去掉表名第一个下划线前的内容，如 tb_user -> User）
ignore.table.prefix=true

# ==================== 类名后缀 ====================
suffix.bean.query=Query
suffix.bean.query.fuzzy=Fuzzy
suffix.bean.query.time.start=Start
suffix.bean.query.time.end=End
suffix.mappers=Mapper
suffix.service=Service
suffix.controller=Controller

# ==================== 字段忽略（JsonIgnore）====================
# 需要添加 @JsonIgnore 注解的字段名（多个用英文逗号分隔）
ignore.bean.tojson.field=companyId,status
ignore.bean.tojson.annotation=@JsonIgnore
ignore.bean.tojson.import=com.fasterxml.jackson.annotation.JsonIgnore

# ==================== getter / setter / toString ====================
ignore.bean.getterSetter=false
ignore.bean.toString=false

# ==================== 日期注解 ====================
bean.date.format.annotation=@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
bean.date.format.import=com.fasterxml.jackson.annotation.JsonFormat
bean.date.unformat.annotation=@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
bean.date.unformat.import=org.springframework.format.annotation.DateTimeFormat

# ==================== 包名 ====================
package.base=com.example.yourproject
package.po=entity.po
package.query=entity.query
package.vo=entity.vo
package.enums=entity.enums
package.utils=utils
package.mappers=mappers
package.service=service
package.service.impl=service.impl
package.exception=exception
package.controller=controller
```

---

## 技术栈

| 依赖 | 版本 |
|---|---|
| Java | 8+ |
| MySQL Connector | 5.1.5 |
| Apache Commons Lang3 | 3.4 |
| Fastjson | 1.1.42 |
| SLF4J + Logback | 1.7.7 / 1.2.10 |

> 生成逻辑基于原生 Java IO（`BufferedWriter`）手动拼装，不依赖 Velocity / Freemarker 等模板引擎，零额外学习成本。

---

## 项目结构

```
autoJava/
├── src/main/java/com/autojava/
│   ├── RunApplication.java          # 程序入口
│   ├── bean/
│   │   ├── Constants.java           # 全局配置常量
│   │   ├── TableInfo.java           # 数据库表信息
│   │   └── FieldInfo.java           # 数据库字段信息
│   ├── builder/
│   │   ├── BuildTable.java          # 读取数据库表结构
│   │   ├── BuildTemplates.java      # 生成基础模板文件
│   │   ├── BuildPo.java             # 生成实体类
│   │   ├── BuildQuery.java          # 生成查询对象
│   │   ├── BuildMapper.java         # 生成 Mapper 接口
│   │   ├── BuildMapperXml.java      # 生成 Mapper XML
│   │   ├── BuildService.java        # 生成 Service 接口
│   │   ├── BuildServiceImpl.java    # 生成 Service 实现类
│   │   ├── BuildController.java     # 生成 Controller
│   │   └── BuildComment.java        # 生成代码注释
│   └── utils/
│       ├── FileCheckUtils.java      # 生成前文件冲突检测
│       ├── PropertiesUtils.java     # 配置文件读取工具
│       ├── StringUtils.java         # 字符串处理工具
│       ├── DateUtils.java           # 日期工具
│       └── JsonUtils.java           # JSON 工具
└── src/main/resources/
    ├── application.properties       # 核心配置文件
    └── template/                    # 基础模板文件源
```

---

## 生成流程

```
启动 RunApplication.main()
        │
        ├─ 1. 连接数据库，读取所有表结构、字段、索引
        │
        ├─ 2. 检测目标目录是否已有文件（FileCheckUtils）
        │       └─ 有文件 → 打印警告 → 等待用户输入 Y 确认
        │
        ├─ 3. 生成基础模板文件（BaseMapper、BaseQuery 等公共类）
        │
        └─ 4. 遍历每张表，依次生成：
                PO → Query → Mapper → MapperXml
                → Service → ServiceImpl → Controller
```
