package com.codexp.solutions.shared.infrastructure.security;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

  @Value("${app.jwt.secret}")
  private String jwtSecret;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
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
    return UserRole.valueOf(extractAllClaims(token).get("role", String.class));
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
