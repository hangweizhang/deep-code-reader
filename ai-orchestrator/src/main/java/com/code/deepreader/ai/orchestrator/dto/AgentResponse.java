package com.code.deepreader.ai.orchestrator.dto;

import java.util.List;

/**
 * Agent execution result containing answer and metadata.
 */
public class AgentResponse {

    private String sessionId;
    private String modelName;
    private String answer;
    private List<AgentMessage> memory;

    public AgentResponse() {
    }

    public AgentResponse(String sessionId, String modelName, String answer, List<AgentMessage> memory) {
        this.sessionId = sessionId;
        this.modelName = modelName;
        this.answer = answer;
        this.memory = memory;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<AgentMessage> getMemory() {
        return memory;
    }

    public void setMemory(List<AgentMessage> memory) {
        this.memory = memory;
    }
}

