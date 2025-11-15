package com.code.deepreader.ingestion.service.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.code.deepreader.ingestion.service.dto.IngestionRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RepositoryIngestionServiceTest {

    private RepositoryIngestionService ingestionService;

    @BeforeEach
    void setUp() {
        ingestionService = new RepositoryIngestionService();
    }

    @Test
    void scheduleIngestionShouldPersistJob() {
        IngestionRequest request = new IngestionRequest();
        request.setProjectId("proj-1");
        request.setRepositoryUrl("https://example.com/repo.git");
        request.setCommitId("abcdef1234567");

        var response = ingestionService.scheduleIngestion(request);

        assertThat(response.getIngestionId()).isNotBlank();
        assertThat(ingestionService.findJob(response.getIngestionId())).isPresent();

        List<?> snapshots = ingestionService.listSnapshots("proj-1");
        assertThat(snapshots).hasSize(1);
    }
}

