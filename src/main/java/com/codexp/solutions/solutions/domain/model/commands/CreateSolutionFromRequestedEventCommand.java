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
) {}
