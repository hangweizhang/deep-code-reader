# semantic-service

- 负责 chunk 切片、向量写入、语义检索，并通过 Dubbo `SemanticSearchRpcService` 提供给编排层。
- 默认 HTTP 端口 8084，Dubbo 端口 20881（可与 Milvus、Redis 配合使用）。

## 环境变量
- `OPENAI_API_KEY`：Spring AI 调用所需 Key。

## 自测
```bash
mvn -pl semantic-service test
```
