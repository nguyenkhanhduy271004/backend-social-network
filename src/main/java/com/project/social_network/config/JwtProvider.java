package com.project.social_network.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {

  SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

  public String generateToken(Authentication auth) {
    String email;

    if (auth.getPrincipal() instanceof OAuth2User) {
      OAuth2User oAuth2User = (OAuth2User) auth.getPrincipal();
      email = oAuth2User.getAttribute("email");

      if (email == null) {
        String id = oAuth2User.getAttribute("id");
        String name = oAuth2User.getAttribute("name");
        if (id != null) {
          email = id + "@oauth2user";
        } else if (name != null) {
          email = name.replaceAll("\\s+", ".").toLowerCase() + "@oauth2user";
        } else {
          email = "unknown@oauth2user";
        }
      }
    } else {
      email = auth.getName();
    }

    String jwt = Jwts.builder()
        .setIssuedAt(new Date())
        .setExpiration(new Date(new Date().getTime() + 86400000))
        .claim("email", email)
        .signWith(key)
        .compact();

    return jwt;
  }

  public String getEmailFromToken(String jwt) {
    jwt = jwt.substring(7);

    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

    String email = String.valueOf(claims.get("email"));

    return email;
  }
}
