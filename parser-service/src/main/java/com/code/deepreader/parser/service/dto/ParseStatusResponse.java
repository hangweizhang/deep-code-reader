package com.code.deepreader.parser.service.dto;

import com.code.deepreader.common.infra.model.ParseJobStatus;
import com.code.deepreader.common.infra.model.ProjectSnapshotRef;
import java.time.Instant;

public class ParseStatusResponse {

    private String parseJobId;
    private ParseJobStatus status;
    private Instant createdAt;
    private Instant startedAt;
    private Instant finishedAt;
    private String message;
    private ParseSummary summary;
    private ProjectSnapshotRef snapshotRef;

    public ParseStatusResponse(String parseJobId, ParseJobStatus status, Instant createdAt,
                               Instant startedAt, Instant finishedAt, String message,
                               ParseSummary summary, ProjectSnapshotRef snapshotRef) {
        this.parseJobId = parseJobId;
        this.status = status;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.message = message;
        this.summary = summary;
        this.snapshotRef = snapshotRef;
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

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public String getMessage() {
        return message;
    }

    public ParseSummary getSummary() {
        return summary;
    }

    public ProjectSnapshotRef getSnapshotRef() {
        return snapshotRef;
    }
}

