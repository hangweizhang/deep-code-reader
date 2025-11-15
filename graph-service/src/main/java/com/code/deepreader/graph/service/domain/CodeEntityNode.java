package com.code.deepreader.graph.service.domain;

import java.time.Instant;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("CodeEntity")
public class CodeEntityNode {

    @Id
    private String id;

    @Property("name")
    private String name;

    @Property("type")
    private String type;

    @Property("projectId")
    private String projectId;

    @Property("lastTouchedAt")
    private Instant lastTouchedAt;

    public CodeEntityNode() {
    }

    public CodeEntityNode(String id, String name, String type, String projectId, Instant lastTouchedAt) {
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
