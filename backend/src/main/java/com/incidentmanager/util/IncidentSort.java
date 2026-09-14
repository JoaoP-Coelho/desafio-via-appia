package com.incidentmanager.util;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class IncidentSort {

    private IncidentSort() {
    }

    public static Sort parse(String sort) {
        String sortValue = sort == null || sort.isBlank()
                ? "dataAbertura,desc"
                : sort;
        String[] parts = sortValue.split(",", -1);

        if (parts.length != 2) {
            throw badRequest("sort must have the format campo,asc|desc");
        }

        String property = resolveProperty(parts[0]);
        Sort.Direction direction = resolveDirection(parts[1]);
        return Sort.by(direction, property);
    }

    private static String resolveProperty(String property) {
        return switch (property) {
            case "id", "titulo", "descricao", "status", "dataAbertura", "dataAtualizacao" -> property;
            case "prioridade" -> "priority";
            default -> throw badRequest("Invalid sort field: " + property);
        };
    }

    private static Sort.Direction resolveDirection(String direction) {
        try {
            return Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException exception) {
            throw badRequest("Sort direction must be asc or desc");
        }
    }

    private static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
