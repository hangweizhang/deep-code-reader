package com.code.deepreader.semantic.service.dto;

import java.util.Map;

public class SemanticSearchResultItem {

    private String content;
    private Map<String, Object> metadata;

    public SemanticSearchResultItem(String content, Map<String, Object> metadata) {
        this.content = content;
        this.metadata = metadata;
    }

    public String getContent() {
        return content;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}

