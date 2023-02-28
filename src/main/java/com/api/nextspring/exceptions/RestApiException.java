package com.api.nextspring.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

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
