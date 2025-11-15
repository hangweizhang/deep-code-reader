package com.code.deepreader.ai.orchestrator.service;

import com.code.deepreader.ai.orchestrator.config.LlmModelProperties.ModelConfig;
import com.code.deepreader.ai.orchestrator.dto.DeepReadRequest;
import com.code.deepreader.ai.orchestrator.dto.DeepReadResponse;
import com.code.deepreader.ai.orchestrator.memory.ConversationMemoryService;
import com.code.deepreader.ai.orchestrator.memory.MemoryMessage;
import com.code.deepreader.ai.orchestrator.memory.MemoryProperties;
import com.code.deepreader.ai.orchestrator.service.model.LlmChatClientManager;
import com.code.deepreader.ai.orchestrator.service.narrative.NarrativeClient;
import com.code.deepreader.rpc.api.graph.GraphQueryRpcService;
import com.code.deepreader.rpc.api.graph.dto.GraphEntityDTO;
import com.code.deepreader.rpc.api.semantic.SemanticSearchRpcService;
import com.code.deepreader.rpc.api.semantic.dto.SemanticSnippetDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 聚合图谱/语义上下文，支持多模型推理与会话记忆。
 */
@Service
public class DeepReadOrchestrator {

    private final GraphQueryRpcService graphQueryRpcService;
    private final SemanticSearchRpcService semanticSearchRpcService;
    private final NarrativeClient narrativeClient;
    private final LlmChatClientManager chatClientManager;
    private final ConversationMemoryService conversationMemoryService;
    private final MemoryProperties memoryProperties;
    private final int entityLimit;
    private final int semanticLimit;

    public DeepReadOrchestrator(GraphQueryRpcService graphQueryRpcService,
                                SemanticSearchRpcService semanticSearchRpcService,
                                NarrativeClient narrativeClient,
                                LlmChatClientManager chatClientManager,
                                ConversationMemoryService conversationMemoryService,
                                MemoryProperties memoryProperties,
                                @Value("${orchestrator.entity-limit:10}") int entityLimit,
                                @Value("${orchestrator.semantic-limit:5}") int semanticLimit) {
        this.graphQueryRpcService = graphQueryRpcService;
        this.semanticSearchRpcService = semanticSearchRpcService;
        this.narrativeClient = narrativeClient;
        this.chatClientManager = chatClientManager;
        this.conversationMemoryService = conversationMemoryService;
        this.memoryProperties = memoryProperties;
        this.entityLimit = entityLimit;
        this.semanticLimit = semanticLimit;
    }

    public DeepReadResponse summarizeModule(String projectId) {
        DeepReadRequest request = new DeepReadRequest();
        request.setProjectId(projectId);
        request.setQuestion("请用中文总结该项目的模块结构与协作关系");
        return analyze(request);
    }

    /**
     * 根据请求构建提示词并返回结构化总结。
     */
    public DeepReadResponse analyze(DeepReadRequest request) {
        String sessionId = resolveSessionId(request.getSessionId());
        List<MemoryMessage> history = conversationMemoryService.loadRecent(sessionId, memoryProperties.getMaxMessages());

        List<GraphEntityDTO> entityContext = graphQueryRpcService.listEntities(request.getProjectId(), entityLimit)
                .stream()
                .collect(Collectors.toList());

        List<SemanticSnippetDTO> snippets = semanticSearchRpcService.search(request.getQuestion(), semanticLimit);
        snippets = snippets != null ? snippets : List.of();
        if (CollectionUtils.isEmpty(snippets)) {
            snippets = List.of();
        }

        ModelConfig resolvedModel = chatClientManager.getConfig(request.getModelName());
        String prompt = buildPrompt(request.getQuestion(), entityContext, snippets, history);
        String summary = narrativeClient.generate(prompt, resolvedModel.getName());

        conversationMemoryService.append(sessionId, MemoryMessage.Role.USER, request.getQuestion());
        conversationMemoryService.append(sessionId, MemoryMessage.Role.ASSISTANT, summary);

        return new DeepReadResponse(summary, entityContext, snippets, resolvedModel.getName(), sessionId);
    }

    private String buildPrompt(String question,
                               List<GraphEntityDTO> entityContext,
                               List<SemanticSnippetDTO> snippets,
                               List<MemoryMessage> history) {
        StringBuilder builder = new StringBuilder();
        builder.append("你是一名资深架构师，请根据以下上下文回答问题。\n\n");
        builder.append("【实体结构】\n");
        for (GraphEntityDTO entity : entityContext) {
            builder.append("- ").append(entity.getType()).append(" ")
                    .append(entity.getName()).append(" (lastTouchedAt=")
                    .append(entity.getLastTouchedAt()).append(")\n");
        }
        builder.append("\n【语义片段】\n");
        for (SemanticSnippetDTO snippet : snippets) {
            builder.append("- ").append(snippet.getContent()).append("\n");
        }
        if (!history.isEmpty()) {
            builder.append("\n【会话记忆】\n");
            history.forEach(msg -> builder.append("- ")
                    .append(msg.role()).append(": ").append(msg.content()).append("\n"));
        }
        builder.append("\n问题: ").append(question);
        builder.append("\n请输出：1）整体概况 2）潜在风险与建议。");
        return builder.toString();
    }

    private String resolveSessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return sessionId;
    }
}
