package com.codexp.solutions.solutions.interfaces.rest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
@AutoConfigureMockMvc
class SolutionControllerSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Test
    void getSolution_requiresAuthentication() throws Exception {
        mockMvc
            .perform(get("/api/v1/solutions/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isForbidden());
    }

    @Test
    void updateSolution_allowsAuthenticatedRequestPipeline() throws Exception {
        String body = """
            {
              "code": "print('hello')"
            }
            """;

        mockMvc
            .perform(
                put("/api/v1/solutions/{id}", UUID.randomUUID().toString())
                    .header(AUTHORIZATION, bearerToken())
                    .contentType(APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isNotFound());
    }

    private String bearerToken() {
        return "Bearer " + generateStudentToken();
    }

    private String generateStudentToken() {
        SecretKey key;
        try {
            key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        } catch (IllegalArgumentException ignored) {
            key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        }

        return Jwts.builder()
            .subject("22222222-2222-2222-2222-222222222222")
            .claim("nickname", "student")
            .claim("email", "student@codexp.dev")
            .claim("role", "ROLE_STUDENT")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600_000))
            .id(UUID.randomUUID().toString())
            .signWith(key)
            .compact();
    }
}
