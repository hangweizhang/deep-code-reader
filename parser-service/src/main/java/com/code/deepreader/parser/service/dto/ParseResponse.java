package com.code.deepreader.parser.service.dto;

import java.time.Instant;

public class ParseResponse {

    private String parseJobId;
    private Instant acceptedAt;

    public ParseResponse(String parseJobId, Instant acceptedAt) {
        this.parseJobId = parseJobId;
        this.acceptedAt = acceptedAt;
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
}

