# ai-orchestrator

- 聚合图谱/语义上下文并调用 Spring AI 生成深度阅读总结。
- 通过 Dubbo 引用 `GraphQueryRpcService`、`SemanticSearchRpcService`，对外提供 `/api/v1/deep-read/**` REST API。

## 配置
- `rpc.graph-service.url`、`rpc.semantic-service.url`：Dubbo 直连地址，默认指向本机 20880/20881。
- `orchestrator.memory.type`：`local`（默认）或 `redis`，可配置 `max-messages`、`redis.key-prefix`、`redis.ttl`。
- `orchestrator.llm.models`：可配置多个模型（OpenAI 兼容、GLM、DeepSeek 等），字段包含 `name/provider/base-url/api-key/model/description`，`default-model` 指定默认模型。
- `spring.ai.mcp.client.*`：按照官方属性配置 SSE/STDIO MCP 连接，自动注册为 Spring AI Tool。
- `OPENAI_API_KEY`、`GLM_API_KEY`、`DEEPSEEK_API_KEY` 等：分别用于不同模型的凭证。

## REST 能力
- `GET /api/v1/deep-read/{projectId}/summary`：支持 `modelName`、`sessionId` 参数。
- `POST /api/v1/deep-read/analysis`：请求体可指定 `modelName`、`sessionId`。
- `POST /api/v1/deep-read/agent`：基于 Agent + MCP 工具调度。
- `GET /api/v1/deep-read/models`：列出可用 LLM。
- `GET /api/v1/deep-read/tools`：列出已注册的 MCP 工具。

## 自测
```bash
mvn -pl ai-orchestrator test
```
