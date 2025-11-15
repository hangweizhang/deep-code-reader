package com.code.deepreader.parser.service.pipeline;

import com.code.deepreader.common.infra.model.ParseJobDescriptor;
import com.code.deepreader.common.infra.model.ParseJobStatus;
import com.code.deepreader.common.infra.model.ProjectSnapshotRef;
import com.code.deepreader.parser.service.dto.FileParseResult;
import com.code.deepreader.parser.service.dto.ParseArtifactsResponse;
import com.code.deepreader.parser.service.dto.ParseRequest;
import com.code.deepreader.parser.service.dto.ParseResponse;
import com.code.deepreader.parser.service.dto.ParseStatusResponse;
import com.code.deepreader.parser.service.dto.ParseSummary;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ParsePipelineService {

    private static final Logger log = LoggerFactory.getLogger(ParsePipelineService.class);
    private final Map<String, ParseJobRecord> jobStore = new ConcurrentHashMap<>();
    private final boolean asyncExecution;
    private final ExecutorService executorService;
    private final boolean manageExecutorLifetime;

    public ParsePipelineService(
            @Value("${parser.execution.async:true}") boolean asyncExecution,
            @Value("${parser.execution.threads:4}") int maxThreads) {
        this.asyncExecution = asyncExecution;
        if (asyncExecution) {
            int poolSize = Math.max(1, maxThreads);
            this.executorService = Executors.newFixedThreadPool(poolSize, new ParseThreadFactory());
            this.manageExecutorLifetime = true;
        } else {
            this.executorService = null;
            this.manageExecutorLifetime = false;
        }
    }

    ParsePipelineService(boolean asyncExecution, ExecutorService executorService) {
        this.asyncExecution = asyncExecution;
        this.executorService = executorService;
        this.manageExecutorLifetime = false;
    }

    public ParseResponse triggerParse(ParseRequest request) {
        String jobId = UUID.randomUUID().toString();

        ProjectSnapshotRef snapshotRef = new ProjectSnapshotRef(
                request.getProjectId(),
                request.getRepositoryUrl(),
                request.getCommitId(),
                Instant.now()
        );

        ParseJobDescriptor descriptor = new ParseJobDescriptor(jobId, snapshotRef, ParseJobStatus.PENDING, Instant.now());
        ParseJobRecord record = new ParseJobRecord(descriptor);
        jobStore.put(jobId, record);

        log.info("[Parser] 接收到解析请求, jobId={}, projectId={}, workspace={}", jobId, request.getProjectId(), request.getWorkspace());
        runJob(record, request.getWorkspace());
        return new ParseResponse(jobId, descriptor.getCreatedAt(), descriptor.getStatus());
    }

    public Optional<ParseStatusResponse> getStatus(String jobId) {
        return Optional.ofNullable(jobStore.get(jobId))
                .map(ParseJobRecord::snapshot);
    }

    public Optional<ParseArtifactsResponse> getArtifacts(String jobId) {
        return Optional.ofNullable(jobStore.get(jobId))
                .map(record -> new ParseArtifactsResponse(jobId, record.artifactsSnapshot()));
    }

    @PreDestroy
    public void shutdown() {
        if (manageExecutorLifetime && executorService != null) {
            executorService.shutdownNow();
        }
    }

    private void runJob(ParseJobRecord record, String workspaceValue) {
        Runnable task = () -> {
            record.markRunning();
            try {
                Path workspace = resolveWorkspace(workspaceValue);
                WorkspaceParseResult result = analyzeWorkspace(record.getDescriptor().getSnapshotRef(), workspace);
                record.markSucceeded(result);
            } catch (Exception ex) {
                log.error("[Parser] 解析失败, jobId={}", record.getDescriptor().getJobId(), ex);
                record.markFailed(ex.getMessage());
            }
        };

        if (asyncExecution && executorService != null) {
            executorService.submit(task);
        } else {
            task.run();
        }
    }

    private Path resolveWorkspace(String workspaceValue) throws IOException {
        if (workspaceValue == null || workspaceValue.isBlank()) {
            Path temp = Files.createTempDirectory("parser-workspace-");
            log.warn("[Parser] 未提供 workspace, 自动创建临时目录 {}", temp);
            return temp;
        }
        try {
            Path workspace = Path.of(workspaceValue);
            if (Files.notExists(workspace)) {
                Files.createDirectories(workspace);
            }
            return workspace;
        } catch (InvalidPathException ex) {
            throw new IOException("非法 workspace 路径: " + workspaceValue, ex);
        }
    }

    private WorkspaceParseResult analyzeWorkspace(ProjectSnapshotRef snapshotRef, Path workspace) throws IOException {
        log.info("[Parser] 开始解析, projectId={}, commitId={}, workspace={}", snapshotRef.getProjectId(), snapshotRef.getCommitId(), workspace);

        AtomicLong totalFiles = new AtomicLong();
        AtomicLong javaFiles = new AtomicLong();
        AtomicLong totalBytes = new AtomicLong();
        AtomicLong totalLines = new AtomicLong();
        List<FileParseResult> artifacts = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (Files.exists(workspace)) {
            try (var paths = Files.walk(workspace)) {
                paths.filter(Files::isRegularFile).forEach(path -> {
                    totalFiles.incrementAndGet();
                    try {
                        totalBytes.addAndGet(Files.size(path));
                        long lineCount = countLines(path);
                        totalLines.addAndGet(lineCount);
                        if (path.toString().endsWith(".java")) {
                            javaFiles.incrementAndGet();
                            parseJavaFile(workspace, path, lineCount).ifPresentOrElse(artifacts::add,
                                    () -> warnings.add("无法解析: " + path));
                        }
                    } catch (IOException e) {
                        log.warn("[Parser] 读取文件信息失败, path={}, err={}", path, e.getMessage());
                        warnings.add("无法读取: " + path + ", err=" + e.getMessage());
                    }
                });
            }
        }

        log.info("[Parser] 解析完成, projectId={}, files={}, javaFiles={}, warnings={}",
                snapshotRef.getProjectId(), totalFiles.get(), javaFiles.get(), warnings.size());
        ParseSummary summary = new ParseSummary(totalFiles.get(), javaFiles.get(), totalBytes.get(), totalLines.get());
        return new WorkspaceParseResult(
                summary,
                Collections.unmodifiableList(new ArrayList<>(artifacts)),
                Collections.unmodifiableList(new ArrayList<>(warnings))
        );
    }

    private Optional<FileParseResult> parseJavaFile(Path root, Path file, long linesOfCode) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(file);
            String packageName = cu.getPackageDeclaration()
                    .map(pd -> pd.getName().asString())
                    .orElse("");
            List<String> imports = cu.getImports().stream()
                    .map(ImportDeclaration::getNameAsString)
                    .collect(Collectors.toList());
            List<String> typeNames = cu.findAll(ClassOrInterfaceDeclaration.class).stream()
                    .map(ClassOrInterfaceDeclaration::getNameAsString)
                    .collect(Collectors.toList());
            typeNames.addAll(cu.findAll(EnumDeclaration.class).stream()
                    .map(EnumDeclaration::getNameAsString)
                    .collect(Collectors.toList()));
            typeNames = typeNames.stream().distinct().collect(Collectors.toList());
            long methodCount = cu.findAll(MethodDeclaration.class).size();
            String relativePath = relativize(root, file);
            String sha256 = sha256(file);
            Instant lastModified = Files.getLastModifiedTime(file).toInstant();

            FileParseResult result = new FileParseResult(
                    relativePath,
                    packageName,
                    typeNames,
                    imports,
                    methodCount,
                    linesOfCode,
                    sha256,
                    lastModified
            );
            return Optional.of(result);
        } catch (IOException | ParseProblemException e) {
            log.warn("[Parser] Java 文件解析失败, path={}, err={}", file, e.getMessage());
            return Optional.empty();
        }
    }

    private long countLines(Path path) throws IOException {
        try (var lines = Files.lines(path)) {
            return lines.count();
        }
    }

    private String sha256(Path path) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("缺少 SHA-256 算法", e);
        }

        try (InputStream input = Files.newInputStream(path);
             DigestInputStream dis = new DigestInputStream(input, digest)) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {
                // consume stream
            }
        }
        byte[] hash = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String relativize(Path root, Path file) {
        try {
            return root.relativize(file).toString();
        } catch (IllegalArgumentException ex) {
            return file.toString();
        }
    }

    private static final class ParseJobRecord {
        private final ParseJobDescriptor descriptor;
        private Instant startedAt;
        private Instant finishedAt;
        private String message;
        private ParseSummary summary;
        private List<FileParseResult> artifacts = Collections.emptyList();
        private List<String> warnings = Collections.emptyList();

        private ParseJobRecord(ParseJobDescriptor descriptor) {
            this.descriptor = descriptor;
        }

        private void markRunning() {
            descriptor.setStatus(ParseJobStatus.RUNNING);
            startedAt = Instant.now();
            message = "开始解析";
        }

        private void markSucceeded(WorkspaceParseResult result) {
            descriptor.setStatus(ParseJobStatus.SUCCEEDED);
            finishedAt = Instant.now();
            this.summary = result.summary();
            this.artifacts = Collections.unmodifiableList(new ArrayList<>(result.artifacts()));
            this.warnings = Collections.unmodifiableList(new ArrayList<>(result.warnings()));
            if (warnings.isEmpty()) {
                message = "解析完成";
            } else {
                String preview = warnings.stream().limit(3).collect(Collectors.joining("; "));
                message = "解析完成（warnings=" + warnings.size() + (preview.isEmpty() ? "" : ", " + preview) + "）";
            }
        }

        private void markFailed(String reason) {
            descriptor.setStatus(ParseJobStatus.FAILED);
            finishedAt = Instant.now();
            message = reason;
            warnings = Collections.singletonList(reason);
            artifacts = Collections.emptyList();
            summary = null;
        }

        private ParseJobDescriptor getDescriptor() {
            return descriptor;
        }

        private ParseStatusResponse snapshot() {
            return new ParseStatusResponse(
                    descriptor.getJobId(),
                    descriptor.getStatus(),
                    descriptor.getCreatedAt(),
                    startedAt,
                    finishedAt,
                    message,
                    summary,
                    descriptor.getSnapshotRef()
            );
        }

        private List<FileParseResult> artifactsSnapshot() {
            return artifacts == null ? List.of() : artifacts;
        }
    }

    private static final class WorkspaceParseResult {
        private final ParseSummary summary;
        private final List<FileParseResult> artifacts;
        private final List<String> warnings;

        private WorkspaceParseResult(ParseSummary summary,
                                     List<FileParseResult> artifacts,
                                     List<String> warnings) {
            this.summary = summary;
            this.artifacts = artifacts;
            this.warnings = warnings;
        }

        private ParseSummary summary() {
            return summary;
        }

        private List<FileParseResult> artifacts() {
            return artifacts;
        }

        private List<String> warnings() {
            return warnings;
        }
    }

    private static final class ParseThreadFactory implements ThreadFactory {

        private int idx = 0;

        @Override
        public synchronized Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "parse-worker-" + idx++);
            thread.setDaemon(true);
            return thread;
        }
    }
}
