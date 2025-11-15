package com.code.deepreader.graph.service.controller;

import com.code.deepreader.graph.service.domain.CodeEntityNode;
import com.code.deepreader.graph.service.dto.CodeEntityUpsertRequest;
import com.code.deepreader.graph.service.repository.CodeEntityRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/graph/entities")
public class GraphMutationController {

    private final CodeEntityRepository repository;

    public GraphMutationController(CodeEntityRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Void> upsert(@Valid @RequestBody CodeEntityUpsertRequest request) {
        CodeEntityNode node = new CodeEntityNode(
                request.getId(),
                request.getName(),
                request.getType(),
                request.getProjectId(),
                request.getLastTouchedAt()
        );
        repository.save(node);
        return ResponseEntity.accepted().build();
    }
}

