package com.code.deepreader.semantic.service.rpc;

import com.code.deepreader.rpc.api.semantic.SemanticSearchRpcService;
import com.code.deepreader.rpc.api.semantic.dto.SemanticSnippetDTO;
import com.code.deepreader.semantic.service.service.SemanticIndexService;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.ai.document.Document;

@DubboService(version = "1.0.0")
public class SemanticSearchRpcServiceImpl implements SemanticSearchRpcService {

    private final SemanticIndexService semanticIndexService;

    public SemanticSearchRpcServiceImpl(SemanticIndexService semanticIndexService) {
        this.semanticIndexService = semanticIndexService;
    }

    @Override
    public List<SemanticSnippetDTO> search(String query, int limit) {
        List<Document> docs = semanticIndexService.search(query, limit);
        return docs.stream()
                .map(doc -> new SemanticSnippetDTO(doc.getContent(), doc.getMetadata()))
                .collect(Collectors.toList());
    }
}

