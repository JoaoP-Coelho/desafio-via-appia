package com.incidentmanager.controller;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.incidentmanager.dto.request.IncidentCreateRequest;
import com.incidentmanager.dto.request.IncidentUpdateRequest;
import com.incidentmanager.dto.request.IncidentStatusUpdateRequest;
import com.incidentmanager.dto.response.IncidentResponse;
import com.incidentmanager.enums.Priority;
import com.incidentmanager.enums.Status;
import com.incidentmanager.service.IncidentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/incidents")
@Tag(name = "Incidentes", description = "Endpoints que tratam de incidentes")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @Operation(summary = "Cria um novo incidente", description = "Cria um novo incidente com base nas informações fornecidas.")
    @PostMapping
    public ResponseEntity<IncidentResponse> create(
            @Valid @RequestBody IncidentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(incidentService.create(request));
    }

    @PreAuthorize("hasAnyAuthority('READ_ONLY', 'WRITER')")
    @Operation(summary = "Busca incidentes", description = "Busca incidentes com base nos filtros e com busca paginada.")
    @GetMapping
    public ResponseEntity<Page<IncidentResponse>> search(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority prioridade,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(10) int size,
            @RequestParam(defaultValue = "dataAbertura,desc") String sort) {
        return ResponseEntity.ok(
                incidentService.search(status, prioridade, q, page, size, sort));
    }

    @PreAuthorize("hasAnyAuthority('READ_ONLY', 'WRITER')")
    @Operation(summary = "Busca incidente por ID", description = "Busca um incidente em específico pelo seu ID.")
    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getById(@PathVariable UUID id) {
        IncidentResponse incidentResponse = incidentService.getById(id);
        return ResponseEntity.ok(incidentResponse);
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @Operation(summary = "Atualiza um incidente", description = "Atualiza as informações de um incidente existente.")
    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody IncidentUpdateRequest request) {

        IncidentResponse response = incidentService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @Operation(summary = "Atualiza o status de um incidente", description = "Atualiza o status de um incidente existente.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<IncidentResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody IncidentStatusUpdateRequest request) {

        IncidentResponse response = incidentService.updateStatus(id, request.status());

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @Operation(summary = "Deleta um incidente", description = "Deleta um incidente existente pelo seu ID, junto com todos seus comentários.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        incidentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}