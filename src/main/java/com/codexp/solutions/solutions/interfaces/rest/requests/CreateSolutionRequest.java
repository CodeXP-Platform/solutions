package com.codexp.solutions.solutions.interfaces.rest.requests;

public record CreateSolutionRequest(
    String challengeId,
    String language
) {}
