package com.code.deepreader.ai.orchestrator.service.agent;

import com.code.deepreader.ai.orchestrator.dto.AgentMessage;
import com.code.deepreader.ai.orchestrator.dto.AgentRequest;
import com.code.deepreader.ai.orchestrator.dto.AgentResponse;
import com.code.deepreader.ai.orchestrator.memory.ConversationMemoryService;
import com.code.deepreader.ai.orchestrator.memory.MemoryMessage;
import com.code.deepreader.ai.orchestrator.memory.MemoryProperties;
import com.code.deepreader.ai.orchestrator.service.model.LlmChatClientManager;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

/**
 * Encapsulates agent-style orchestration that can leverage MCP tools.
 */
@Service
public class DeepReadAgentService {

    private final LlmChatClientManager chatClientManager;
    private final ConversationMemoryService memoryService;
    private final MemoryProperties memoryProperties;
    private final List<ToolCallback> toolCallbacks;

    public DeepReadAgentService(LlmChatClientManager chatClientManager,
                                ConversationMemoryService memoryService,
                                MemoryProperties memoryProperties,
                                List<ToolCallbackProvider> toolCallbackProviders) {
        this.chatClientManager = chatClientManager;
        this.memoryService = memoryService;
        this.memoryProperties = memoryProperties;
        this.toolCallbacks = toolCallbackProviders == null ? List.of()
                : toolCallbackProviders.stream()
                .flatMap(provider -> Arrays.stream(provider.getToolCallbacks()))
                .collect(Collectors.toList());
    }

    /**
     * Execute an agent task with optional MCP tools.
     */
    public AgentResponse runAgent(AgentRequest request) {
        String sessionId = resolveSessionId(request.getSessionId());
        var history = memoryService.loadRecent(sessionId, memoryProperties.getMaxMessages());
        var modelConfig = chatClientManager.getConfig(request.getModelName());

        ChatClient client = chatClientManager.resolveClient(modelConfig.getName());
        ChatClient.ChatClientRequestSpec spec = client.prompt()
                .system(buildSystemPrompt());
        String answer = spec.user(buildUserPrompt(request.getProjectId(), request.getQuestion(), history, toolCallbacks)).call().content();
        memoryService.append(sessionId, MemoryMessage.Role.USER, request.getQuestion());
        memoryService.append(sessionId, MemoryMessage.Role.ASSISTANT, answer);

        List<AgentMessage> messages = history.stream()
                .map(msg -> new AgentMessage(msg.role().name(), msg.content(), msg.timestamp()))
                .collect(Collectors.toList());
        messages.add(new AgentMessage(MemoryMessage.Role.USER.name(), request.getQuestion(), java.time.Instant.now()));
        messages.add(new AgentMessage(MemoryMessage.Role.ASSISTANT.name(), answer, java.time.Instant.now()));

        return new AgentResponse(sessionId, modelConfig.getName(), answer, messages);
    }

    private String buildSystemPrompt() {
        return "你是 Deep Read Agent，能够分析大型 Java 项目的结构。若工具可用，请善用 MCP 工具检索上下文。";
    }

    private String buildUserPrompt(String projectId,
                                   String question,
                                   List<MemoryMessage> history,
                                   List<ToolCallback> toolCallbacks) {
        StringBuilder builder = new StringBuilder();
        builder.append("项目 ID: ").append(projectId).append("\n");
        if (!history.isEmpty()) {
            builder.append("历史对话：\n");
            history.forEach(msg -> builder.append(msg.role()).append(": ").append(msg.content()).append("\n"));
        }
        if (!toolCallbacks.isEmpty()) {
            builder.append("MCP 工具目录：\n");
            toolCallbacks.forEach(callback -> builder.append("- ")
                    .append(callback.getToolDefinition().name())
                    .append(": ")
                    .append(callback.getToolDefinition().description())
                    .append("\n"));
        }
        builder.append("当前问题：").append(question);
        return builder.toString();
    }

    private String resolveSessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return sessionId;
    }
}

