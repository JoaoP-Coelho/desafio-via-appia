package com.incidentmanager.dto.request;

import com.incidentmanager.enums.Status;

import jakarta.validation.constraints.NotNull;

public record IncidentStatusUpdateRequest(
        @NotNull(message = "Status é obrigatório")
        Status status
) {
}
