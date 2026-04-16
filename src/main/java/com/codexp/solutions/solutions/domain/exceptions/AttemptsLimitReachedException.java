package com.codexp.solutions.solutions.domain.exceptions;

public class AttemptsLimitReachedException extends RuntimeException {

    public AttemptsLimitReachedException(String message) {
        super(message);
    }
}
