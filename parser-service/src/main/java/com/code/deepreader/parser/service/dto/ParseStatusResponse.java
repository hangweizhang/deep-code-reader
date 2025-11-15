package com.code.deepreader.parser.service.dto;

import com.code.deepreader.common.infra.model.ParseJobStatus;
import java.time.Instant;

public class ParseStatusResponse {

    private String parseJobId;
    private ParseJobStatus status;
    private Instant createdAt;

    public ParseStatusResponse(String parseJobId, ParseJobStatus status, Instant createdAt) {
        this.parseJobId = parseJobId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getParseJobId() {
        return parseJobId;
    }

    public ParseJobStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

