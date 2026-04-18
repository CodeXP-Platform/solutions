package com.codexp.solutions.solutions.domain.model.queries;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;

public record GetSolutionByIdQuery(
    SolutionId solutionId,
    AuthorId requesterId,
    UserRole requesterRole
) {}
