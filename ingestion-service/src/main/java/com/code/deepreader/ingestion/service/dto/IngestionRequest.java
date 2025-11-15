package com.code.deepreader.ingestion.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class IngestionRequest {

    @NotBlank(message = "projectId不能为空")
    private String projectId;

    @NotBlank(message = "仓库地址不能为空")
    private String repositoryUrl;

    @Pattern(regexp = "[a-fA-F0-9]{7,40}", message = "commitId格式不正确")
    private String commitId;

    private boolean forceFullScan;

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

    public boolean isForceFullScan() {
        return forceFullScan;
    }

    public void setForceFullScan(boolean forceFullScan) {
        this.forceFullScan = forceFullScan;
    }
}
