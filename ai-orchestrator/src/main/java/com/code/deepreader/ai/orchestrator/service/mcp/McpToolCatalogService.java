package com.code.deepreader.ai.orchestrator.service.mcp;

import com.code.deepreader.ai.orchestrator.dto.McpToolInfo;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

/**
 * Helper service that exposes the MCP tools registered with Spring AI.
 */
@Service
public class McpToolCatalogService {

    private final List<ToolCallbackProvider> providers;

    public McpToolCatalogService(List<ToolCallbackProvider> providers) {
        this.providers = providers == null ? List.of() : providers;
    }

    public List<McpToolInfo> listTools() {
        return providers.stream()
                .flatMap(provider -> Arrays.stream(provider.getToolCallbacks()))
                .map(this::mapTool)
                .collect(Collectors.toList());
    }

    private McpToolInfo mapTool(ToolCallback callback) {
        var definition = callback.getToolDefinition();
        return new McpToolInfo(definition.name(), definition.description(), definition.inputSchema());
    }
}

