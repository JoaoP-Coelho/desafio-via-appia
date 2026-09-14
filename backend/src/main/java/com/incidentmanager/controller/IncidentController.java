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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import com.incidentmanager.dto.request.IncidentCreateRequest;
import com.incidentmanager.dto.request.IncidentUpdateRequest;
import com.incidentmanager.dto.response.IncidentResponse;
import com.incidentmanager.enums.Priority;
import com.incidentmanager.enums.Status;
import com.incidentmanager.service.IncidentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/incidents")
@Validated
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @PostMapping
    public ResponseEntity<IncidentResponse> create(
            @Valid @RequestBody IncidentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(incidentService.create(request));
    }

    @PreAuthorize("hasAnyAuthority('READ_ONLY', 'WRITER')")
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
    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getById(@PathVariable UUID id) {
        IncidentResponse incidentResponse = incidentService.getById(id);
        return ResponseEntity.ok(incidentResponse);
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody IncidentUpdateRequest request) {

        IncidentResponse response = incidentService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('WRITER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        incidentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}