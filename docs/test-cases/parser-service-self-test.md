# Parser-Service 自测手册

## 1. 目的
验证解析服务在无真实仓库的情况下，也能完成任务受理、异步执行、状态查询与结果摘要输出。

## 2. 前置条件
- JDK 21+
- 已执行 `mvn clean install`
- 可选：准备一个包含若干 `.java` 文件的本地目录作为 `workspace`

## 3. 启动服务
```bash
mvn -pl parser-service spring-boot:run \
  -Dspring-boot.run.arguments="--parser.execution.async=false"
```
> 关闭异步模式便于观察日志；实际环境可保持默认异步。

## 4. 触发解析
```bash
curl -X POST http://localhost:8082/api/v1/parses \
  -H "Content-Type: application/json" \
  -d '{
        "projectId": "demo-project",
        "repositoryUrl": "https://example.com/repo.git",
        "commitId": "abc1234",
        "workspace": "/path/to/local/workspace"
      }'
```
- **预期返回**：`202 Accepted`，body 包含 `parseJobId`、`acceptedAt`、`status`。
- 若 `workspace` 不存在，服务会自动创建临时目录并继续。

## 5. 查询状态
```bash
curl http://localhost:8082/api/v1/parses/{parseJobId}
```
检查字段：
- `status`: `RUNNING` / `SUCCEEDED` / `FAILED`
- `summary`: `totalFiles`、`javaFiles`、`totalBytes`、`totalLines`
- `message`: 文本说明（成功则“解析完成”，若有解析告警会附带 warnings）
- `startedAt`、`finishedAt`: 任务时间戳

## 6. 获取解析产物（真实 AST 统计）
```bash
curl http://localhost:8082/api/v1/parses/{parseJobId}/artifacts
```
- 返回 `relativePath`、`packageName`、`typeNames`、`imports`、`methodCount`、`linesOfCode`、`sha256`、`lastModifiedAt` 等字段。
- 若解析失败或文件无法解析，列表为空，同时在 `status` 查询中可看到 warning 信息。

## 7. 典型场景
| 场景 | 操作 | 预期 |
| --- | --- | --- |
| 正常解析 | 指向包含 Java 文件的目录 | `summary` 数量与文件真实数量一致，`artifacts` 返回文件明细，状态 `SUCCEEDED` |
| 空目录 | 提供空 workspace | `summary.totalFiles=0`，状态 `SUCCEEDED` |
| 非法路径 | `workspace` 含非法字符 | 状态 `FAILED`，`message` 包含 “非法 workspace” |

## 8. 清理
- `CTRL+C` 退出 Spring Boot。
- 删除测试过程中生成的临时目录（若日志提示路径）。

> 更多扩展：可在 Postman/Rest Client 中保存上述请求，并结合日志确认状态流转。

