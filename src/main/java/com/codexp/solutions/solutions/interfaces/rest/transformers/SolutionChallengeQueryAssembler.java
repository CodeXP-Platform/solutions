package com.codexp.solutions.solutions.interfaces.rest.transformers;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionByChallengeQuery;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionsByChallengeQuery;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;

public class SolutionChallengeQueryAssembler {

    public static GetSolutionByChallengeQuery toGetSolutionByChallengeQuery(
        String challengeId,
        String requesterId,
        UserRole requesterRole,
        String language
    ) {
        return new GetSolutionByChallengeQuery(
            ChallengeId.fromString(challengeId),
            AuthorId.fromString(requesterId),
            requesterRole,
            TemplateLanguage.fromString(language)
        );
    }

    public static GetSolutionsByChallengeQuery toGetSolutionsByChallengeQuery(
        String challengeId,
        String requesterId,
        UserRole requesterRole
    ) {
        return new GetSolutionsByChallengeQuery(
            ChallengeId.fromString(challengeId),
            AuthorId.fromString(requesterId),
            requesterRole
        );
    }
}
