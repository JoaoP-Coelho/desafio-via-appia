package com.incidentmanager.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import com.incidentmanager.dto.request.IncidentCreateRequest;
import com.incidentmanager.dto.request.IncidentUpdateRequest;
import com.incidentmanager.dto.response.IncidentResponse;
import com.incidentmanager.entity.Incident;
import com.incidentmanager.enums.Status;
import com.incidentmanager.mapper.IncidentMapper;
import com.incidentmanager.repository.IncidentRepository;
import com.incidentmanager.repository.UserRepository;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;
    private final UserRepository userRepository;

    public IncidentService(
            IncidentRepository incidentRepository,
            IncidentMapper incidentMapper,
            UserRepository userRepository) {

        this.incidentRepository = incidentRepository;
        this.incidentMapper = incidentMapper;
        this.userRepository = userRepository;
    }

    public IncidentResponse create(IncidentCreateRequest request) {

        Incident incident = incidentMapper.toEntity(request);

        String login = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        incident.setAutor(userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário autenticado não encontrado")));

        incident.setStatus(Status.ABERTA);

        LocalDateTime currentTime = LocalDateTime.now();

        incident.setDataAbertura(currentTime);
        incident.setDataAtualizacao(currentTime);

        Incident savedIncident = incidentRepository.save(incident);
        return incidentMapper.toResponse(savedIncident);
    }

    public IncidentResponse getById(UUID id) {
        Incident incident = incidentRepository.findIncidentById(id);
        return incidentMapper.toResponse(incident);
    }
    
    public IncidentResponse update(UUID id, IncidentUpdateRequest request) {

        Incident incident = incidentRepository.findIncidentById(id);

        incidentMapper.updateEntity(incident, request);

        incident.setDataAtualizacao(LocalDateTime.now());

        Incident updatedIncident = incidentRepository.save(incident);

        return incidentMapper.toResponse(updatedIncident);
    }

    public void delete(UUID id) {
        Incident incident = incidentRepository.findIncidentById(id);
        incidentRepository.delete(incident);
    }

}