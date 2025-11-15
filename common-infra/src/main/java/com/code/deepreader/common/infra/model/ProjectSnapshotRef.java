package com.code.deepreader.common.infra.model;

import java.time.Instant;
import java.util.Objects;

/**
 * 描述一次代码快照的核心元数据，供摄取、解析、图谱服务共享。
 */
public class ProjectSnapshotRef {

    private String projectId;
    private String repositoryUrl;
    private String commitId;
    private Instant capturedAt;

    public ProjectSnapshotRef() {
    }

    public ProjectSnapshotRef(String projectId, String repositoryUrl, String commitId, Instant capturedAt) {
        this.projectId = projectId;
        this.repositoryUrl = repositoryUrl;
        this.commitId = commitId;
        this.capturedAt = capturedAt;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public Instant getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(Instant capturedAt) {
        this.capturedAt = capturedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectSnapshotRef that = (ProjectSnapshotRef) o;
        return Objects.equals(projectId, that.projectId)
                && Objects.equals(repositoryUrl, that.repositoryUrl)
                && Objects.equals(commitId, that.commitId)
                && Objects.equals(capturedAt, that.capturedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, repositoryUrl, commitId, capturedAt);
    }

    @Override
    public String toString() {
        return "ProjectSnapshotRef{" +
                "projectId='" + projectId + '\'' +
                ", repositoryUrl='" + repositoryUrl + '\'' +
                ", commitId='" + commitId + '\'' +
                ", capturedAt=" + capturedAt +
                '}';
    }
}
