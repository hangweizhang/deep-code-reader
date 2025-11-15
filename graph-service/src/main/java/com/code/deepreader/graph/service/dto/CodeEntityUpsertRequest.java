package com.code.deepreader.graph.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class CodeEntityUpsertRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @NotBlank
    private String projectId;

    @NotNull
    private Instant lastTouchedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Instant getLastTouchedAt() {
        return lastTouchedAt;
    }

    public void setLastTouchedAt(Instant lastTouchedAt) {
        this.lastTouchedAt = lastTouchedAt;
    }
}

