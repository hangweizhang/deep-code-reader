package com.code.deepreader.parser.service.pipeline;

import static org.assertj.core.api.Assertions.assertThat;

import com.code.deepreader.common.infra.model.ParseJobStatus;
import com.code.deepreader.parser.service.dto.ParseRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParsePipelineServiceTest {

    private ParsePipelineService parsePipelineService;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        parsePipelineService = new ParsePipelineService(false, null);
        tempDir = Files.createTempDirectory("parser-test-");
    }

    @AfterEach
    void tearDown() throws IOException {
        if (tempDir != null && Files.exists(tempDir)) {
            try (var paths = Files.walk(tempDir)) {
                paths.sorted((a, b) -> b.compareTo(a))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException ignored) {
                            }
                        });
            }
        }
    }

    @Test
    void triggerParseShouldProduceSummary() throws IOException {
        Files.writeString(tempDir.resolve("Main.java"), "class Main {}");
        Files.writeString(tempDir.resolve("notes.md"), "hello");

        ParseRequest request = buildRequest(tempDir.toString());

        var response = parsePipelineService.triggerParse(request);
        var status = parsePipelineService.getStatus(response.getParseJobId()).orElseThrow();
        var artifactsResponse = parsePipelineService.getArtifacts(response.getParseJobId()).orElseThrow();

        assertThat(response.getStatus()).isEqualTo(ParseJobStatus.SUCCEEDED);
        assertThat(status.getStatus()).isEqualTo(ParseJobStatus.SUCCEEDED);
        assertThat(status.getSummary()).isNotNull();
        assertThat(status.getSummary().getTotalFiles()).isEqualTo(2);
        assertThat(status.getSummary().getJavaFiles()).isEqualTo(1);
        assertThat(status.getSummary().getTotalBytes()).isGreaterThan(0);
        assertThat(status.getSummary().getTotalLines()).isGreaterThan(0);
        assertThat(status.getMessage()).isEqualTo("解析完成");
        assertThat(status.getStartedAt()).isNotNull();
        assertThat(status.getFinishedAt()).isNotNull();
        assertThat(artifactsResponse.getArtifacts()).hasSize(1);
        var artifact = artifactsResponse.getArtifacts().get(0);
        assertThat(artifact.getRelativePath()).isEqualTo("Main.java");
        assertThat(artifact.getMethodCount()).isEqualTo(0);
        assertThat(artifact.getLinesOfCode()).isGreaterThan(0);
        assertThat(artifact.getSha256()).hasSize(64);
    }

    @Test
    void triggerParseWithInvalidWorkspaceShouldFailGracefully() {
        String invalidWorkspace = "\u0000invalid";
        ParseRequest request = buildRequest(invalidWorkspace);

        var response = parsePipelineService.triggerParse(request);
        var status = parsePipelineService.getStatus(response.getParseJobId()).orElseThrow();
        var artifacts = parsePipelineService.getArtifacts(response.getParseJobId()).orElseThrow();

        assertThat(status.getStatus()).isEqualTo(ParseJobStatus.FAILED);
        assertThat(status.getMessage()).contains("非法 workspace");
        assertThat(artifacts.getArtifacts()).isEmpty();
    }

    private ParseRequest buildRequest(String workspace) {
        ParseRequest request = new ParseRequest();
        request.setProjectId("proj-1");
        request.setRepositoryUrl("https://example.com/repo.git");
        request.setCommitId("abc1234");
        request.setWorkspace(workspace);
        return request;
    }
}
