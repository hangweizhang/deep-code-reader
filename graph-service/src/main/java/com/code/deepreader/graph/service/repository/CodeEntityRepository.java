package com.code.deepreader.graph.service.repository;

import com.code.deepreader.graph.service.domain.CodeEntityNode;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface CodeEntityRepository extends Neo4jRepository<CodeEntityNode, String> {

    List<CodeEntityNode> findByProjectId(String projectId);

    @Query("MATCH (c:CodeEntity)-[:DEPENDS_ON]->(t:CodeEntity) WHERE c.id = $entityId RETURN t")
    List<CodeEntityNode> findDirectDependencies(String entityId);
}
