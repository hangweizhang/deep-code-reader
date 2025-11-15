package com.code.deepreader.semantic.service.dto;

import java.util.List;

public class SemanticSearchResponse {

    private List<SemanticSearchResultItem> results;

    public SemanticSearchResponse(List<SemanticSearchResultItem> results) {
        this.results = results;
    }

    public List<SemanticSearchResultItem> getResults() {
        return results;
    }
}

