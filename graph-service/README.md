# graph-service

- 提供 Neo4j 图谱读写 REST API，并通过 Dubbo `GraphQueryRpcService` 向内部调用方暴露结构数据。
- 默认端口 8083，Dubbo 端口 20880。

## 本地运行
```bash
export SPRING_NEO4J_URI=bolt://localhost:7687
mvn -pl graph-service spring-boot:run
```

## 自测
```bash
mvn -pl graph-service test
```
