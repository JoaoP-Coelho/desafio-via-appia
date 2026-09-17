package com.incidentmanager.controller;

import com.incidentmanager.dto.response.StatsResponse;
import com.incidentmanager.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/stats")
@Tag(name = "Estatísticas", description = "Endpoints que tratam de estatísticas relacionadas a incidentes")
@SecurityRequirement(name = "bearerAuth")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PreAuthorize("hasAnyAuthority('READ_ONLY', 'WRITER')")
    @Operation(summary = "Obtém estatísticas de incidentes", description = "Retorna estatísticas relacionadas a incidentes, como contagem total, status e prioridade.")
    @GetMapping("/incidents")
    public ResponseEntity<StatsResponse> getIncidentStats() {

        StatsResponse stats = statsService.getIncidentStats();

        return ResponseEntity.ok(stats);
    }
}