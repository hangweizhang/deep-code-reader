package com.code.deepreader.semantic.service.controller;

import com.code.deepreader.semantic.service.dto.SemanticSearchRequest;
import com.code.deepreader.semantic.service.dto.SemanticSearchResponse;
import com.code.deepreader.semantic.service.dto.SemanticSearchResultItem;
import com.code.deepreader.semantic.service.service.SemanticIndexService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/semantic/search")
public class SemanticSearchController {

    private final SemanticIndexService semanticIndexService;

    public SemanticSearchController(SemanticIndexService semanticIndexService) {
        this.semanticIndexService = semanticIndexService;
    }

    @PostMapping
    public ResponseEntity<SemanticSearchResponse> search(@Valid @RequestBody SemanticSearchRequest request) {
        List<SemanticSearchResultItem> items = semanticIndexService.search(request.getQuery(), request.getLimit())
                .stream()
                .map(doc -> new SemanticSearchResultItem(doc.getContent(), doc.getMetadata()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new SemanticSearchResponse(items));
    }
}

