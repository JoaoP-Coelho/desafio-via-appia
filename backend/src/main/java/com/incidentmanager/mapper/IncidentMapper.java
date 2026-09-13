package com.incidentmanager.mapper;

import org.springframework.stereotype.Component;

import com.incidentmanager.dto.response.IncidentResponse;
import com.incidentmanager.entity.Incident;

@Component
public class IncidentMapper {

    public IncidentResponse toResponse(Incident incident) {

        return new IncidentResponse(
            incident.getId(),
            incident.getTitulo(),
            incident.getDescricao(),
            incident.getPriority(),
            incident.getStatus(),
            incident.getAutor().getNome(),
            incident.getResponsavelEmail(),
            incident.getTags(),
            incident.getDataAbertura(),
            incident.getDataAtualizacao()
        );
    }
}