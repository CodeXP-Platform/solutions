package com.codexp.solutions.solutions.domain.model.queries;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;

public record GetSolutionByChallengeQuery(
    ChallengeId challengeId,
    AuthorId requesterId,
    UserRole requesterRole,
    TemplateLanguage language
) {}
