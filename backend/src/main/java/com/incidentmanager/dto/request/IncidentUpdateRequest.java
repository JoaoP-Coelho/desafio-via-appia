package com.incidentmanager.dto.request;

import java.util.List;

import com.incidentmanager.enums.Priority;
import com.incidentmanager.enums.Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IncidentUpdateRequest(

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 5, max = 120, message = "Título deve ter entre 5 e 120 caracteres")
    String titulo,

    @Size(max = 5000, message = "Descrição deve ter no máximo 5000 caracteres")
    String descricao,

    @NotNull(message = "Prioridade é obrigatória")
    Priority prioridade,

    @NotBlank(message = "E-mail do responsável é obrigatório")
    @Email(message = "E-mail do responsável inválido")
    String responsavelEmail,

    @NotNull(message = "Status é obrigatório")
    Status status,

    List<String> tags

) {
}