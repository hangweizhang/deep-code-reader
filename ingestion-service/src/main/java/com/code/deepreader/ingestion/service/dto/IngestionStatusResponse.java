package com.code.deepreader.ingestion.service.dto;

import com.code.deepreader.common.infra.model.IngestionJobStatus;
import java.time.Instant;

public class IngestionStatusResponse {

    private String ingestionId;
    private IngestionJobStatus status;
    private Instant createdAt;

    public IngestionStatusResponse() {
    }

    public IngestionStatusResponse(String ingestionId, IngestionJobStatus status, Instant createdAt) {
        this.ingestionId = ingestionId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getIngestionId() {
        return ingestionId;
    }

    public void setIngestionId(String ingestionId) {
        this.ingestionId = ingestionId;
    }

    public IngestionJobStatus getStatus() {
        return status;
    }

    public void setStatus(IngestionJobStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

