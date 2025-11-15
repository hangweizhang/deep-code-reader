package com.code.deepreader.ingestion.service.service;

import com.code.deepreader.common.infra.model.IngestionJob;
import com.code.deepreader.common.infra.model.IngestionJobStatus;
import com.code.deepreader.common.infra.model.ProjectSnapshotRef;
import com.code.deepreader.ingestion.service.dto.IngestionRequest;
import com.code.deepreader.ingestion.service.dto.IngestionResponse;
import com.code.deepreader.ingestion.service.dto.IngestionStatusResponse;
import java.time.Instant;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RepositoryIngestionService {

    private static final Logger log = LoggerFactory.getLogger(RepositoryIngestionService.class);
    private final Map<String, IngestionJob> jobStore = new ConcurrentHashMap<>();

    public IngestionResponse scheduleIngestion(IngestionRequest request) {
        String ingestionId = UUID.randomUUID().toString();

        ProjectSnapshotRef ref = new ProjectSnapshotRef(
                request.getProjectId(),
                request.getRepositoryUrl(),
                request.getCommitId(),
                Instant.now()
        );

        IngestionJob job = new IngestionJob(ingestionId, ref, IngestionJobStatus.PENDING, Instant.now());
        jobStore.put(ingestionId, job);

        log.info("[Ingestion] 接收到新的摄取请求, ref={}, forceFullScan={}", ref, request.isForceFullScan());
        return new IngestionResponse(ingestionId, job.getCreatedAt());
    }

    public Optional<IngestionStatusResponse> findJob(String ingestionId) {
        return Optional.ofNullable(jobStore.get(ingestionId))
                .map(job -> new IngestionStatusResponse(job.getJobId(), job.getStatus(), job.getCreatedAt()));
    }

    public List<ProjectSnapshotRef> listSnapshots(String projectId) {
        return jobStore.values().stream()
                .map(IngestionJob::getSnapshotRef)
                .filter(ref -> ref.getProjectId().equals(projectId))
                .collect(Collectors.toList());
    }

    public void updateJobStatus(String ingestionId, IngestionJobStatus status) {
        IngestionJob job = jobStore.get(ingestionId);
        if (job != null) {
            job.setStatus(status);
            log.info("[Ingestion] 更新任务状态, ingestionId={}, status={}", ingestionId, status);
        } else {
            log.warn("[Ingestion] 未找到任务, ingestionId={}", ingestionId);
        }
    }
}
