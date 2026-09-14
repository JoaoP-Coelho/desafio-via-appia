package com.incidentmanager.repository.specification;

import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;

import com.incidentmanager.entity.Incident;
import com.incidentmanager.enums.Priority;
import com.incidentmanager.enums.Status;

public final class IncidentSpecifications {

    private IncidentSpecifications() {
    }

    public static Specification<Incident> buildIncidentFilter(
            Status status,
            Priority prioridade,
            String query) {
        Specification<Incident> specification = (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.conjunction();

        if (status != null) {
            specification = specification.and(hasStatus(status));
        }
        if (prioridade != null) {
            specification = specification.and(hasPriority(prioridade));
        }
        if (query != null && !query.isBlank()) {
            specification = specification.and(containsText(query));
        }

        return specification;
    }

    private static Specification<Incident> hasStatus(Status status) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    private static Specification<Incident> hasPriority(Priority prioridade) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), prioridade);
    }

    private static Specification<Incident> containsText(String query) {
        String pattern = "%" + query.trim().toLowerCase(Locale.ROOT) + "%";
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("titulo")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("descricao")), pattern));
    }
}
