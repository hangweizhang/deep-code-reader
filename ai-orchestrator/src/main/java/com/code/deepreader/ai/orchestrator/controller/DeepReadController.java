package com.code.deepreader.ai.orchestrator.controller;

import com.code.deepreader.ai.orchestrator.dto.AgentRequest;
import com.code.deepreader.ai.orchestrator.dto.AgentResponse;
import com.code.deepreader.ai.orchestrator.dto.DeepReadRequest;
import com.code.deepreader.ai.orchestrator.dto.DeepReadResponse;
import com.code.deepreader.ai.orchestrator.dto.McpToolInfo;
import com.code.deepreader.ai.orchestrator.dto.ModelInfoResponse;
import com.code.deepreader.ai.orchestrator.service.DeepReadOrchestrator;
import com.code.deepreader.ai.orchestrator.service.agent.DeepReadAgentService;
import com.code.deepreader.ai.orchestrator.service.mcp.McpToolCatalogService;
import com.code.deepreader.ai.orchestrator.service.model.LlmChatClientManager;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deep-read")
public class DeepReadController {

    private final DeepReadOrchestrator deepReadOrchestrator;
    private final LlmChatClientManager llmChatClientManager;
    private final DeepReadAgentService deepReadAgentService;
    private final McpToolCatalogService mcpToolCatalogService;

    public DeepReadController(DeepReadOrchestrator deepReadOrchestrator,
                              LlmChatClientManager llmChatClientManager,
                              DeepReadAgentService deepReadAgentService,
                              McpToolCatalogService mcpToolCatalogService) {
        this.deepReadOrchestrator = deepReadOrchestrator;
        this.llmChatClientManager = llmChatClientManager;
        this.deepReadAgentService = deepReadAgentService;
        this.mcpToolCatalogService = mcpToolCatalogService;
    }

    @GetMapping("/{projectId}/summary")
    public ResponseEntity<DeepReadResponse> summarize(@PathVariable String projectId,
                                                      @RequestParam(value = "modelName", required = false) String modelName,
                                                      @RequestParam(value = "sessionId", required = false) String sessionId) {
        if (modelName != null) {
            DeepReadRequest request = new DeepReadRequest();
            request.setProjectId(projectId);
            request.setModelName(modelName);
            request.setQuestion("请用中文总结该项目的模块结构与协作关系");
            request.setSessionId(sessionId);
            return ResponseEntity.ok(deepReadOrchestrator.analyze(request));
        }
        DeepReadRequest request = new DeepReadRequest();
        request.setProjectId(projectId);
        request.setSessionId(sessionId);
        request.setQuestion("请用中文总结该项目的模块结构与协作关系");
        return ResponseEntity.ok(deepReadOrchestrator.analyze(request));
    }

    @PostMapping("/analysis")
    public ResponseEntity<DeepReadResponse> analyze(@Valid @RequestBody DeepReadRequest request) {
        return ResponseEntity.ok(deepReadOrchestrator.analyze(request));
    }

    @GetMapping("/models")
    public ResponseEntity<ModelInfoResponse[]> listModels() {
        ModelInfoResponse[] responses = llmChatClientManager.listModels().stream()
                .map(desc -> new ModelInfoResponse(
                        desc.name(),
                        desc.provider(),
                        desc.model(),
                        desc.description(),
                        desc.isDefault()
                ))
                .toArray(ModelInfoResponse[]::new);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/agent")
    public ResponseEntity<AgentResponse> runAgent(@Valid @RequestBody AgentRequest request) {
        return ResponseEntity.ok(deepReadAgentService.runAgent(request));
    }

    @GetMapping("/tools")
    public ResponseEntity<McpToolInfo[]> listTools() {
        var tools = mcpToolCatalogService.listTools().toArray(new McpToolInfo[0]);
        return ResponseEntity.ok(tools);
    }
}
