package com.code.deepreader.parser.service.dto;

import java.time.Instant;
import java.util.List;

public class FileParseResult {

    private String relativePath;
    private String packageName;
    private List<String> typeNames;
    private List<String> imports;
    private long methodCount;
    private long linesOfCode;
    private String sha256;
    private Instant lastModifiedAt;

    public FileParseResult() {
    }

    public FileParseResult(String relativePath,
                           String packageName,
                           List<String> typeNames,
                           List<String> imports,
                           long methodCount,
                           long linesOfCode,
                           String sha256,
                           Instant lastModifiedAt) {
        this.relativePath = relativePath;
        this.packageName = packageName;
        this.typeNames = typeNames;
        this.imports = imports;
        this.methodCount = methodCount;
        this.linesOfCode = linesOfCode;
        this.sha256 = sha256;
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(List<String> typeNames) {
        this.typeNames = typeNames;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public long getMethodCount() {
        return methodCount;
    }

    public void setMethodCount(long methodCount) {
        this.methodCount = methodCount;
    }

    public long getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(long linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}

