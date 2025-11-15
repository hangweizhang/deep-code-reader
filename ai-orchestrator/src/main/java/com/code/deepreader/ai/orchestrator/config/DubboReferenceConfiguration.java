package com.code.deepreader.ai.orchestrator.config;

import com.code.deepreader.rpc.api.graph.GraphQueryRpcService;
import com.code.deepreader.rpc.api.semantic.SemanticSearchRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@ConditionalOnProperty(prefix = "rpc.mock", name = "enabled", havingValue = "false", matchIfMissing = true)
public class DubboReferenceConfiguration {

    @DubboReference(check = false, url = "${rpc.graph-service.url:dubbo://127.0.0.1:20880}")
    private GraphQueryRpcService graphQueryRpcService;

    @DubboReference(check = false, url = "${rpc.semantic-service.url:dubbo://127.0.0.1:20881}")
    private SemanticSearchRpcService semanticSearchRpcService;

    @Bean
    public GraphQueryRpcService graphQueryRpcServiceBean() {
        return graphQueryRpcService;
    }

    @Bean
    public SemanticSearchRpcService semanticSearchRpcServiceBean() {
        return semanticSearchRpcService;
    }
}

