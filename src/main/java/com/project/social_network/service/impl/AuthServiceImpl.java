package com.project.social_network.service.impl;

import com.project.social_network.config.JwtProvider;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.User;
import com.project.social_network.model.Verification;
import com.project.social_network.repository.UserRepository;
import com.project.social_network.request.LoginRequest;
import com.project.social_network.request.RegisterRequest;
import com.project.social_network.request.ResetPasswordRequest;
import com.project.social_network.response.AuthResponse;
import com.project.social_network.service.interfaces.AuthService;
import com.project.social_network.service.interfaces.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final MailService mailService;

  private final JwtProvider jwtProvider;

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private CustomUserDetailsServiceImpl customUserDetailsService;


  @Override
  public String login(LoginRequest user) {
    String username = user.getEmail();
    String password = user.getPassword();

    Authentication authentication = authenticate(username, password);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return jwtProvider.generateToken(authentication);
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

  @Override
  public AuthResponse register(RegisterRequest user) {
    String password = user.getPassword();
    String fullName = user.getFullName();
    String birthDate = user.getBirthDate();

    userRepository.findByEmail(user.getEmail())
        .orElseThrow(() -> new UserException("Email already exists!"));

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

    return res;
  }

  @Override
  public void forgotPassword(String email) throws MessagingException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserException("User not found with email: " + email));
    if (user == null) {
      throw new UserException("User not found with email: " + email);
    }

    mailService.sendOTP(email);
  }

  @Override
  public void resetPassword(ResetPasswordRequest request) {
    String storedOTP = mailService.getOTP(request.getEmail());

    if (storedOTP == null || !storedOTP.equals(request.getOtp())) {
      throw new UserException("OTP does not match");
    }

    mailService.deleteOTP(request.getEmail());
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UserException("User not found with email: " + request.getEmail()));
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
  }
}
