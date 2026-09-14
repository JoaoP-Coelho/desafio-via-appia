package com.incidentmanager.dto.response;

import java.util.Map;

import com.incidentmanager.enums.Priority;
import com.incidentmanager.enums.Status;

public record StatsResponse(
        Map<Status, Long> porStatus,
        Map<Priority, Long> porPrioridade
) {
}
