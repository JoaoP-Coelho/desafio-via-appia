package com.incidentmanager.mapper;

import org.springframework.stereotype.Component;

import com.incidentmanager.dto.request.IncidentCreateRequest;
import com.incidentmanager.dto.request.IncidentUpdateRequest;
import com.incidentmanager.dto.request.CommentCreateRequest;
import com.incidentmanager.dto.response.CommentResponse;
import com.incidentmanager.dto.response.IncidentResponse;
import com.incidentmanager.entity.Comment;
import com.incidentmanager.entity.Incident;
import com.incidentmanager.util.TagUtils;

@Component
public class IncidentMapper {

    public Incident toEntity(IncidentCreateRequest request) {
        Incident incident = new Incident();
        incident.setTitulo(request.titulo());
        incident.setDescricao(request.descricao());
        incident.setPriority(request.prioridade());
        incident.setResponsavelEmail(request.responsavelEmail());
        incident.setTags(TagUtils.normalizeTags(request.tags()));
        return incident;
    }

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

    public Comment toEntity(CommentCreateRequest request) {
        Comment comment = new Comment();
        comment.setMensagem(request.mensagem());
        return comment;
    }

    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getIncident().getId(),
            comment.getAutor().getNome(),
            comment.getMensagem(),
            comment.getDataCriacao()
        );
    }

    public void updateEntity(Incident incident, IncidentUpdateRequest request) {
        incident.setTitulo(request.titulo());
        incident.setDescricao(request.descricao());
        incident.setPriority(request.prioridade());
        incident.setStatus(request.status());
        incident.setTags(TagUtils.normalizeTags(request.tags()));
    }

}