package com.api.levelup.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.api.levelup.dto.ErrorDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * Exception thrown when a resource is not found.
 *
 * @param resourceName the name of the resource that was not found
 * @param fieldName    the name of the field that was used to search for the
 *                     resource
 * @param fieldValue   the value of the field that was used to search for the
 *                     resource
 *
 * @see GlobalExceptionHandler
 * @see ErrorDetails
 *
 * @author Athirson Silva
 */
@Getter
@Setter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	private final String resourceName;
	private final String fieldName;
	private final long fieldValue;

	public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
		super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));

		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
}
