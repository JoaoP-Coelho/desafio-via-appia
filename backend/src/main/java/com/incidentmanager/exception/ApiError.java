package com.incidentmanager.exception;

import java.time.Instant;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiError {

	private final Instant timestamp;
	private final String path;
	private final String message;
	private final Map<String, String> details;
}