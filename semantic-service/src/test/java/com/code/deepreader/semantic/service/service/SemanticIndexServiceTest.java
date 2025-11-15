package com.code.deepreader.semantic.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

class SemanticIndexServiceTest {

    private VectorStore vectorStore;
    private SemanticIndexService semanticIndexService;

    @BeforeEach
    void setUp() {
        vectorStore = Mockito.mock(VectorStore.class);
        semanticIndexService = new SemanticIndexService(vectorStore);
    }

    @Test
    void upsertShouldAddDocument() {
        semanticIndexService.upsert("content");
        verify(vectorStore).add(anyList());
    }

    @Test
    void searchShouldDelegateToVectorStore() {
        when(vectorStore.similaritySearch(Mockito.any(org.springframework.ai.vectorstore.SearchRequest.class)))
                .thenReturn(List.of(new Document("hello")));

        var result = semanticIndexService.search("query", 5);

        assertThat(result).hasSize(1);
    }
}

