package com.incidentmanager.controller;

import com.incidentmanager.dto.response.StatsResponse;
import com.incidentmanager.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PreAuthorize("hasAnyAuthority('READ_ONLY', 'WRITER')")
    @GetMapping("/incidents")
    public ResponseEntity<StatsResponse> getIncidentStats() {

        StatsResponse stats = statsService.getIncidentStats();

        return ResponseEntity.ok(stats);
    }
}