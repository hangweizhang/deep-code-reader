package com.code.deepreader.ai.orchestrator.service.model;

import com.code.deepreader.ai.orchestrator.config.LlmModelProperties;
import com.code.deepreader.ai.orchestrator.config.LlmModelProperties.ModelConfig;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Component;

@Component
public class LlmChatClientManager {

    private final Map<String, ChatClient> chatClients;
    private final Map<String, ModelConfig> configByName;
    private final LlmModelProperties properties;

    public LlmChatClientManager(LlmModelProperties properties) {
        this.properties = properties;
        this.configByName = properties.getModels().stream()
                .collect(Collectors.toMap(ModelConfig::getName, cfg -> cfg));
        this.chatClients = properties.getModels().stream()
                .collect(Collectors.toMap(ModelConfig::getName, this::buildClient));
    }

    public ChatClient resolveClient(String name) {
        ModelConfig cfg = properties.findByNameOrDefault(name);
        if (cfg == null) {
            throw new IllegalStateException("No LLM model configured");
        }
        return chatClients.get(cfg.getName());
    }

    public ModelConfig getConfig(String name) {
        ModelConfig cfg = properties.findByNameOrDefault(name);
        if (cfg == null) {
            throw new IllegalStateException("No LLM model configured");
        }
        return cfg;
    }

    public List<ModelDescriptor> listModels() {
        if (properties.getModels() == null) {
            return Collections.emptyList();
        }
        return properties.getModels().stream()
                .map(cfg -> new ModelDescriptor(
                        cfg.getName(),
                        cfg.getProvider(),
                        cfg.getModel(),
                        cfg.getDescription(),
                        cfg.getName().equalsIgnoreCase(properties.getDefaultModel())
                ))
                .collect(Collectors.toList());
    }

    private ChatClient buildClient(ModelConfig config) {
        OpenAiApi api = new OpenAiApi(config.getBaseUrl(), config.getApiKey());
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .withModel(config.getModel())
                .build();
        OpenAiChatModel chatModel = new OpenAiChatModel(api, options);
        return ChatClient.builder(chatModel).build();
    }

    public record ModelDescriptor(String name,
                                  String provider,
                                  String model,
                                  String description,
                                  boolean isDefault) {
    }
}

