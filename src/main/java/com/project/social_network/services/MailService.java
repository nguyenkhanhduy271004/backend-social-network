package com.project.social_network.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class MailService {

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  public String generateOTP() {
    Random random = new Random();
    int otp = 100000 + random.nextInt(900000);
    return String.valueOf(otp);
  }

  public void sendOTP(String email) {
    String otp = generateOTP();

    redisTemplate.opsForValue().set("OTP_" + email, otp, 5, TimeUnit.MINUTES);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Xác thực OTP - Đặt lại mật khẩu");
    message.setText("Mã OTP của bạn là: " + otp + ". Mã này có hiệu lực trong 5 phút.");

    mailSender.send(message);
  }

  public String getOTP(String email) {
    return redisTemplate.opsForValue().get("OTP_" + email);
  }

  public void deleteOTP(String email) {
    redisTemplate.delete("OTP_" + email);
  }
}
