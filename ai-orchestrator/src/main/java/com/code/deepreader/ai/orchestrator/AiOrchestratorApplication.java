package com.code.deepreader.ai.orchestrator;

import com.code.deepreader.ai.orchestrator.config.LlmModelProperties;
import com.code.deepreader.ai.orchestrator.memory.MemoryProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "com.code.deepreader")
@EnableConfigurationProperties({LlmModelProperties.class, MemoryProperties.class})
public class AiOrchestratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiOrchestratorApplication.class, args);
    }
}
