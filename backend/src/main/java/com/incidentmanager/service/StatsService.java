package com.incidentmanager.service;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.incidentmanager.dto.response.StatsResponse;
import com.incidentmanager.entity.Incident;
import com.incidentmanager.enums.Priority;
import com.incidentmanager.enums.Status;
import com.incidentmanager.repository.IncidentRepository;

@Service
public class StatsService {

	private final IncidentRepository incidentRepository;

	public StatsService(IncidentRepository incidentRepository) {
		this.incidentRepository = incidentRepository;
	}

	public StatsResponse getIncidentStats() {
		Map<Status, Long> porStatus = new EnumMap<>(Status.class);
		Map<Priority, Long> porPrioridade = new EnumMap<>(Priority.class);

		for (Status status : Status.values()) {
			porStatus.put(status, 0L);
		}
		for (Priority priority : Priority.values()) {
			porPrioridade.put(priority, 0L);
		}

		for (Incident incident : incidentRepository.findAll()) {
			porStatus.computeIfPresent(incident.getStatus(),
					(status, total) -> total + 1);
			porPrioridade.computeIfPresent(incident.getPriority(),
					(priority, total) -> total + 1);
		}

		return new StatsResponse(
				porStatus,
				porPrioridade);
	}
}
