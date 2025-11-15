package com.code.deepreader.rpc.api.graph.dto;

import java.io.Serializable;
import java.time.Instant;

public class GraphEntityDTO implements Serializable {

    private String id;
    private String name;
    private String type;
    private String projectId;
    private Instant lastTouchedAt;

    public GraphEntityDTO() {
    }

    public GraphEntityDTO(String id, String name, String type, String projectId, Instant lastTouchedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.projectId = projectId;
        this.lastTouchedAt = lastTouchedAt;
    }

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

