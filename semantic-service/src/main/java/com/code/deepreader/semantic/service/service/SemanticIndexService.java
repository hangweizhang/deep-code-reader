package com.code.deepreader.semantic.service.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class SemanticIndexService {

    private static final Logger log = LoggerFactory.getLogger(SemanticIndexService.class);

    private final VectorStore vectorStore;

    public SemanticIndexService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void upsert(String rawContent) {
        Document doc = new Document(rawContent);
        vectorStore.add(List.of(doc));
        log.info("[Semantic] 已接收新的 chunk, 长度={}", rawContent.length());
    }

    public List<Document> search(String query, int limit) {
        SearchRequest request = SearchRequest.query(query)
                .withTopK(limit);
        return vectorStore.similaritySearch(request);
    }
}
