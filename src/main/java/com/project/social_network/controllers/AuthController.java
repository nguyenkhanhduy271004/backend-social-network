package com.project.social_network.controllers;

import com.project.social_network.config.JwtProvider;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.entities.User;
import com.project.social_network.models.entities.Verification;
import com.project.social_network.models.requests.LoginRequest;
import com.project.social_network.models.requests.RegisterRequest;
import com.project.social_network.repositories.UserRepository;
import com.project.social_network.models.responses.AuthResponse;
import com.project.social_network.services.MailService;
import com.project.social_network.services.impl.CustomUserDetailsServiceImpl;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    if(isEmailExist != null) {
      throw new UserException("Email đã tồn tại!");
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

    return new ResponseEntity<AuthResponse>(res, HttpStatus.CREATED);
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


  private Authentication authenticate(String username, String password) {
    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

    if(userDetails == null) {
      throw new BadCredentialsException("Invalid username...");
    }

    if(!passwordEncoder.matches(password, userDetails.getPassword())) {
      throw new BadCredentialsException("Invalid username or password...");
    }

    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@RequestParam String email) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không tồn tại!");
    }

    mailService.sendOTP(email);

    return ResponseEntity.ok("Mã OTP đã được gửi đến email của bạn!");
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
    String storedOTP = mailService.getOTP(email);

    if (storedOTP == null || !storedOTP.equals(otp)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP không hợp lệ hoặc đã hết hạn!");
    }

    mailService.deleteOTP(email);

    User user = userRepository.findByEmail(email);
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công!");
  }

}
