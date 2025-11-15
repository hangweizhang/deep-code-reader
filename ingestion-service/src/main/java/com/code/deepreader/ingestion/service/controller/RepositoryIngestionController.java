package com.code.deepreader.ingestion.service.controller;

import com.code.deepreader.common.infra.model.ProjectSnapshotRef;
import com.code.deepreader.ingestion.service.dto.IngestionRequest;
import com.code.deepreader.ingestion.service.dto.IngestionResponse;
import com.code.deepreader.ingestion.service.dto.IngestionStatusResponse;
import com.code.deepreader.ingestion.service.dto.UpdateIngestionStatusRequest;
import com.code.deepreader.ingestion.service.service.RepositoryIngestionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingestions")
public class RepositoryIngestionController {

    private final RepositoryIngestionService ingestionService;

    public RepositoryIngestionController(RepositoryIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    public ResponseEntity<IngestionResponse> triggerIngestion(@Valid @RequestBody IngestionRequest request) {
        return ResponseEntity.accepted().body(ingestionService.scheduleIngestion(request));
    }

    @GetMapping("/{ingestionId}")
    public ResponseEntity<IngestionStatusResponse> getStatus(@PathVariable String ingestionId) {
        return ingestionService.findJob(ingestionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/projects/{projectId}/snapshots")
    public ResponseEntity<List<ProjectSnapshotRef>> snapshots(@PathVariable String projectId) {
        return ResponseEntity.ok(ingestionService.listSnapshots(projectId));
    }

    @PostMapping("/{ingestionId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable String ingestionId,
                                             @Valid @RequestBody UpdateIngestionStatusRequest request) {
        ingestionService.updateJobStatus(ingestionId, request.getStatus());
        return ResponseEntity.accepted().build();
    }
}
