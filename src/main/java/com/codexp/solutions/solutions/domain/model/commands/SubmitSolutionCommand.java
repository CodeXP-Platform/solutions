package com.codexp.solutions.solutions.domain.model.commands;

import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;
import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;

public record SubmitSolutionCommand(
    SolutionId solutionId,
    AuthorId requesterId,
    UserRole requesterRole
) {}
