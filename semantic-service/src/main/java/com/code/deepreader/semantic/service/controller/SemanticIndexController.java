package com.code.deepreader.semantic.service.controller;

import com.code.deepreader.semantic.service.service.SemanticIndexService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/semantic/indexes")
public class SemanticIndexController {

    private final SemanticIndexService semanticIndexService;

    public SemanticIndexController(SemanticIndexService semanticIndexService) {
        this.semanticIndexService = semanticIndexService;
    }

    @PostMapping
    public ResponseEntity<Void> upsertChunks(@RequestBody String payload) {
        semanticIndexService.upsert(payload);
        return ResponseEntity.accepted().build();
    }
}
