# 核心服务接口总览

| 服务 | 关键接口 | 说明 |
|------|----------|------|
| `ingestion-service` | `POST /api/v1/ingestions` | 触发仓库摄取，返回 ingestionId |
| | `GET /api/v1/ingestions/{id}` | 查询任务状态（PENDING/RUNNING/...） |
| | `POST /api/v1/ingestions/{id}/status` | 内部回调更新状态 |
| | `GET /api/v1/ingestions/projects/{projectId}/snapshots` | 查看历史快照引用 |
| `parser-service` | `POST /api/v1/parses` | 提交解析任务，返回 parseJobId |
| | `GET /api/v1/parses/{jobId}` | 查询解析状态 |
| `graph-service` | `GET /api/v1/graph/{projectId}/entities` | 拉取项目实体列表（供可视化/AI使用） |
| | `GET /api/v1/graph/entity/{entityId}/dependencies` | 查询直接依赖 |
| | `POST /api/v1/graph/entities` | upsert 图谱节点（后续可扩展关系） |
| | **Dubbo** `GraphQueryRpcService.listEntities` | 给 AI 编排等内部调用方提供结构数据 |
| `semantic-service` | `POST /api/v1/semantic/indexes` | 写入 chunk/嵌入 |
| | `POST /api/v1/semantic/search` | 语义检索，支持 limit/metadata |
| | **Dubbo** `SemanticSearchRpcService.search` | 对内部调用开放语义检索结果 |
| `ai-orchestrator` | `GET /api/v1/deep-read/{projectId}/summary` | 快速概览项目结构 |
| | `POST /api/v1/deep-read/analysis` | 自定义问题，返回结构化总结/上下文 |
| | `POST /api/v1/deep-read/agent` | Agent 模式，结合 Memory + MCP Tool |
| | `GET /api/v1/deep-read/models` | 列出可用 LLM 模型，支持 GLM/DeepSeek/OpenAI 等 |
| | `GET /api/v1/deep-read/tools` | 列出当前注册的 MCP 工具 |
| `rpc-api` | - | 定义 Dubbo RPC 接口，供各模块复用 |
| `api-gateway` | 统一代理上述 REST；默认 8080 端口 | 可扩展鉴权、限流、SSE |

> 详细实现参考各服务 `controller` 目录；若需要新增事件/回调，请同步更新此文档。
