package com.project.social_network.service.interfaces;

import com.project.social_network.request.LoginRequest;
import com.project.social_network.request.RegisterRequest;
import com.project.social_network.request.ResetPasswordRequest;
import com.project.social_network.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface AuthService {

  String login(LoginRequest user);
  AuthResponse register(RegisterRequest user);
  void forgotPassword(String email) throws MessagingException;
  void resetPassword(ResetPasswordRequest request);

}
