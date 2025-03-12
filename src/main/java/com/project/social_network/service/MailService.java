package com.project.social_network.service;

import com.project.social_network.service.interfaces.BaseRedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class MailService {

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private TemplateEngine templateEngine;

  @Autowired
  private BaseRedisService baseRedisService; // ✅ Đúng dependency

  public String generateOTP() {
    Random random = new Random();
    int otp = 100000 + random.nextInt(900000);
    return String.valueOf(otp);
  }

  public void sendOTP(String email) throws MessagingException {
    String otp = generateOTP();

    baseRedisService.setWithTTL("OTP_" + email, otp, 5, TimeUnit.MINUTES);

    Context context = new Context();
    context.setVariable("otp", otp);

    String htmlContent = templateEngine.process("email/otp-email", context);

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setTo(email);
    helper.setSubject("Xác thực OTP - Đặt lại mật khẩu");
    helper.setText(htmlContent, true);

    mailSender.send(message);
  }

  public String getOTP(String email) {
    return (String) baseRedisService.get("OTP_" + email);
  }

  public void deleteOTP(String email) {
    baseRedisService.delete("OTP_" + email);
  }
}

