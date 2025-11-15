package com.code.deepreader.ingestion.service.dto;

import com.code.deepreader.common.infra.model.IngestionJobStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateIngestionStatusRequest {

    @NotNull(message = "status不能为空")
    private IngestionJobStatus status;

    public IngestionJobStatus getStatus() {
        return status;
    }

    public void setStatus(IngestionJobStatus status) {
        this.status = status;
    }
}

