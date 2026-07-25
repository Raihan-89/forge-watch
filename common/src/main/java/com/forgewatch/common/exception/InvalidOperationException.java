package com.forgewatch.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an operation is invalid based on current business state.
 * Maps to HTTP 400 Bad Request.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }
}
