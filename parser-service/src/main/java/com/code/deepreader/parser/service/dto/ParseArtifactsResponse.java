package com.code.deepreader.parser.service.dto;

import java.util.List;

public class ParseArtifactsResponse {

    private String parseJobId;
    private List<FileParseResult> artifacts;

    public ParseArtifactsResponse() {
    }

    public ParseArtifactsResponse(String parseJobId, List<FileParseResult> artifacts) {
        this.parseJobId = parseJobId;
        this.artifacts = artifacts;
    }

    public String getParseJobId() {
        return parseJobId;
    }

    public void setParseJobId(String parseJobId) {
        this.parseJobId = parseJobId;
    }

    public List<FileParseResult> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<FileParseResult> artifacts) {
        this.artifacts = artifacts;
    }
}

