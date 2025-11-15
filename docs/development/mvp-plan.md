# MVP 开发计划

| Sprint | 交付内容 | 说明 |
|--------|----------|------|
| Week 1-2 | 项目骨架、CI/CD、Git 摄取服务、元数据表 | 建立多模块结构，跑通手动全量扫描。 |
| Week 3-4 | Java 解析器、IR 存储、Neo4j 接入、基础指标 | 以试点仓库验证 AST/依赖能力，暴露 GraphQL。 |
| Week 5-6 | 语义索引流水线、Spring AI 集成、Hybrid Retrieval | 生成类/模块摘要，实现“Describe Module”API。 |
| Week 7 | Web Console V0、Chat UI、SSE 输出 | 支持拓扑树、热力图 placeholder。 |
| Week 8 | 变更影响 MVP、性能/观测加固、Demo 准备 | 引入 diff 分析、风险报告。 |

## 风险与缓解
- **模型成本**：提供本地/远程双通道配置，支持 Mock Provider。
- **图谱规模**：提前压测 Neo4j，必要时开启 Fabric/分片。
- **向量库**：本地 Milvus Standalone 验证，后续切集群模式。
- **团队协作**：输出 API/事件契约，保障并行开发。
