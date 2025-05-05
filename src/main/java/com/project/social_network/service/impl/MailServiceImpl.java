package com.project.social_network.service.impl;

import com.project.social_network.service.interfaces.MailService;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.project.social_network.service.interfaces.BaseRedisService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

  private final JavaMailSender mailSender;

  private final TemplateEngine templateEngine;

  private final BaseRedisService baseRedisService;

  @Override
  public String generateOTP() {
    Random random = new Random();
    int otp = 100000 + random.nextInt(900000);
    return String.valueOf(otp);
  }

  @Override
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

  @Override
  public String getOTP(String email) {
    return (String) baseRedisService.get("OTP_" + email);
  }

  @Override
  public void deleteOTP(String email) {
    baseRedisService.delete("OTP_" + email);
  }
}
