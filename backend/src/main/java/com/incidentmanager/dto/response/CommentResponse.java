package com.incidentmanager.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        UUID incidentId,
        String autor,
        String mensagem,
        LocalDateTime dataCriacao
) {}