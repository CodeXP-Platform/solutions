package com.codexp.solutions.solutions.domain.model.commands;

import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionCode;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;

public record CreateSolutionFromRequestedEventCommand(
    ChallengeId challengeId,
    AuthorId authorId,
    TemplateLanguage language,
    SolutionCode templateCode
) {
    public ChallengeId challengeId() {
        return challengeId;
    }

    public AuthorId authorId() {
        return authorId;
    }

    public TemplateLanguage language() {
        return language;
    }

    public SolutionCode templateCode() {
        return templateCode;
    }
}
