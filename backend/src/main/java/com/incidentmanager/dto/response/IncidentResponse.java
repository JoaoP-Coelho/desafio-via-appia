package com.incidentmanager.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.incidentmanager.enums.Priority;
import com.incidentmanager.enums.Status;

public record IncidentResponse(

    UUID id,
    String titulo,
    String descricao,
    Priority prioridade,
    Status status,
    String autor,
    String responsavelEmail,
    List<String> tags,
    LocalDateTime dataAbertura,
    LocalDateTime dataAtualizacao
) {
}