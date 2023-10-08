package com.api.nextspring.exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.api.nextspring.dto.ErrorDetails;

import lombok.NonNull;

/**
 * This class is responsible for handling exceptions that occur in the
 * application.
 * It extends the ResponseEntityExceptionHandler class, which provides
 * convenient methods for handling exceptions.
 * 
 * @see ResponseEntityExceptionHandler
 * @see ResourceNotFoundException
 * @see RestApiException
 * 
 * @author Athirson Silva
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Extracts a list of errors from the stack trace of a
	 * MethodArgumentNotValidException.
	 * 
	 * @param exception The MethodArgumentNotValidException to extract errors from.
	 * @return A map of field names to error messages.
	 */
	private static Map<String, String> extractErrorListFromStackTrace(MethodArgumentNotValidException exception) {
		Map<String, String> errors = new HashMap<>();

		exception.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();

			errors.put(fieldName, message);
		});

		return errors;
	}

	/**
	 * Handles ResourceNotFoundExceptions by returning an HTTP 404 Not Found
	 * response with an ErrorDetails object.
	 * 
	 * @param exception The ResourceNotFoundException to handle.
	 * @param request   The WebRequest associated with the request.
	 * @return An HTTP 404 Not Found response with an ErrorDetails object.
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
			ResourceNotFoundException exception,
			WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(
				new Date(),
				exception.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles RestApiExceptions by returning an HTTP 400 Bad Request response with
	 * an ErrorDetails object.
	 * 
	 * @param exception The RestApiException to handle.
	 * @param request   The WebRequest associated with the request.
	 * @return An HTTP 400 Bad Request response with an ErrorDetails object.
	 */
	@ExceptionHandler(RestApiException.class)
	public ResponseEntity<ErrorDetails> handleRestApiException(
			RestApiException exception,
			WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(
				new Date(),
				exception.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles all other exceptions by returning an HTTP 500 Internal Server Error
	 * response with an ErrorDetails object.
	 * 
	 * @param exception The Exception to handle.
	 * @param request   The WebRequest associated with the request.
	 * @return An HTTP 500 Internal Server Error response with an ErrorDetails
	 *         object.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleGlobalException(
			Exception exception,
			WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(
				new Date(),
				exception.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles AccessDeniedExceptions by returning an HTTP 401 Unauthorized response
	 * with an ErrorDetails object.
	 * 
	 * @param exception The AccessDeniedException to handle.
	 * @param request   The WebRequest associated with the request.
	 * @return An HTTP 401 Unauthorized response with an ErrorDetails object.
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDetails> handleAccessDeniedException(
			AccessDeniedException exception,
			WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(
				new Date(),
				exception.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handles MethodArgumentNotValidExceptions by returning an HTTP 400 Bad Request
	 * response with a map of field names to error messages.
	 * 
	 * @param exception The MethodArgumentNotValidException to handle.
	 * @param headers   The HttpHeaders associated with the request.
	 * @param status    The HttpStatusCode associated with the request.
	 * @param request   The WebRequest associated with the request.
	 * @return An HTTP 400 Bad Request response with a map of field names to error
	 *         messages.
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			@NonNull MethodArgumentNotValidException exception,
			@NonNull HttpHeaders headers,
			@NonNull HttpStatusCode status,
			@NonNull WebRequest request) {
		Map<String, String> errors = extractErrorListFromStackTrace(exception);

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
