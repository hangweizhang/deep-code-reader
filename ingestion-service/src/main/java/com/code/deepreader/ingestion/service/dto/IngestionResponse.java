package com.code.deepreader.ingestion.service.dto;

import java.time.Instant;

public class IngestionResponse {

    private String ingestionId;
    private Instant acceptedAt;

    public IngestionResponse(String ingestionId, Instant acceptedAt) {
        this.ingestionId = ingestionId;
        this.acceptedAt = acceptedAt;
    }

    public String getIngestionId() {
        return ingestionId;
    }

    public void setIngestionId(String ingestionId) {
        this.ingestionId = ingestionId;
    }

    public Instant getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Instant acceptedAt) {
        this.acceptedAt = acceptedAt;
    }
}
