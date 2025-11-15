package com.code.deepreader.parser.service.dto;

/**
 * Lightweight metrics produced by the parse pipeline so that callers
 * can quickly了解本次解析产出的规模。
 */
public class ParseSummary {

    private long totalFiles;
    private long javaFiles;
    private long totalBytes;
    private long totalLines;

    public ParseSummary() {
    }

    public ParseSummary(long totalFiles, long javaFiles, long totalBytes, long totalLines) {
        this.totalFiles = totalFiles;
        this.javaFiles = javaFiles;
        this.totalBytes = totalBytes;
        this.totalLines = totalLines;
    }

    public long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(long totalFiles) {
        this.totalFiles = totalFiles;
    }

    public long getJavaFiles() {
        return javaFiles;
    }

    public void setJavaFiles(long javaFiles) {
        this.javaFiles = javaFiles;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public long getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(long totalLines) {
        this.totalLines = totalLines;
    }
}

