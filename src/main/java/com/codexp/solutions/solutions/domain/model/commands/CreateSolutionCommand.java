package com.codexp.solutions.solutions.domain.model.commands;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;

public record CreateSolutionCommand(
    ChallengeId challengeId,
    AuthorId requesterId,
    UserRole requesterRole,
    TemplateLanguage language
) {
    public ChallengeId challengeId() {
        return challengeId;
    }

    public AuthorId requesterId() {
        return requesterId;
    }

    public UserRole requesterRole() {
        return requesterRole;
    }

    public TemplateLanguage language() {
        return language;
    }
}
