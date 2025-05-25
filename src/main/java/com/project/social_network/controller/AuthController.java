package com.project.social_network.controller;

import com.project.social_network.config.JwtProvider;
import com.project.social_network.dto.IdTokenRequestDto;
import com.project.social_network.request.LoginRequest;
import com.project.social_network.request.RegisterRequest;
import com.project.social_network.request.ResetPasswordRequest;
import com.project.social_network.response.AuthResponse;
import com.project.social_network.service.impl.AccountServiceImpl;
import com.project.social_network.service.impl.CustomUserDetailsServiceImpl;
import com.project.social_network.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller")
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AuthController {

  AuthService authService;

  AccountServiceImpl accountServiceImpl;
  private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;


  @PostMapping("/register")
  ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest user) {
    AuthResponse res = authService.register(user);

    return new ResponseEntity<>(res, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest user) {
    String username = user.getEmail();
    String password = user.getPassword();

    Authentication authentication = authenticate(username, password);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtProvider.generateToken(authentication);
    return ResponseEntity.ok(new AuthResponse(token, true));
  }

  private Authentication authenticate(String username, String password) {
    UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
    if (userDetails == null) {
      throw new BadCredentialsException("Invalid username...");
    }
    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
      throw new BadCredentialsException("Invalid username or password...");
    }
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  @PostMapping("/login-oauth2")
  ResponseEntity<AuthResponse> LoginWithGoogleOauth2(@RequestBody IdTokenRequestDto requestBody) {
    String token = accountServiceImpl.loginOAuthGoogle(requestBody);
    return ResponseEntity.ok(new AuthResponse(token, true));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(
      @Valid @RequestParam @NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email format") String email)
      throws MessagingException {
    authService.forgotPassword(email);
    return ResponseEntity.ok("OTP has been sent to your email!");
  }

  @PostMapping("/reset-password")
  ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    authService.resetPassword(request);

    return ResponseEntity.ok("Password has been successfully reset!");
  }

}
