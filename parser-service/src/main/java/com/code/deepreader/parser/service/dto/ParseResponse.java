package com.code.deepreader.parser.service.dto;

import com.code.deepreader.common.infra.model.ParseJobStatus;
import java.time.Instant;

public class ParseResponse {

    private String parseJobId;
    private Instant acceptedAt;
    private ParseJobStatus status;

    public ParseResponse(String parseJobId, Instant acceptedAt, ParseJobStatus status) {
        this.parseJobId = parseJobId;
        this.acceptedAt = acceptedAt;
        this.status = status;
    }

    public String getParseJobId() {
        return parseJobId;
    }

    public void setParseJobId(String parseJobId) {
        this.parseJobId = parseJobId;
    }

    public Instant getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Instant acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public ParseJobStatus getStatus() {
        return status;
    }

    public void setStatus(ParseJobStatus status) {
        this.status = status;
    }
}

