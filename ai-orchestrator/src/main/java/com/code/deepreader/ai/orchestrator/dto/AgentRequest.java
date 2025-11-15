package com.code.deepreader.ai.orchestrator.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for agent-style interactions.
 */
public class AgentRequest {

    @NotBlank
    private String projectId;

    @NotBlank
    private String question;

    private String modelName;
    private String sessionId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

