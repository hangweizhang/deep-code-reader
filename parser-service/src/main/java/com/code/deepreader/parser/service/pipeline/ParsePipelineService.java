package com.code.deepreader.parser.service.pipeline;

import com.code.deepreader.common.infra.model.ParseJobDescriptor;
import com.code.deepreader.common.infra.model.ParseJobStatus;
import com.code.deepreader.common.infra.model.ProjectSnapshotRef;
import com.code.deepreader.parser.service.dto.ParseRequest;
import com.code.deepreader.parser.service.dto.ParseResponse;
import com.code.deepreader.parser.service.dto.ParseStatusResponse;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ParsePipelineService {

    private static final Logger log = LoggerFactory.getLogger(ParsePipelineService.class);
    private final Map<String, ParseJobDescriptor> jobStore = new ConcurrentHashMap<>();

    public ParseResponse triggerParse(ParseRequest request) {
        String jobId = UUID.randomUUID().toString();

        ProjectSnapshotRef snapshotRef = new ProjectSnapshotRef(
                request.getProjectId(),
                request.getRepositoryUrl(),
                request.getCommitId(),
                Instant.now()
        );

        ParseJobDescriptor descriptor = new ParseJobDescriptor(jobId, snapshotRef, ParseJobStatus.PENDING, Instant.now());
        jobStore.put(jobId, descriptor);

        log.info("[Parser] 接收到解析请求, jobId={}, projectId={}", jobId, request.getProjectId());
        execute(snapshotRef, Path.of(request.getWorkspace()));
        return new ParseResponse(jobId, descriptor.getCreatedAt());
    }

    public Optional<ParseStatusResponse> getStatus(String jobId) {
        return Optional.ofNullable(jobStore.get(jobId))
                .map(descriptor -> new ParseStatusResponse(
                        descriptor.getJobId(),
                        descriptor.getStatus(),
                        descriptor.getCreatedAt()
                ));
    }

    private void execute(ProjectSnapshotRef snapshotRef, Path workspace) {
        log.info("[Parser] 开始解析, projectId={}, commitId={}, workspace={}",
                snapshotRef.getProjectId(), snapshotRef.getCommitId(), workspace);
        // TODO zhanghangwei 2025-11-15: 调用 JDT/Javaparser 构建 IR
    }
}
