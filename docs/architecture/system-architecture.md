# 源码深度阅读平台 - 系统架构草案

## 1. 整体概览
平台采用多模块 Spring Boot + Spring Cloud 架构，划分为摄取、解析、图谱、语义索引、AI 编排、统一网关六大后端服务，并配套前端控制台与文档体系。核心目标是让团队快速获取大型 Java 仓库的结构化认知、语义理解与变更风险提示。

```
外部仓库 -> 摄取服务 -> 解析服务 -> 图谱服务/语义服务 -> AI 编排 -> API 网关/前端
```

## 2. 模块职责

| 模块 | 端口 | 关键依赖 | 职责摘要 |
|------|------|----------|----------|
| `api-gateway` | 8080 | Spring Cloud Gateway | 对外统一入口，承担认证、路由、限流与观测。|
| `ingestion-service` | 8081 | Quartz, Kafka, JGit | 负责 Git 仓库同步、快照管理、任务调度。|
| `parser-service` | 8082 | Spring Batch, JDT, JavaParser | AST/CFG 构建、IR 生成、静态指标初步计算。|
| `graph-service` | 8083 | Spring Data Neo4j | 负责知识图谱入库、关系查询与API暴露。|
| `semantic-service` | 8084 | Spring AI, Milvus, Redis | Chunk 切片、嵌入写入、对话记忆存储策略。|
| `ai-orchestrator` | 8085 | Spring AI, OpenFeign | 调用图谱/语义/外部模型，生成结构化解读与剧本。|
| `common-infra` | - | Spring Boot, Jakarta Validation | 公共配置、DTO、事件定义。|

## 3. 数据流
1. **全量构建**：`ingestion-service` 拉取仓库 → 生成快照 → 派发解析任务。
2. **解析阶段**：`parser-service` 读取快照 → 产出 IR/指标 → 通过消息写入图谱与语义通道。
3. **图谱/语义**：`graph-service` 更新实体与关系；`semantic-service` 切片 + 嵌入 + 记忆；同时将核心摘要写回消息总线。
4. **AI 推理**：`ai-orchestrator` 聚合图谱子图 + 语义上下文 + Diff，调用 Spring AI 模型输出多视角洞察。
5. **消费层**：`api-gateway` 暴露 REST/SSE；前端或 CLI 触发深度阅读剧本、导出报告。

## 4. 关键技术点
- **Spring AI**：统一管理模型、多租户 Prompt、Function Calling、记忆策略。
- **Neo4j + Milvus**：结构化与向量检索结合，提供路径查询与语义召回。
- **Dubbo RPC**：图谱、语义等内部服务通过 Dubbo 暴露接口，AI 编排模块走 Dubbo 直连，提高 RPC 一致性与可观测性。
- **MCP Tooling + Agent**：AI 编排服务通过 Spring AI MCP 客户端注册外部工具，可由 Agent 模式调度执行，配合会话记忆（支持本地/Redis）完成多轮推理。
- **可扩展摄取**：通过 Quartz + Kafka 组合，支持定时全量 + Webhook 增量。
- **记忆存储抽象**：`ConversationMemoryRepository` 可切换到本地、Redis、向量库，实现短期与长期知识沉淀。
- **治理**：所有模块预留 Actuator 端点、Prometheus 指标、OpenTelemetry 日志接入点。

## 5. 后续扩展
- 引入 `web-console` React 客户端与文档自动化流水线。
- 扩展语言插件（Kotlin/Scala）和更多分析器（安全、性能）。
- 将内部服务以 MCP Server 暴露，方便与其他 AI 工具协作。
