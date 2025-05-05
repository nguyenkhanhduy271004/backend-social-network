package com.project.social_network.service.interfaces;

import jakarta.mail.MessagingException;

public interface MailService {

  String generateOTP();

  void sendOTP(String email) throws MessagingException;

  String getOTP(String email);

  void deleteOTP(String email);
}