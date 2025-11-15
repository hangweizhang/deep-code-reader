package com.code.deepreader.ai.orchestrator.dto;

import java.util.Map;

/**
 * DTO exposing MCP tool metadata.
 */
public class McpToolInfo {

    private String name;
    private String description;
    private String inputSchema;

    public McpToolInfo() {
    }

    public McpToolInfo(String name, String description, String inputSchema) {
        this.name = name;
        this.description = description;
        this.inputSchema = inputSchema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(String inputSchema) {
        this.inputSchema = inputSchema;
    }
}

