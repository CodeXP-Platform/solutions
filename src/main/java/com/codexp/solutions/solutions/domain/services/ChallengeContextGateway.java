package com.codexp.solutions.solutions.domain.services;

import java.util.List;

import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;

public interface ChallengeContextGateway {

    SubmitChallengeContext fetchSubmitContext(ChallengeId challengeId, TemplateLanguage language);

    record SubmitChallengeContext(
        String templateCode,
        String language,
        String entryFunctionName,
        List<SubmitTestCase> testCases
    ) {}

    record SubmitTestCase(
        String testId,
        String input,
        String expectedOutput,
        Boolean isHidden
    ) {}
}
