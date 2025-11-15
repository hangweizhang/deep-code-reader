package com.code.deepreader.graph.service.rpc;

import com.code.deepreader.graph.service.domain.CodeEntityNode;
import com.code.deepreader.graph.service.service.GraphQueryService;
import com.code.deepreader.rpc.api.graph.GraphQueryRpcService;
import com.code.deepreader.rpc.api.graph.dto.GraphEntityDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = "1.0.0")
public class GraphQueryRpcServiceImpl implements GraphQueryRpcService {

    private final GraphQueryService graphQueryService;

    public GraphQueryRpcServiceImpl(GraphQueryService graphQueryService) {
        this.graphQueryService = graphQueryService;
    }

    @Override
    public List<GraphEntityDTO> listEntities(String projectId, int limit) {
        return graphQueryService.listByProject(projectId).stream()
                .limit(limit)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GraphEntityDTO> listDependencies(String entityId) {
        return graphQueryService.queryDependencies(entityId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private GraphEntityDTO toDto(CodeEntityNode node) {
        return new GraphEntityDTO(
                node.getId(),
                node.getName(),
                node.getType(),
                node.getProjectId(),
                node.getLastTouchedAt()
        );
    }
}

