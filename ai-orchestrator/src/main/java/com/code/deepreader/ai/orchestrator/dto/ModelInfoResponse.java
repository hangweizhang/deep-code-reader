package com.code.deepreader.ai.orchestrator.dto;

public class ModelInfoResponse {

    private String name;
    private String provider;
    private String model;
    private String description;
    private boolean isDefault;

    public ModelInfoResponse() {
    }

    public ModelInfoResponse(String name, String provider, String model, String description, boolean isDefault) {
        this.name = name;
        this.provider = provider;
        this.model = model;
        this.description = description;
        this.isDefault = isDefault;
    }

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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}

