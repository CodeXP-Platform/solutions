package com.codexp.solutions.shared.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.codexp.solutions.shared.domain.exceptions.UnauthorizedActionException;
import com.codexp.solutions.solutions.domain.exceptions.AttemptsLimitReachedException;
import com.codexp.solutions.solutions.domain.exceptions.ChallengeContextFetchException;
import com.codexp.solutions.solutions.domain.exceptions.SolutionNotFoundException;
import com.codexp.solutions.solutions.domain.exceptions.SolutionOwnershipException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
        var errorResponse = new ErrorResponse("Malformed request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({ UnauthorizedActionException.class, SolutionOwnershipException.class })
    public ResponseEntity<ErrorResponse> handleForbidden(RuntimeException ex) {
        var errorResponse = new ErrorResponse("Unauthorized action", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(SolutionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        var errorResponse = new ErrorResponse("Resource not found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ChallengeContextFetchException.class)
    public ResponseEntity<ErrorResponse> handleChallengeContextUnavailable(ChallengeContextFetchException ex) {
        var errorResponse = new ErrorResponse("Challenge context unavailable", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler(AttemptsLimitReachedException.class)
    public ResponseEntity<ErrorResponse> handleTooManyRequests(AttemptsLimitReachedException ex) {
        var errorResponse = new ErrorResponse("Attempts limit reached", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(IllegalArgumentException ex) {
        var errorResponse = new ErrorResponse("Validation error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(errorResponse);
    }
}
