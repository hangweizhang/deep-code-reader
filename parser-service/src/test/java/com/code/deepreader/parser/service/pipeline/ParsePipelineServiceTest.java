package com.code.deepreader.parser.service.pipeline;

import static org.assertj.core.api.Assertions.assertThat;

import com.code.deepreader.parser.service.dto.ParseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParsePipelineServiceTest {

    private ParsePipelineService parsePipelineService;

    @BeforeEach
    void setUp() {
        parsePipelineService = new ParsePipelineService();
    }

    @Test
    void triggerParseShouldRecordJob() {
        ParseRequest request = new ParseRequest();
        request.setProjectId("proj-1");
        request.setRepositoryUrl("https://example.com/repo.git");
        request.setCommitId("abc1234");
        request.setWorkspace("/tmp/workspace");

        var response = parsePipelineService.triggerParse(request);

        assertThat(parsePipelineService.getStatus(response.getParseJobId())).isPresent();
    }
}

