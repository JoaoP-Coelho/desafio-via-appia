package com.incidentmanager.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import com.incidentmanager.dto.request.IncidentCreateRequest;
import com.incidentmanager.dto.request.IncidentUpdateRequest;
import com.incidentmanager.dto.response.IncidentResponse;
import com.incidentmanager.service.IncidentService;

import jakarta.validation.Valid;

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

}