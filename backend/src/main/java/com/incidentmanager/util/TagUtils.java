package com.incidentmanager.util;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TagUtils {

    private TagUtils() {
    }

    public static List<String> normalizeTags(List<String> tags) {

        if (tags == null) {
            return List.of();
        }

        return tags.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(tag -> !tag.isBlank())
            .map(tag -> tag.toLowerCase(Locale.ROOT))
            .distinct()
            .sorted()
            .collect(Collectors.toCollection(ArrayList::new));
    }
}