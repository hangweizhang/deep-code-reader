package com.code.deepreader.graph.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.code.deepreader.graph.service.domain.CodeEntityNode;
import com.code.deepreader.graph.service.repository.CodeEntityRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GraphQueryServiceTest {

    private CodeEntityRepository repository;
    private GraphQueryService graphQueryService;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(CodeEntityRepository.class);
        graphQueryService = new GraphQueryService(repository);
    }

    @Test
    void listByProjectShouldDelegateToRepository() {
        var node = new CodeEntityNode("id", "Foo", "CLASS", "proj", Instant.now());
        when(repository.findByProjectId("proj")).thenReturn(List.of(node));

        assertThat(graphQueryService.listByProject("proj")).containsExactly(node);
    }
}

