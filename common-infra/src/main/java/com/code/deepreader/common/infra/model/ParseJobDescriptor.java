package com.code.deepreader.common.infra.model;

import java.time.Instant;

public class ParseJobDescriptor {

    private String jobId;
    private ProjectSnapshotRef snapshotRef;
    private ParseJobStatus status;
    private Instant createdAt;

    public ParseJobDescriptor() {
    }

    public ParseJobDescriptor(String jobId, ProjectSnapshotRef snapshotRef, ParseJobStatus status, Instant createdAt) {
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

    public ParseJobStatus getStatus() {
        return status;
    }

    public void setStatus(ParseJobStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

