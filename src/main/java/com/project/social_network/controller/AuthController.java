package com.project.social_network.controller;

import com.project.social_network.dto.IdTokenRequestDto;
import com.project.social_network.request.LoginRequest;
import com.project.social_network.request.RegisterRequest;
import com.project.social_network.request.ResetPasswordRequest;
import com.project.social_network.response.AuthResponse;
import com.project.social_network.service.impl.AccountServiceImpl;
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


  @PostMapping("/register")
  ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest user) {
    AuthResponse res = authService.register(user);

    return new ResponseEntity<>(res, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  ResponseEntity<AuthResponse> login(@RequestBody LoginRequest user) {
    String token = authService.login(user);

    return ResponseEntity.ok(new AuthResponse(token, true));
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
