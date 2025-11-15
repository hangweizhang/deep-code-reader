package com.code.deepreader.common.infra.model;

import java.time.Instant;

public class IngestionJob {

    private String jobId;
    private ProjectSnapshotRef snapshotRef;
    private IngestionJobStatus status;
    private Instant createdAt;

    public IngestionJob() {
    }

    public IngestionJob(String jobId, ProjectSnapshotRef snapshotRef, IngestionJobStatus status, Instant createdAt) {
        this.jobId = jobId;
        this.snapshotRef = snapshotRef;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public ProjectSnapshotRef getSnapshotRef() {
        return snapshotRef;
    }

    public void setSnapshotRef(ProjectSnapshotRef snapshotRef) {
        this.snapshotRef = snapshotRef;
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

