package com.code.deepreader.rpc.api.graph;

import com.code.deepreader.rpc.api.graph.dto.GraphEntityDTO;
import java.util.List;

public interface GraphQueryRpcService {

    List<GraphEntityDTO> listEntities(String projectId, int limit);

    List<GraphEntityDTO> listDependencies(String entityId);
}

