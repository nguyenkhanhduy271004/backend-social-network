package com.project.social_network.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordRequest {

  @NotNull(message = "Email must be not null")
  private String email;

  @NotNull(message = "Otp must be not null")
  private String otp;

  @NotNull(message = "New password must be not null")
  private String newPassword;

}
