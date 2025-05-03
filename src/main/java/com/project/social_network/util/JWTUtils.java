package com.project.social_network.util;

import com.project.social_network.config.JwtConstant;
import com.project.social_network.model.Account;
import com.project.social_network.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class JWTUtils {

  private static final long TOKEN_VALIDITY = 86400000L;
  private static final long TOKEN_VALIDITY_REMEMBER = 2592000000L;
  SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

  public String createToken(Account account, boolean rememberMe) {
    long now = (new Date()).getTime();
    Date validity = rememberMe ? new Date(now + TOKEN_VALIDITY_REMEMBER) : new Date(now + TOKEN_VALIDITY);
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", account.getEmail());

    String token =  Jwts.builder()
        .setSubject(account.getId().toString())
        .setIssuedAt(new Date())
        .setExpiration(validity)
        .addClaims(claims)
        .signWith(key)
        .compact();

    return token;
  }

  public Authentication verifyAndGetAuthentication(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
      List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get("email", String.class));
      return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, authorities);
    } catch (JwtException | IllegalArgumentException ignored) {
      return null;
    }
  }
}
