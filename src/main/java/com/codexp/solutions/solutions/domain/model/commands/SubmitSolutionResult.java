package com.codexp.solutions.solutions.domain.model.commands;

import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptId;

public record SubmitSolutionResult(
    Solution solution,
    AttemptId attemptId
) {}
