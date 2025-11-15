package com.code.deepreader.ai.orchestrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.code.deepreader.ai.orchestrator.config.LlmModelProperties.ModelConfig;
import com.code.deepreader.ai.orchestrator.dto.DeepReadRequest;
import com.code.deepreader.ai.orchestrator.memory.ConversationMemoryService;
import com.code.deepreader.ai.orchestrator.memory.MemoryMessage;
import com.code.deepreader.ai.orchestrator.memory.MemoryProperties;
import com.code.deepreader.ai.orchestrator.service.model.LlmChatClientManager;
import com.code.deepreader.ai.orchestrator.service.narrative.NarrativeClient;
import com.code.deepreader.rpc.api.graph.GraphQueryRpcService;
import com.code.deepreader.rpc.api.graph.dto.GraphEntityDTO;
import com.code.deepreader.rpc.api.semantic.SemanticSearchRpcService;
import com.code.deepreader.rpc.api.semantic.dto.SemanticSnippetDTO;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DeepReadOrchestratorTest {

    private GraphQueryRpcService graphQueryRpcService;
    private SemanticSearchRpcService semanticSearchRpcService;
    private NarrativeClient narrativeClient;
    private LlmChatClientManager chatClientManager;
    private ConversationMemoryService memoryService;
    private MemoryProperties memoryProperties;
    private DeepReadOrchestrator orchestrator;
    private ModelConfig modelConfig;

    @BeforeEach
    void setUp() {
        graphQueryRpcService = Mockito.mock(GraphQueryRpcService.class);
        semanticSearchRpcService = Mockito.mock(SemanticSearchRpcService.class);
        narrativeClient = Mockito.mock(NarrativeClient.class);
        chatClientManager = Mockito.mock(LlmChatClientManager.class);
        memoryService = Mockito.mock(ConversationMemoryService.class);
        memoryProperties = new MemoryProperties();
        modelConfig = new ModelConfig();
        modelConfig.setName("glm");
        modelConfig.setModel("glm-4");
        modelConfig.setProvider("glm");
        when(chatClientManager.getConfig(any())).thenReturn(modelConfig);
        when(memoryService.loadRecent(any(), anyInt())).thenReturn(List.of());

        orchestrator = new DeepReadOrchestrator(
                graphQueryRpcService,
                semanticSearchRpcService,
                narrativeClient,
                chatClientManager,
                memoryService,
                memoryProperties,
                5,
                3
        );
    }

    @Test
    void analyzeShouldCombineGraphSemanticAndNarrative() {
        GraphEntityDTO entity = new GraphEntityDTO();
        entity.setId("cls1");
        entity.setName("PaymentService");
        entity.setType("CLASS");
        entity.setProjectId("proj");
        entity.setLastTouchedAt(Instant.now());
        when(graphQueryRpcService.listEntities("proj", 5)).thenReturn(List.of(entity));

        SemanticSnippetDTO snippet = new SemanticSnippetDTO("code snippet", null);
        when(semanticSearchRpcService.search(any(), anyInt())).thenReturn(List.of(snippet));
        when(narrativeClient.generate(any(), any())).thenReturn("analysis");

        DeepReadRequest request = new DeepReadRequest();
        request.setProjectId("proj");
        request.setQuestion("what changed?");

        var result = orchestrator.analyze(request);

        assertThat(result.getSummary()).isEqualTo("analysis");
        assertThat(result.getEntityContext()).hasSize(1);
        assertThat(result.getSemanticSnippets()).hasSize(1);
        assertThat(result.getSelectedModel()).isEqualTo("glm");
        verify(memoryService, times(1)).append(any(), Mockito.eq(MemoryMessage.Role.USER), any());
        verify(memoryService, times(1)).append(any(), Mockito.eq(MemoryMessage.Role.ASSISTANT), any());
    }
}
