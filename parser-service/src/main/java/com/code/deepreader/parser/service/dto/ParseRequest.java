package com.code.deepreader.parser.service.dto;

import jakarta.validation.constraints.NotBlank;

public class ParseRequest {

    @NotBlank(message = "projectId不能为空")
    private String projectId;

    @NotBlank(message = "repositoryUrl不能为空")
    private String repositoryUrl;

    @NotBlank(message = "commitId不能为空")
    private String commitId;

    @NotBlank(message = "workspace不能为空")
    private String workspace;

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

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }
}

