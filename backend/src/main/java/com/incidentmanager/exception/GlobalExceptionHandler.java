package com.incidentmanager.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		Map<String, String> details = new HashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			details.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return buildResponse(HttpStatus.BAD_REQUEST, request, "Validation failed", details);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleConstraintViolation(
			ConstraintViolationException ex,
			HttpServletRequest request) {
		Map<String, String> details = new HashMap<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			details.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return buildResponse(HttpStatus.BAD_REQUEST, request, "Validation failed", details);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handleAuthenticationException(
			AuthenticationException ex,
			HttpServletRequest request) {
		return buildResponse(HttpStatus.UNAUTHORIZED, request, "Invalid credentials", Map.of());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest request) {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, ex.getMessage(), Map.of());
	}
	private ResponseEntity<ApiError> buildResponse(
			HttpStatus status,
			HttpServletRequest request,
			String message,
			Map<String, String> details) {
		ApiError error = new ApiError(Instant.now(), request.getRequestURI(), message, details);
		return ResponseEntity.status(status).body(error);
	}
}