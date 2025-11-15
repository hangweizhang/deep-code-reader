# parser-service

- 负责解析摄取的快照（AST、依赖、指标），并暴露解析任务 API。
- 当前实现了内存任务表、可配置的同步/异步执行，以及真实解析器（基于 JavaParser）产出的文件级摘要。
- `/api/v1/parses/{jobId}` 可查看任务状态与总体指标，`/api/v1/parses/{jobId}/artifacts` 可获取每个 Java 文件的包、类型、方法、导入、LOC、SHA256 等信息。
- 配置项 `parser.execution.async`、`parser.execution.threads` 控制执行模式，默认为异步 4 线程。

## 自测
```bash
mvn -pl parser-service test
```
或参考 `docs/test-cases/parser-service-self-test.md` 进行端到端验证。
