package com.code.deepreader.ai.orchestrator.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

@ConfigurationProperties(prefix = "orchestrator.llm")
public class LlmModelProperties {

    private String defaultModel = "default-openai";
    private List<ModelConfig> models = new ArrayList<>();

    public String getDefaultModel() {
        return defaultModel;
    }

    public void setDefaultModel(String defaultModel) {
        this.defaultModel = defaultModel;
    }

    public List<ModelConfig> getModels() {
        return models;
    }

    public void setModels(List<ModelConfig> models) {
        this.models = models;
    }

    public ModelConfig findByNameOrDefault(String name) {
        if (CollectionUtils.isEmpty(models)) {
            return null;
        }
        return models.stream()
                .filter(cfg -> cfg.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> models.stream()
                        .filter(cfg -> cfg.getName().equalsIgnoreCase(defaultModel))
                        .findFirst()
                        .orElse(models.get(0)));
    }

    public static class ModelConfig {

        private String name;
        private String provider; // e.g. openai-compatible, glm, deepseek
        private String baseUrl;
        private String apiKey;
        private String model;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}

