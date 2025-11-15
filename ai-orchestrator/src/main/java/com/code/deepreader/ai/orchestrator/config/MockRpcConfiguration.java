package com.code.deepreader.ai.orchestrator.config;

import com.code.deepreader.rpc.api.graph.GraphQueryRpcService;
import com.code.deepreader.rpc.api.graph.dto.GraphEntityDTO;
import com.code.deepreader.rpc.api.semantic.SemanticSearchRpcService;
import com.code.deepreader.rpc.api.semantic.dto.SemanticSnippetDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides in-memory mock implementations for downstream RPC services so the orchestrator
 * can be started locally without Dubbo backends.
 */
@Configuration
@ConditionalOnProperty(prefix = "rpc.mock", name = "enabled", havingValue = "true")
public class MockRpcConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MockRpcConfiguration.class);
    private static final String DEFAULT_PROJECT = "demo-project";

    @Bean
    public GraphQueryRpcService mockGraphQueryRpcService() {
        log.info("[MockRPC] Using in-memory GraphQueryRpcService implementation");
        return new GraphQueryRpcService() {

            @Override
            public List<GraphEntityDTO> listEntities(String projectId, int limit) {
                String targetProject = projectId != null ? projectId : DEFAULT_PROJECT;
                return buildGraphEntities(targetProject).stream()
                        .limit(limit)
                        .collect(Collectors.toList());
            }

            @Override
            public List<GraphEntityDTO> listDependencies(String entityId) {
                return buildGraphEntities(DEFAULT_PROJECT).stream()
                        .filter(entity -> !entity.getId().equals(entityId))
                        .limit(3)
                        .collect(Collectors.toList());
            }
        };
    }

    @Bean
    public SemanticSearchRpcService mockSemanticSearchRpcService() {
        log.info("[MockRPC] Using in-memory SemanticSearchRpcService implementation");
        return (query, limit) -> buildSemanticSnippets(query).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<GraphEntityDTO> buildGraphEntities(String projectId) {
        List<GraphEntityDTO> entities = new ArrayList<>();
        Instant now = Instant.now();
        entities.add(new GraphEntityDTO("module-core", "core-service", "MODULE", projectId, now));
        entities.add(new GraphEntityDTO("module-api", "api-gateway", "MODULE", projectId, now.minusSeconds(600)));
        entities.add(new GraphEntityDTO("class-DeepReadOrchestrator", "DeepReadOrchestrator", "CLASS", projectId, now.minusSeconds(1_200)));
        entities.add(new GraphEntityDTO("class-NarrativeClient", "NarrativeClient", "CLASS", projectId, now.minusSeconds(2_400)));
        entities.add(new GraphEntityDTO("class-LlmChatClientManager", "LlmChatClientManager", "CLASS", projectId, now.minusSeconds(3_000)));
        return entities;
    }

    private List<SemanticSnippetDTO> buildSemanticSnippets(String query) {
        List<SemanticSnippetDTO> snippets = new ArrayList<>();
        snippets.add(snippet("""
                DeepReadOrchestrator 负责调度代码图谱、语义搜索以及 LLM 编排，
                当收到用户问题时优先拉取图谱实体，再结合语义片段生成回答。
                """, "DeepReadOrchestrator.java", query));
        snippets.add(snippet("""
                LlmChatClientManager 支持 OpenAI、GLM、DeepSeek 等多模型配置，
                通过名称解析动态选择目标模型，并对每个 provider 维持独立的 API Key。
                """, "LlmChatClientManager.java", query));
        snippets.add(snippet("""
                ConversationMemoryService 提供本地与 Redis 双实现，支持最近 N 轮对话记忆，
                便于 Agent 在多轮分析中保持上下文。
                """, "ConversationMemoryService.java", query));
        return snippets;
    }

    private SemanticSnippetDTO snippet(String content, String source, String query) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", source);
        metadata.put("confidence", 0.8 + ThreadLocalRandom.current().nextDouble(0.2));
        metadata.put("queryEcho", query);
        return new SemanticSnippetDTO(content.trim(), metadata);
    }
}

