package com.api.levelup.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a custom exception for REST API errors. It extends the
 * RuntimeException class.
 * It contains the HTTP status code and a message describing the error.
 * 
 * @param status  The HTTP status code.
 * @param message A message describing the error.
 * 
 * @see RuntimeException
 * @see HttpStatus
 * @see GlobalExceptionHandler
 * 
 * @author Athirson Silva
 */
@Getter
@Setter
public class RestApiException extends RuntimeException {
	private final HttpStatus status;
	private final String message;

	public RestApiException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
