# 源码深度阅读平台（Skeleton）

该仓库提供源码深度阅读助手的后端多模块骨架，覆盖摄取、解析、图谱、语义索引、AI 编排以及统一网关等核心能力，便于后续逐步填充业务逻辑与部署配置。

## 模块总览
- `common-infra`：公共配置、DTO、事件契约。
- `rpc-api`：Dubbo 接口与跨服务 DTO。
- `ingestion-service`：仓库摄取、调度、快照管理。
- `parser-service`：AST/IR 分析流水线。
- `graph-service`：Neo4j 知识图谱 API。
- `semantic-service`：Spring AI + Milvus 语义索引与记忆。
- `ai-orchestrator`：多模态上下文聚合、对话推理。
- `api-gateway`：统一入口、路由与安全。

## 快速开始
```bash
# 构建全部模块
mvn clean install

# 启动某服务
mvn -pl ingestion-service spring-boot:run
```

更多架构说明参见 `docs/architecture/system-architecture.md`，接口汇总见 `docs/development/api-overview.md`。
