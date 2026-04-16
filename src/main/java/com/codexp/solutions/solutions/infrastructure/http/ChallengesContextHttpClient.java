package com.codexp.solutions.solutions.infrastructure.http;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.codexp.solutions.solutions.domain.exceptions.ChallengeContextFetchException;
import com.codexp.solutions.solutions.domain.services.ChallengeContextGateway;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;

@Component
public class ChallengesContextHttpClient implements ChallengeContextGateway {

    private final RestClient restClient;
    private final String internalToken;

    public ChallengesContextHttpClient(
        @Value("${app.challenges.base-url}") String challengesBaseUrl,
        @Value("${app.challenges.internal-token}") String internalToken
    ) {
        this.restClient = RestClient.builder()
            .baseUrl(challengesBaseUrl)
            .requestFactory(new org.springframework.http.client.JdkClientHttpRequestFactory(
                java.net.http.HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build()
            ))
            .build();
        this.internalToken = internalToken;
    }

    @Override
    public SubmitChallengeContext fetchSubmitContext(ChallengeId challengeId, TemplateLanguage language) {
        try {
            SubmitContextResponse response = restClient
                .get()
                .uri(uriBuilder ->
                    uriBuilder
                        .path("/api/v1/challenges/{challengeId}/solutions/submit-context")
                        .queryParam("language", language.value())
                        .build(challengeId.value().toString())
                )
                .header("X-Internal-Token", internalToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(SubmitContextResponse.class);

            if (response == null || response.entryFunctionName() == null || response.entryFunctionName().isBlank()) {
                throw new ChallengeContextFetchException("Challenge context response is missing entryFunctionName.");
            }

            if (response.testCases() == null || response.testCases().isEmpty()) {
                throw new ChallengeContextFetchException("Challenge context response is missing test cases.");
            }

            List<SubmitTestCase> mapped = response.testCases().stream()
                .map(testCase -> new SubmitTestCase(
                    testCase.testId(),
                    testCase.input(),
                    testCase.expectedOutput(),
                    testCase.isHidden()
                ))
                .toList();

            return new SubmitChallengeContext(response.entryFunctionName(), mapped);
        } catch (RestClientException ex) {
            throw new ChallengeContextFetchException("Failed to fetch challenge submit context: " + ex.getMessage());
        }
    }

    private record SubmitContextResponse(
        String entryFunctionName,
        List<SubmitTestCaseResponse> testCases
    ) {}

    private record SubmitTestCaseResponse(
        String testId,
        String input,
        String expectedOutput,
        Boolean isHidden
    ) {}
}
