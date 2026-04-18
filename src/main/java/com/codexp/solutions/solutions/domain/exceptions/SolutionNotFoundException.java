package com.codexp.solutions.solutions.domain.exceptions;

public class SolutionNotFoundException extends RuntimeException {

    public SolutionNotFoundException() {
        super("Solution not found.");
    }
}
