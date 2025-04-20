package com.project.social_network.controller;

import com.project.social_network.config.JwtProvider;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.User;
import com.project.social_network.model.Verification;
import com.project.social_network.request.LoginRequest;
import com.project.social_network.request.RegisterRequest;
import com.project.social_network.repository.UserRepository;
import com.project.social_network.response.AuthResponse;
import com.project.social_network.service.MailService;
import com.project.social_network.service.impl.CustomUserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth Controller")
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  private MailService mailService;

  @Autowired
  private CustomUserDetailsServiceImpl customUserDetailsService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody RegisterRequest user) throws UserException {
    String email = user.getEmail();
    String password = user.getPassword();
    String fullName = user.getFullName();
    String birthDate = user.getBirthDate();

    User isEmailExist = userRepository.findByEmail(email);
    if (isEmailExist != null) {
      throw new UserException("Email already exists!");
    }

    User newUser = new User();
    newUser.setEmail(email);
    newUser.setPassword(passwordEncoder.encode(password));
    newUser.setFullName(fullName);
    newUser.setBirthDate(birthDate);
    newUser.setVerification(new Verification());

    User savedUser = userRepository.save(newUser);
    Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtProvider.generateToken(authentication);
    AuthResponse res = new AuthResponse(token, true);

    return new ResponseEntity<>(res, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest user, HttpServletRequest request) throws UserException {
    String username = user.getEmail();
    String password = user.getPassword();

    Authentication authentication = authenticate(username, password);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String ipAddress = getClientIp(request);
    System.out.println("User logged in from IP: " + ipAddress);

    String token = jwtProvider.generateToken(authentication);

    return ResponseEntity.ok(new AuthResponse(token, true));
  }

  private String getClientIp(HttpServletRequest request) {
    String ipAddress = request.getHeader("X-Forwarded-For");
    if (ipAddress == null || ipAddress.isEmpty()) {
      ipAddress = request.getRemoteAddr();
    }
    return ipAddress;
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
    User user = userRepository.findByEmail(email);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found!");
    }

    mailService.sendOTP(email);
    return ResponseEntity.ok("OTP has been sent to your email!");
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
    String storedOTP = mailService.getOTP(email);

    if (storedOTP == null || !storedOTP.equals(otp)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP!");
    }

    mailService.deleteOTP(email);
    User user = userRepository.findByEmail(email);
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    return ResponseEntity.ok("Password has been successfully reset!");
  }


}
