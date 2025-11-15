package com.code.deepreader.ai.orchestrator.dto;

import com.code.deepreader.rpc.api.graph.dto.GraphEntityDTO;
import com.code.deepreader.rpc.api.semantic.dto.SemanticSnippetDTO;
import java.util.List;

public class DeepReadResponse {

    private String summary;
    private List<GraphEntityDTO> entityContext;
    private List<SemanticSnippetDTO> semanticSnippets;
    private String selectedModel;
    private String sessionId;

    public DeepReadResponse() {
    }

    public DeepReadResponse(String summary,
                            List<GraphEntityDTO> entityContext,
                            List<SemanticSnippetDTO> semanticSnippets,
                            String selectedModel,
                            String sessionId) {
        this.summary = summary;
        this.entityContext = entityContext;
        this.semanticSnippets = semanticSnippets;
        this.selectedModel = selectedModel;
        this.sessionId = sessionId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<GraphEntityDTO> getEntityContext() {
        return entityContext;
    }

    public void setEntityContext(List<GraphEntityDTO> entityContext) {
        this.entityContext = entityContext;
    }

    public List<SemanticSnippetDTO> getSemanticSnippets() {
        return semanticSnippets;
    }

    public void setSemanticSnippets(List<SemanticSnippetDTO> semanticSnippets) {
        this.semanticSnippets = semanticSnippets;
    }

    public String getSelectedModel() {
        return selectedModel;
    }

    public void setSelectedModel(String selectedModel) {
        this.selectedModel = selectedModel;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

