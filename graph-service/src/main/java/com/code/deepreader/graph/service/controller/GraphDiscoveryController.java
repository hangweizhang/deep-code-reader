package com.code.deepreader.graph.service.controller;

import com.code.deepreader.graph.service.domain.CodeEntityNode;
import com.code.deepreader.graph.service.service.GraphQueryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/graph")
public class GraphDiscoveryController {

    private final GraphQueryService graphQueryService;

    public GraphDiscoveryController(GraphQueryService graphQueryService) {
        this.graphQueryService = graphQueryService;
    }

    @GetMapping("/{projectId}/entities")
    public ResponseEntity<List<CodeEntityNode>> listEntities(@PathVariable String projectId) {
        return ResponseEntity.ok(graphQueryService.listByProject(projectId));
    }

    @GetMapping("/entity/{entityId}/dependencies")
    public ResponseEntity<List<CodeEntityNode>> dependencies(@PathVariable String entityId) {
        return ResponseEntity.ok(graphQueryService.queryDependencies(entityId));
    }
}
