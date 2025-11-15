package com.code.deepreader.ai.orchestrator.dto;

import jakarta.validation.constraints.NotBlank;

public class DeepReadRequest {

    @NotBlank(message = "projectId不能为空")
    private String projectId;

    private String question = "请给出项目结构与最新变更的总结";
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

