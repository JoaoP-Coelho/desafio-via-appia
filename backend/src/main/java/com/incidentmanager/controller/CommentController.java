package com.incidentmanager.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import com.incidentmanager.dto.request.CommentCreateRequest;
import com.incidentmanager.dto.response.CommentResponse;
import com.incidentmanager.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/incidents")
@Validated
@Tag(name = "Comentários", description = "Endpoints que tratam de comentários relacionados a incidentes")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @Operation(summary = "Cria um comentário em um incidente", description = "Cria um comentário associado a um incidente.")
    @PostMapping("/{incidentId}/comments")
    public ResponseEntity<CommentResponse> create(
            @PathVariable UUID incidentId,
            @Valid @RequestBody CommentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(incidentId, request));
    }

    @PreAuthorize("hasAnyAuthority('READ_ONLY', 'WRITER')")
    @Operation(summary = "Busca comentários de um incidente", description = "Retorna todos os comentários associados a um incidente.")
    @GetMapping("/{incidentId}/comments")
    public ResponseEntity<List<CommentResponse>> getByIncident(
            @PathVariable UUID incidentId) {
        return ResponseEntity.ok(commentService.getByIncidentId(incidentId));
    }

}