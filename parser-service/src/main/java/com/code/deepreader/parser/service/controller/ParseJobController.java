package com.code.deepreader.parser.service.controller;

import com.code.deepreader.parser.service.dto.ParseArtifactsResponse;
import com.code.deepreader.parser.service.dto.ParseRequest;
import com.code.deepreader.parser.service.dto.ParseResponse;
import com.code.deepreader.parser.service.dto.ParseStatusResponse;
import com.code.deepreader.parser.service.pipeline.ParsePipelineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/parses")
public class ParseJobController {

    private final ParsePipelineService parsePipelineService;

    public ParseJobController(ParsePipelineService parsePipelineService) {
        this.parsePipelineService = parsePipelineService;
    }

    @PostMapping
    public ResponseEntity<ParseResponse> triggerParse(@Valid @RequestBody ParseRequest request) {
        return ResponseEntity.accepted().body(parsePipelineService.triggerParse(request));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ParseStatusResponse> queryStatus(@PathVariable String jobId) {
        return parsePipelineService.getStatus(jobId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{jobId}/artifacts")
    public ResponseEntity<ParseArtifactsResponse> queryArtifacts(@PathVariable String jobId) {
        return parsePipelineService.getArtifacts(jobId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

