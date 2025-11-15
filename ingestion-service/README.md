# ingestion-service

- 负责 Git 仓库摄取、快照登记以及任务状态查询/更新。
- 对外提供 REST API（/api/v1/ingestions/**），可后续扩展为事件驱动调度。

## 本地运行
```bash
mvn -pl ingestion-service spring-boot:run
```

## 自测
```bash
mvn -pl ingestion-service test
```
