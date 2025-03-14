package com.project4.restaurant.domain.provider;

import com.project4.restaurant.domain.entity.type.AccountRole;
import com.project4.restaurant.domain.pojo.PayloadInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenProvider {
  @Value("${spring.security.jwt.access-token-expired-ms}") //1 day
  private long accessTokenValidityInMilliseconds;

  @Value("${spring.security.jwt.refresh-token-expired-ms:2592000000}") // 30 days
  private long refreshTokenValidityInMilliseconds;

  private final SecretKey secretKey;

  public JwtTokenProvider(@Value("${${spring.security.jwt.secret-key:xyz}") String secretKeyString) {
    this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
  }

  public long getExpiredRefreshToken() {
    return refreshTokenValidityInMilliseconds;
  }

  public String resolveToken(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }

  public String createAccessToken(String username, Long userId, AccountRole role) {
    List<AccountRole> roles = List.of(role);
    return createToken(username, userId, roles, accessTokenValidityInMilliseconds);
  }

  public String createRefreshToken(String username, Long userId, AccountRole role) {
    List<AccountRole> roles = List.of(role);
    return createToken(username, userId, roles, refreshTokenValidityInMilliseconds);
  }

  private String createToken(String username, Long userId, List<AccountRole> roles, long validity) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("sub", username);
    claims.put("roles", roles);
    claims.put("userId", userId);

    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + validity);

    return Jwts.builder()
        .claims(claims)
        .issuedAt(now)
        .expiration(expirationDate)
        .signWith(secretKey)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public PayloadInfo getPayloadInfo(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    return PayloadInfo.builder()
        .username(claims.getSubject())
        .userId((Integer) claims.get("userId"))
        .roles((List<AccountRole>) claims.get("roles"))
        .build();
  }

  public String getUsername(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    return claims.getSubject();
  }
}