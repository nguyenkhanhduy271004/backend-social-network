package com.project.social_network.controller;

import com.project.social_network.config.JwtProvider;
import com.project.social_network.dto.IdTokenRequestDto;
import com.project.social_network.exceptions.UserException;
import com.project.social_network.model.User;
import com.project.social_network.model.Verification;
import com.project.social_network.repository.UserRepository;
import com.project.social_network.request.LoginRequest;
import com.project.social_network.request.RegisterRequest;
import com.project.social_network.response.AuthResponse;
import com.project.social_network.service.impl.AccountServiceImpl;
import com.project.social_network.service.impl.MailServiceImpl;
import com.project.social_network.service.impl.CustomUserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AuthController {

  private final JwtProvider jwtProvider;

  private final MailServiceImpl mailServiceImpl;

  private final UserRepository userRepository;

  private final AccountServiceImpl accountServiceImpl;

  private final PasswordEncoder passwordEncoder;

  private final CustomUserDetailsServiceImpl customUserDetailsService;


  @PostMapping("/register")
  public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody RegisterRequest user)
      throws UserException {
    String password = user.getPassword();
    String fullName = user.getFullName();
    String birthDate = user.getBirthDate();

    User isEmailExist = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UserException("Email already exists!"));

    User newUser = new User();
    newUser.setEmail(user.getEmail());
    newUser.setPassword(passwordEncoder.encode(password));
    newUser.setFullName(fullName);
    newUser.setBirthDate(birthDate);
    newUser.setVerification(new Verification());

    userRepository.save(newUser);
    Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),
        password);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtProvider.generateToken(authentication);
    AuthResponse res = new AuthResponse(token, true);

    return new ResponseEntity<>(res, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest user) throws UserException {
    String username = user.getEmail();
    String password = user.getPassword();

    Authentication authentication = authenticate(username, password);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtProvider.generateToken(authentication);

    return ResponseEntity.ok(new AuthResponse(token, true));
  }

  @PostMapping("/login-oauth2")
  public ResponseEntity<AuthResponse> LoginWithGoogleOauth2(@RequestBody IdTokenRequestDto requestBody) throws UserException {
    String token = accountServiceImpl.loginOAuthGoogle(requestBody);
    return ResponseEntity.ok(new AuthResponse(token, true));
  }


  private Authentication authenticate(String username, String password) {
    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
    if (userDetails == null) {
      throw new BadCredentialsException("Invalid username...");
    }
    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
      throw new BadCredentialsException("Invalid username or password...");
    }
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@RequestParam String email) throws MessagingException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserException("User not found with email: " + email));
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found!");
    }

    mailServiceImpl.sendOTP(email);
    return ResponseEntity.ok("OTP has been sent to your email!");
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String otp,
      @RequestParam String newPassword) {
    String storedOTP = mailServiceImpl.getOTP(email);

    if (storedOTP == null || !storedOTP.equals(otp)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP!");
    }

    mailServiceImpl.deleteOTP(email);
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserException("User not found with email: " + email));
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    return ResponseEntity.ok("Password has been successfully reset!");
  }
}
