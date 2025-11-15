package com.code.deepreader.rpc.api.semantic.dto;

import java.io.Serializable;
import java.util.Map;

public class SemanticSnippetDTO implements Serializable {

    private String content;
    private Map<String, Object> metadata;

    public SemanticSnippetDTO() {
    }

    public SemanticSnippetDTO(String content, Map<String, Object> metadata) {
        this.content = content;
        this.metadata = metadata;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}

