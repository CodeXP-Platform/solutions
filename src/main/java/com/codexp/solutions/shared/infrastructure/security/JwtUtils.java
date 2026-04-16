package com.codexp.solutions.shared.infrastructure.security;

import com.codexp.solutions.shared.domain.model.valueobjects.JwtPrincipal;
import com.codexp.solutions.shared.domain.model.valueobjects.NickName;
import com.codexp.solutions.shared.domain.model.valueobjects.UserEmail;
import com.codexp.solutions.shared.domain.model.valueobjects.UserId;
import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

  @Value("${app.jwt.secret}")
  private String jwtSecret;

  private SecretKey getSigningKey() {
    try {
      return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    } catch (IllegalArgumentException ignored) {
      return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parser()
      .verifyWith(getSigningKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public String extractUserId(String token) {
    return extractAllClaims(token).getSubject();
  }

  public String extractNickname(String token) {
    return extractAllClaims(token).get("nickname", String.class);
  }

  public String extractEmail(String token) {
    return extractAllClaims(token).get("email", String.class);
  }

  public UserRole extractRole(String token) {
    return UserRole.fromClaim(extractAllClaims(token).get("role", String.class));
  }

  public Optional<JwtPrincipal> extractPrincipal(String token) {
    try {
      Claims claims = extractAllClaims(token);

      String userId = claims.getSubject();
      String nickname = claims.get("nickname", String.class);
      String email = claims.get("email", String.class);
      UserRole role = UserRole.fromClaim(claims.get("role", String.class));

      return Optional.of(new JwtPrincipal(
          UserId.fromString(userId),
          NickName.fromString(nickname),
          UserEmail.fromString(email),
          role
      ));
    } catch (JwtException | IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  public boolean isTokenValid(String token) {
    try {
      extractAllClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}
