package com.project.social_network.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.project.social_network.dto.IdTokenRequestDto;
import com.project.social_network.enums.AuthProvider;
import com.project.social_network.exceptions.UserException;
import com.project.social_network.model.Account;
import com.project.social_network.model.User;
import com.project.social_network.repository.UserRepository;
import com.project.social_network.service.interfaces.AccountService;
import com.project.social_network.util.JWTUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.google.api.client.json.JsonFactory;
import java.util.Collections;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

  private final JWTUtils jwtUtils;

  private final UserRepository userRepository;

  private final GoogleIdTokenVerifier verifier;

  public AccountServiceImpl(@Value("${app.googleClientId}") String clientId, UserRepository userRepository,
      JWTUtils jwtUtils) {
    this.userRepository = userRepository;
    this.jwtUtils = jwtUtils;
    NetHttpTransport transport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(Collections.singletonList(clientId))
        .build();
  }

  @Override
  public Account getAccount(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserException("User not found!"));
    Account account = new Account();
    account.setId(user.getId());
    account.setEmail(user.getEmail());
    account.setRoles("ROLE_USER");
    account.setPictureUrl(user.getImage());
    account.setFullName(user.getFullName());
    return account;
  }

  @Override
  public String loginOAuthGoogle(IdTokenRequestDto requestBody) {
    Account account = verifyIDToken(requestBody.getIdToken());
    if (account == null) {
      throw new IllegalArgumentException();
    }
    account = createOrUpdateUser(account);
    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
    Authentication authentication = new UsernamePasswordAuthenticationToken(account.getId(), null, authorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return jwtUtils.createToken(account, false);
  }

  @Override
  @Transactional
  public Account createOrUpdateUser(Account account) {
    User user = userRepository.findByEmail(account.getEmail()).orElse(new User());
    if (user.getId() == null) {
      user.setEmail(account.getEmail());
      user.setAuthProvider(AuthProvider.GOOGLE);
    }
    user.setFullName(account.getFullName());
    user.setImage(account.getPictureUrl());
    user = userRepository.save(user);

    return new Account(user.getId(), user.getFullName(), user.getEmail(), user.getImage(), "ROLE_USER");
  }

  private Account verifyIDToken(String idToken) {
    try {
      GoogleIdToken idTokenObj = verifier.verify(idToken);
      if (idTokenObj == null) {
        return null;
      }
      GoogleIdToken.Payload payload = idTokenObj.getPayload();
      String firstName = (String) payload.get("given_name");
      String lastName = (String) payload.get("family_name");
      String email = payload.getEmail();
      String pictureUrl = (String) payload.get("picture");

      String fullName = firstName + " " + lastName;

      return new Account(fullName, email, pictureUrl);
    } catch (GeneralSecurityException | IOException e) {
      return null;
    }
  }
}
