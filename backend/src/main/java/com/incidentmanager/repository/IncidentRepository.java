package com.incidentmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.incidentmanager.entity.Incident;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {

    default Incident findIncidentById(UUID id) {
        return this.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Incident not found with id: " + id));
    }

}