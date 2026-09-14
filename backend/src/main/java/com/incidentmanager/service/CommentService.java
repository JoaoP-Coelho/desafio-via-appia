package com.incidentmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import com.incidentmanager.repository.UserRepository;
import com.incidentmanager.repository.CommentRepository;
import com.incidentmanager.repository.IncidentRepository;
import com.incidentmanager.entity.Comment;
import com.incidentmanager.entity.Incident;
import com.incidentmanager.entity.User;
import com.incidentmanager.dto.request.CommentCreateRequest;
import com.incidentmanager.dto.response.CommentResponse;
import com.incidentmanager.mapper.IncidentMapper;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;

    public CommentService(
            CommentRepository commentRepository,
            UserRepository userRepository,
            IncidentRepository incidentRepository,
            IncidentMapper incidentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.incidentRepository = incidentRepository;
        this.incidentMapper = incidentMapper;
    }

        public CommentResponse createComment(UUID id, CommentCreateRequest request) {
        Incident incident = incidentRepository.findIncidentById(id);

        String login = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();
        User autor = userRepository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Usuário autenticado não encontrado"));

        Comment comment = incidentMapper.toEntity(request);
        comment.setIncident(incident);
        comment.setAutor(autor);
        comment.setDataCriacao(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return incidentMapper.toResponse(savedComment);
    }

    public List<CommentResponse> getByIncidentId(UUID id) {
        Incident incident = incidentRepository.findIncidentById(id);

        return commentRepository.findByIncidentId(id).stream()
                .map(incidentMapper::toResponse)
                .toList();
    }

}