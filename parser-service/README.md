# parser-service

- 负责解析摄取的快照（AST、依赖、指标），并暴露解析任务 API。
- 当前实现了内存任务表与 Spring Batch 骨架，后续可连接分布式存储。

## 自测
```bash
mvn -pl parser-service test
```
