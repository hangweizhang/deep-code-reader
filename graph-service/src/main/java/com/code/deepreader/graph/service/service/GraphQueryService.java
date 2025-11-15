package com.code.deepreader.graph.service.service;

import com.code.deepreader.graph.service.domain.CodeEntityNode;
import com.code.deepreader.graph.service.repository.CodeEntityRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GraphQueryService {

    private final CodeEntityRepository repository;

    public GraphQueryService(CodeEntityRepository repository) {
        this.repository = repository;
    }

    public List<CodeEntityNode> listByProject(String projectId) {
        return repository.findByProjectId(projectId);
    }

    public List<CodeEntityNode> queryDependencies(String entityId) {
        return repository.findDirectDependencies(entityId);
    }
}
